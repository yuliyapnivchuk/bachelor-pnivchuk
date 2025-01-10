package com.itstep.repository;

import com.itstep.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
            u.name AS userName
        FROM 
            item i
        JOIN 
            item_share s ON i.id = s.item_id
        JOIN 
            users u ON s.user_id = u.id
        JOIN 
            expense e ON e.id = i.expense_id
        WHERE 
            e.payer = :payer
        UNION
        SELECT
            e.id, 
            e.id, 
            e.subtotal_amount,
            e.currency, 
            e.split_type, 
            s.value, 
            u.name
        FROM 
            expense e
        JOIN
            expense_share s on s.expense_id = e.id
        JOIN
            users u on s.user_id = u.id
        WHERE 
            e.payer = :payer
        """, nativeQuery = true)
    List<ExpenseItemProjection> findItems(@Param("payer") String payer);
}