package com.vsoft.moneytransf.jpl;

import com.vsoft.moneytransf.exception.MerchantNotFoundException;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public class MerchantRepository {


    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Merchant save(Merchant merchant) {
        entityManager.persist(merchant);
        Query query = entityManager.createQuery(
                "UPDATE Merchant " +
                        "SET totalTransactionSum = 0 " +
                        "WHERE id = :merchant"
        );
        query.setParameter("merchant", merchant.getId());
        query.executeUpdate();
        return merchant;
    }

    @Transactional
    public Merchant getById(UUID id) {
        return entityManager.find(Merchant.class, id);
    }

    @Transactional
    public Merchant getByEmail(String email) throws MerchantNotFoundException {
        TypedQuery<Merchant> query = entityManager.createQuery(
                "SELECT e FROM Merchant e WHERE e.email = :email", Merchant.class
        );
        query.setParameter("email", email);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new MerchantNotFoundException(e);
        }
    }

    @Transactional
    public void updateMerchantTupdateMerchantTotalSumByotalSumBy(Merchant merchant, BigDecimal value) {
        Query query = entityManager.createQuery(
                "UPDATE Merchant " +
                        "SET totalTransactionSum = totalTransactionSum + :incrementValue " +
                        "WHERE id = :merchant"
        );
        query.setParameter("incrementValue", value);
        query.setParameter("merchant", merchant.getId());
        query.executeUpdate();
    }

    @Transactional
    public List<String> list() {
        var result = entityManager.createQuery("SELECT name from Merchant");
        return result.getResultList();
    }

/*    @Transactional
    public Merchant update(Merchant merchant) {
        return sessionFactory.openSession().merge(merchant);
    }*/
}


