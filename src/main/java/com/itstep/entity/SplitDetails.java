package com.itstep.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "split_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SplitDetails {

    @Transient
    private final UUID tempUuid = UUID.randomUUID();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Expense expense;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "value")
    private Double value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SplitDetails)) return false;
        return id != null && id.equals(((SplitDetails) o).getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : Objects.hash(tempUuid);
    }
}
