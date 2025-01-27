package com.itstep.entity;

import jakarta.persistence.*;
import lombok.*;

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
    @ToString.Exclude
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
    @EqualsAndHashCode.Exclude
    private List<SplitDetails> splitDetails;
}