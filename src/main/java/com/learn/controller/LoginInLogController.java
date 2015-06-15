//package com.learn.controller;
//
//import com.learn.domain.LoginInLog;
//import com.learn.repository.LoginInlogDao;
//import com.learn.service.LoginInLogService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.http.HttpSession;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by Yi on 2015/6/13.
// */
//@Controller
//@RequestMapping(value = "/user/log")
//public class LoginInLogController {
//
//    @Autowired
//    private LoginInLogService loginInLogService;
//
//
//    private static final String PAGE_SIZE = "3";
//
//    private static Map<String, String> sortTypes = new HashMap<String, String>();
//    static {
//        sortTypes.put("id", "自动");
//        sortTypes.put("time", "时间");
//    }
//    @RequestMapping(value = "index{page}" ,method = RequestMethod.GET)
//    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
//                       @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
//                       @RequestParam(value = "sortType", defaultValue = "auto") String sortType, ModelAndView modelAndView,
//                       ServletRequest request, HttpSession session) {
//        Long userId = (Long) session.getAttribute("userId");
//
//        Page<LoginInLog> log = loginInLogService.getUserLoginInlog(userId, pageNumber, pageSize, sortType);
//
//        modelAndView.addObject("log", log);
//        modelAndView.addObject("sortType", sortType);
//        modelAndView.addObject("sortTypes", sortTypes);
//        // 将搜索条件编码成字符串，用于排序，分页的URL
//        modelAndView.setViewName("/user/log");
//        return  modelAndView;
//    }
//    @RequestMapping(method = RequestMethod.GET)
//    public ModelAndView a(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
//                             @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
//                             @RequestParam(value = "sortType", defaultValue = "auto") String sortType, ModelAndView modelAndView,
//                             ServletRequest request, HttpSession session) {
//        Long userId = (Long) session.getAttribute("userId");
//        Sort sort = new Sort(Sort.Direction.DESC, "id");
//        Page<LoginInLog> log = loginInlogDao.findAll(new PageRequest(pageNumber - 1, 3, sort));
//        modelAndView.addObject("log", log);
//        modelAndView.addObject("sortType", sortType);
//        modelAndView.addObject("sortTypes", sortTypes);
//        // 将搜索条件编码成字符串，用于排序，分页的URL
//        modelAndView.setViewName("/user/log");
//        return  modelAndView;
//    }
//}
