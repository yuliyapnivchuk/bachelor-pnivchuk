package com.itstep.repository;

import com.itstep.entity.Expense;
import com.itstep.entity.ExpenseItemProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    @Query(value = """
            SELECT
                e.id AS expenseId,
                i.id AS itemId,
                i.total_price AS totalPrice,
                e.currency,
                i.split_type AS splitType,
                s.value AS value,
                s.user_name AS userName
            FROM
                item i
            JOIN
                split_details s ON i.id = s.item_id
            JOIN
                expense e ON e.id = i.expense_id
            WHERE
                e.payer = :payer
            AND
                e.status = 'SUBMITTED'
            UNION
            SELECT
                e.id,
                e.id,
                e.subtotal_amount,
                e.currency,
                e.split_type,
                s.value,
                s.user_name
            FROM
                expense e
            JOIN
                split_details s on s.expense_id = e.id
            WHERE
                e.payer = :payer
            AND
                e.status = 'SUBMITTED'
             """, nativeQuery = true)
    List<ExpenseItemProjection> findUserIsOwedItems(@Param("payer") String payer);

    @Query(value = """
            SELECT
                e.id AS expenseId,
                e.payer AS payer,
                i.id AS itemId,
                i.total_price AS totalPrice,
                e.currency,
                i.split_type AS splitType,
                s.value AS value,
                s.user_name AS userName
            FROM
                item i
            JOIN
                split_details s ON i.id = s.item_id
            JOIN
                expense e ON e.id = i.expense_id
            WHERE
                e.payer != :payer
            AND
                e.status = 'SUBMITTED'
            UNION
            SELECT
                e.id,
                e.payer,
                e.id,
                e.subtotal_amount,
                e.currency,
                e.split_type,
                s.value,
                s.user_name
            FROM
                expense e
            JOIN
                split_details s on s.expense_id = e.id
            WHERE
                e.payer != :payer
            AND
                e.status = 'SUBMITTED'
            """, nativeQuery = true)
    List<ExpenseItemProjection> findUserOweItems(@Param("payer") String payer);

    @Modifying
    @Transactional
    @Query(value = "UPDATE expense SET image = :image WHERE id = :id", nativeQuery = true)
    void updateImageName(@Param("image") String name, @Param("id") Integer id);

    List<Expense> findByEventId(Integer eventId);
}