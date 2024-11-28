package com.dualsoft.banking.transaction.repository;

import com.dualsoft.banking.transaction.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query(value = "SELECT pg_advisory_xact_lock(?1)", nativeQuery = true)
    void lockTransaction(long lockKey);
    List<Transaction> findAllByAccountId(UUID accountId);
    Page<Transaction> findAllByAccountId(UUID accountId, Pageable page);
    @Query(value = "select * from transaction.transaction where t_account_id = ?1 order by t_created_at desc limit 1", nativeQuery = true)
    Optional<Transaction> getLastTransaction(UUID accountId);
    List<Transaction> findAllByCorrelationId(UUID correlationId);
}
