package com.itstep.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "split_details")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SplitDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "expense_id", referencedColumnName = "id")
    @ToString.Exclude
    private Expense expense;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_name", referencedColumnName = "name")
    private User user;

    @Column(name = "value")
    private Double value;
}
