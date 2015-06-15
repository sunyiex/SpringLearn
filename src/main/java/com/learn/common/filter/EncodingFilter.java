//package com.learn.common.filter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.logging.Filter;
//
///**
// * Created by Yi on 2015/6/8.
// */
//public class EncodingFilter implements Filter {
//    public void doFilter(ServletRequest sreq, ServletResponse sres, FilterChain fc) throws IOException, ServletException {
//        final HttpServletRequest req = (HttpServletRequest) sreq;
//        final HttpServletResponse res = (HttpServletResponse) sres;
//
//        if (req.getMethod() == "GET") {
//            fc.doFilter(new ParametersWrapper(req), res);
//        }else {
//            fc.doFilter(wrapper, res);
//        }
//    }
//
//}