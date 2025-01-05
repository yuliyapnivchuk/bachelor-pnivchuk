package com.itstep.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "item")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "expense_id", referencedColumnName = "id")
    private Expense expense;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "split_type")
    private String splitType;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<SplitItem> splitDetails;
}