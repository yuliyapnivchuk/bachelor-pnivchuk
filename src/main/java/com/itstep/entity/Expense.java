package com.itstep.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "expense")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "payer", referencedColumnName = "name")
    private User payer;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "name")
    private User createdBy;

    @Column(name = "summary")
    private String summary;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "subtotal_amount")
    private Double subtotalAmount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "split_type")
    private String splitType;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private List<SplitExpense> splitDetails;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Column(name = "transaction_time")
    private LocalTime transactionTime;

    @Column(name = "category")
    private String category;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private List<Item> items = new ArrayList<>();
}