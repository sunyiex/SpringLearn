package com.learn.repository;


import com.learn.domain.LoginUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by Yi on 2015/5/24.
 */
@Repository
public interface LoginUserDao extends JpaRepository<LoginUser, Long> {
    /**
     * @param IDCard
     * @return
     */
    @Query(value = "select u from LoginUser u where u.IDCard = ?1")
    LoginUser findByIDCard(String IDCard);
}
