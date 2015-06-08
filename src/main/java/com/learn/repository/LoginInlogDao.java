package com.learn.repository;

import com.learn.domain.LoginInLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Yi on 2015/5/29.
 */
@Repository
public interface LoginInlogDao extends JpaRepository<LoginInLog, Long> {

}
