package com.itstep.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "expense_id", referencedColumnName = "id")
    private Expense expense;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "name")
    private User createdBy;

    @Column(name = "note_text")
    private String noteText;
}