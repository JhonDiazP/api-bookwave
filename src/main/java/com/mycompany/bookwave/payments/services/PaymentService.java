package com.mycompany.bookwave.payments.services;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mycompany.bookwave.catalog.controllers.CatalogInternalApi;
import com.mycompany.bookwave.catalog.entities.Book;
import com.mycompany.bookwave.common.DTOs.BuyDTO;
import com.mycompany.bookwave.orders.controllers.OrderInternalApi;
import com.mycompany.bookwave.orders.entities.ShoppingBook;
import com.mycompany.bookwave.orders.entities.ShoppingCartStatus;
import com.mycompany.bookwave.orders.entities.ShoppingCarts;
import com.mycompany.bookwave.payments.controllers.PaymentExternalApi;
import com.mycompany.bookwave.payments.controllers.PaymentInternalApi;
import com.mycompany.bookwave.payments.entities.Pay;
import com.mycompany.bookwave.payments.entities.PayShoppingBook;
import com.mycompany.bookwave.payments.entities.PayStatus;
import com.mycompany.bookwave.payments.repositories.PayRepository;
import com.mycompany.bookwave.payments.repositories.PayShoppingBookRepository;
import com.mycompany.bookwave.users.controllers.UserInternalApi;
import com.mycompany.bookwave.users.entities.MemberShipCard;
import com.mycompany.bookwave.users.entities.User;

import jakarta.transaction.Transactional;

@Service
public class PaymentService implements PaymentInternalApi, PaymentExternalApi {
    
    private final OrderInternalApi orderInternalApi;
    private final CatalogInternalApi catalogInternalApi;
    private final UserInternalApi userInternalApi;
    private final PayRepository payRepository;
    private final PayShoppingBookRepository payShoppingBookRepository;

    public PaymentService(  OrderInternalApi orderInternalApi, 
                            CatalogInternalApi catalogInternalApi, 
                            UserInternalApi userInternalApi, 
                            PayRepository payRepository,
                            PayShoppingBookRepository payShoppingBookRepository
                            ) {
        this.orderInternalApi = orderInternalApi;
        this.catalogInternalApi = catalogInternalApi;
        this.userInternalApi = userInternalApi;
        this.payRepository = payRepository;
        this.payShoppingBookRepository = payShoppingBookRepository;
    }

    @Override
    // @Transactional
    public ResponseEntity<Map<String, Object>> savePayment(BuyDTO buyDTO) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        try {
            MemberShipCard memberShipCard = userInternalApi.findMemberShipCartById(buyDTO.memberShitCart());
            if (memberShipCard.getPassword().equals(buyDTO.password())) {
                List<Book> books = orderInternalApi.getBooksInShoppingCard(buyDTO.shoppingCartId());
                BigDecimal total = new BigDecimal(0);
                for (Book book : books) {
                    total = total.add(book.getPrice());
                }
                if (memberShipCard.getQuota().compareTo(total) >= 1) {
                    for (Book book : books) {
                        Book updateBook = catalogInternalApi.getBookById(book.getIsbn());
                        updateBook.setAmount(updateBook.getAmount() - 1);
                        catalogInternalApi.saveBook(updateBook);
                    }
                    memberShipCard.setQuota(memberShipCard.getQuota().subtract(total));
                    userInternalApi.saveMemberShipCart(memberShipCard);

                    Pay pay = new Pay();
                    UUID uuid = UUID.randomUUID();
                    pay.setId(uuid.toString());
                    pay.setAmount(total);
                    LocalDate date = LocalDate.now();
                    pay.setDate(date);
                    PayStatus payStatus = new PayStatus();
                    payStatus.setId(1);
                    pay.setStatus(payStatus);
                    pay.setMemberShipCard(memberShipCard);
                    payRepository.save(pay);

                    List<ShoppingBook> shoppingBooks = orderInternalApi
                            .getShoppingBooksByShoppingCartId(buyDTO.shoppingCartId());

                    for (ShoppingBook shoppingBook : shoppingBooks) {
                        PayShoppingBook payShoppingBook = new PayShoppingBook();
                        payShoppingBook.setPay(pay);
                        payShoppingBook.setShoppingBook(shoppingBook);
                        payShoppingBookRepository.save(payShoppingBook);
                    }

                    ShoppingCarts shoppingCarts = orderInternalApi.getShoppingCartById(buyDTO.shoppingCartId());
                    ShoppingCartStatus shoppingCartStatus = new ShoppingCartStatus();
                    shoppingCartStatus.setId(2);
                    shoppingCarts.setStatus(shoppingCartStatus);
                    orderInternalApi.saveShoppingCart(shoppingCarts);

                    User user = userInternalApi.getUserById(shoppingCarts.getUser().getId());
                    orderInternalApi.createShoppingCard(user);

                    map.put("status", HttpStatus.OK);
                    map.put("message", "Payment successful");
                } else {
                    map.put("message", "Quota is not enough");
                    return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
                }
            } else {
                map.put("message", "Password is incorrect");
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            map.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            map.put("message", "Error saving payment");
            map.put("error", e.getMessage());
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
