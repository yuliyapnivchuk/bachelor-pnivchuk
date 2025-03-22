package com.itstep.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "item")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Item {

    @Transient
    private final UUID tempUuid = UUID.randomUUID();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
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

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SplitDetails> splitDetails = new ArrayList<>();

    public void setSplitDetails(List<SplitDetails> splitDetails) {
        if (splitDetails == null) {
            this.splitDetails.clear();
        } else {
            splitDetails.forEach(i -> i.setItem(this));
            this.splitDetails = splitDetails;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        return id != null && id.equals(((Item) o).getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : Objects.hash(tempUuid);
    }
}