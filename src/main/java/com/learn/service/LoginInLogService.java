package com.learn.service;

import com.learn.domain.LoginInLog;
import com.learn.repository.LoginInlogDao;
import org.springframework.data.domain.Page;

import java.util.List;


/**
 * Created by Yi on 2015/6/13.
 */
public interface LoginInLogService{
    public Iterable<LoginInLog> findAll();
    public String save(LoginInLog loginInLog);
    public Page<LoginInLog> getUserLoginInlog(Long userId, int pageNumber, int pageSize,
                                              String sortType);
}
