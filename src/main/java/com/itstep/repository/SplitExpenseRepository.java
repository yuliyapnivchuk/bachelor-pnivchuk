package com.itstep.repository;

import com.itstep.entity.SplitExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SplitExpenseRepository extends JpaRepository<SplitExpense, Integer> {
}
