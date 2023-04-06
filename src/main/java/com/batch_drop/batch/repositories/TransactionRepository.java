package com.batch_drop.batch.repositories;

import com.batch_drop.batch.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    @Query(value = "select t.transaction_reference, t.amount, t.from_acc_id, t.to_acc_id,t.currency, t.transaction_date, t.transaction_id From transaction_t t",nativeQuery = true)
    Page<Transaction> findCustom(Pageable pageable);
}
