package com.vsoft.moneytransf.jpl;

import com.vsoft.moneytransf.MerchantNotFoundException;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class MerchantRepository {


    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Merchant save(Merchant merchant) {
        entityManager.persist(merchant);
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
    public List<String> list() {
        var result = entityManager.createQuery("SELECT name from Merchant");
        return result.getResultList();
    }
}


