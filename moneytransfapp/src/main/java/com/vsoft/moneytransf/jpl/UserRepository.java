package com.vsoft.moneytransf.jpl;

import com.vsoft.moneytransf.jpl.entity.Transaction;
import com.vsoft.moneytransf.jpl.entity.UserData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {


    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public UserData loadUser(String username) {
        TypedQuery<UserData> query = entityManager.createQuery(
                "SELECT e FROM UserData e WHERE e.username = :username", UserData.class
        );

        query.setParameter("username", username);

        var result = query.getResultList();
        if(result.isEmpty()) {
            return null;
        } else {
            return query.getResultList().get(0);
        }
    }

    @Transactional
    public UserData save(UserData userData) {
        entityManager.persist(userData);
        return userData;
    }

}
