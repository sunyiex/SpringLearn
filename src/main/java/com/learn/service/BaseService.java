package com.learn.service;

import com.learn.domain.BaseObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


/**
 * Created by Yi on 2015/5/24.
 */
public interface BaseService<T extends BaseObject, R extends JpaRepository> {

    public String save(T entriy);

}
