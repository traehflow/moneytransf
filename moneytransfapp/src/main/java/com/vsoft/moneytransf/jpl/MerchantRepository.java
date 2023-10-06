package com.vsoft.moneytransf.jpl;

import com.vsoft.moneytransf.jpl.entity.Merchant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public List<String> list() {
        var result = entityManager.createQuery("SELECT name from Merchant");
        return result.getResultList();
    }
}


