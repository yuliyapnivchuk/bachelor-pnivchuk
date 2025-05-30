package com.itstep.repository;

import com.itstep.entity.Expense;
import com.itstep.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByExpense(Expense expense);
}