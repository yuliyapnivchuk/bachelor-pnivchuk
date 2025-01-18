package com.itstep.repository;

import com.itstep.entity.SplitItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SplitItemRepository extends JpaRepository<SplitItem, Integer> {
}
