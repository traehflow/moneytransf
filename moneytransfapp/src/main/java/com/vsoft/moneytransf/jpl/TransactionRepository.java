package com.vsoft.moneytransf.jpl;

import com.vsoft.moneytransf.jpl.entity.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class TransactionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Transaction save(Transaction merchant) {
        entityManager.persist(merchant);
        return merchant;
    }

    @Transactional
    public List<String> list() {
        var result = entityManager.createQuery("SELECT amount from Transaction");
        return result.getResultList();
    }

    @Transactional
    public void deleteByTimestampBefore(long timestamp) {
        entityManager.createQuery("DELETE FROM Transaction t WHERE t.timestamp < :timestamp")
                .setParameter("timestamp", timestamp)
                .executeUpdate();
    }

    @Transactional
    public Transaction fetch(UUID transactionId) {
        return entityManager.find(Transaction.class, transactionId);
    }
}
