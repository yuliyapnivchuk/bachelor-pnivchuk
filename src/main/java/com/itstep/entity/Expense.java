package com.itstep.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "expense")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
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

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SplitDetails> splitDetails = new ArrayList<>();

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Column(name = "transaction_time")
    private LocalTime transactionTime;

    @Column(name = "category")
    private String category;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    public void setSplitDetails(List<SplitDetails> splitDetails) {
        if (splitDetails == null) {
            this.splitDetails.clear();
        } else {
            splitDetails.forEach(i -> i.setExpense(this));
            this.splitDetails = splitDetails;
        }
    }

    public void setItems(List<Item> items) {
        if (items == null) {
            this.items.clear();
        } else {
            items.forEach(i -> i.setExpense(this));
            this.items = items;
        }
    }
}