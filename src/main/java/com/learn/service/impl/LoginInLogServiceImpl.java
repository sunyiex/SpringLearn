//package com.learn.service.impl;
//
//import com.learn.domain.LoginInLog;
//import com.learn.repository.LoginInlogDao;
//import com.learn.service.LoginInLogService;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.Resource;
//
///**
// * Created by Yi on 2015/6/13.
// */
//// Spring Bean的标识.
//@Service
//// 类中所有public函数都纳入事务管理的标识.
//@Transactional
//public class LoginInLogServiceImpl implements LoginInLogService {
//
//    @Resource
//    private LoginInlogDao loginInlogDao;
//
//    public Iterable<LoginInLog> findAll() {
//        return loginInlogDao.findAll();
//    }
//
//    public String  save(LoginInLog loginInLog) {
//        loginInlogDao.save(loginInLog);
//        return "success";
//    }
//
//    public Page<LoginInLog> getUserLoginInlog(Long userId, int pageNumber, int pageSize, String sortType){
//            PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
//            return loginInlogDao.findByUserId(userId, pageRequest);
//    }
//    /**
//     * 创建分页请求.
//     */
//    private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
//        Sort sort = null;
//        if ("auto".equals(sortType)) {
//            sort = new Sort(Sort.Direction.DESC, "id");
//        } else if ("title".equals(sortType)) {
//            sort = new Sort(Sort.Direction.ASC, "time");
//        }
//        return new PageRequest(pageNumber - 1, pagzSize, sort);
//    }
//
//}
