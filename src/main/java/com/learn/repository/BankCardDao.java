package com.learn.repository;

import com.learn.domain.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Yi on 2015/5/28.
 */
@Repository
public interface BankCardDao extends JpaRepository<BankCard, Long> {
    @Query("select u from BankCard u where u.cardNumber = ?1")
    BankCard findByCardNumber(String cardNumber);



}
