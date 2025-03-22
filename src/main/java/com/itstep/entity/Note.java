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
@Builder
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "expense_id")
    private Integer expenseId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "name")
    private User createdBy;

    @Column(name = "note_text")
    private String noteText;
}