package com.vsoft.moneytransf.jpl;

import com.vsoft.moneytransf.dto.TransactionDTO;
import com.vsoft.moneytransf.jpl.entity.ChargeTransaction;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import com.vsoft.moneytransf.jpl.entity.Transaction;
import com.vsoft.moneytransf.jpl.entity.TransactionDescriminator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public class TransactionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Transaction save(Transaction transaction) {
        entityManager.persist(transaction);
        return transaction;
    }

    @Transactional
    public List<TransactionDTO> list(TransactionDescriminator descriminator, Merchant merchant) {
        Query query;
        String select;
        if (descriminator == null) {
            //I didn't tested it
            query = entityManager.createQuery("SELECT t from Transaction t where t.merchant = :merchant");
            query.setParameter("merchant", merchant);
        } else {
            select = "SELECT new com.vsoft.moneytransf.dto.TransactionDTO(t.timestamp, t.id, t.status, t.customerEmail, " + switch (descriminator) {
                case AUTHORIZE -> "t.amount) from AuthorizeTransaction t ";
                case REVERSAL -> "t.referencedTransaction.id) from ReversalTransaction t ";
                case CHARGE ->  "t.amount) from ChargeTransaction t ";
                case REFUND -> "t.referencedTransaction.id, t.amount) from RefundTransaction t ";
            } + " WHERE t.merchant = :merchant";
            query = entityManager.createQuery(select);
            query.setParameter("merchant", merchant);
        }
        return query.getResultList();
    }

    @Transactional
    public void deleteByTimestampBefore(long timestamp) {
        entityManager.createQuery("DELETE FROM Transaction t WHERE t.timestamp < :timestamp")
                .setParameter("timestamp", timestamp)
                .executeUpdate();
    }

    @Transactional
    public void updateRefundedChargeTransaction(ChargeTransaction transaction, BigDecimal refundedValue) {
        Query query = entityManager.createQuery(
                "UPDATE ChargeTransaction " +
                        "SET refundedAmount = refundedAmount + :refundedAmount, " +
                        "status = REFUNDED " +
                        "WHERE id = :transactionId"
        );
        query.setParameter("refundedAmount", refundedValue);
        query.setParameter("transactionId", transaction.getId());
        query.executeUpdate();
    }

    @Transactional
    public Transaction fetch(UUID transactionId) {
        return entityManager.find(Transaction.class, transactionId);
    }
}
