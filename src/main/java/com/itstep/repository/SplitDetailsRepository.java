package com.itstep.repository;

import com.itstep.entity.Expense;
import com.itstep.entity.Item;
import com.itstep.entity.SplitDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SplitDetailsRepository extends JpaRepository<SplitDetails, Integer> {
    List<SplitDetails> findByExpense(Expense expense);
    List<SplitDetails> findByItem(Item item);
}
