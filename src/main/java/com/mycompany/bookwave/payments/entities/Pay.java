package com.mycompany.bookwave.payments.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mycompany.bookwave.users.entities.MemberShipCard;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "pays")
public class Pay {
    
    @Id
    @Column(name = "id", nullable = false, length = 50)
    String id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private PayStatus status;

    @ManyToOne
    @JoinColumn(name = "member_ship_card_id", nullable = false)
    private MemberShipCard memberShipCard;
}
