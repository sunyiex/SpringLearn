package com.learn.controller;

import com.learn.common.utils.IP;
import com.learn.common.utils.MD5Util;
import com.learn.common.utils.SessionAndIdCard;
import com.learn.domain.BankCard;
import com.learn.domain.LoginInLog;
import com.learn.domain.LoginUser;
import com.learn.service.BankCardService;
import com.learn.service.LoginUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * Created by Yi on 2015/6/6.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private LoginUserService loginUserService;
    @Autowired
    private BankCardService bankCardService;

    private static final String PAGE_SIZE = "3";

    @RequestMapping("/index.do")
    public ModelAndView index(HttpServletRequest request, HttpSession session, ModelAndView modelAndView){
        modelAndView.setViewName("admin/index");
        return modelAndView;
    }
    @RequestMapping("/reslut.do")
    public ModelAndView reslut(HttpServletRequest request, HttpSession session, ModelAndView modelAndView){

        modelAndView.setViewName("admin/result");
        return modelAndView;
    }

    @RequestMapping("/login.do")
    public ModelAndView login(HttpServletRequest request, HttpSession session, ModelAndView modelAndView){
        String msg = "success";
        String kaptchaCode = (String) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        String captcha = request.getParameter("kaptcha");
        if (!captcha.equals(kaptchaCode)) {
            msg = "验证码错误";
            modelAndView.addObject("message", msg);
            modelAndView.setViewName("redirect:/admin/result.do");
            return modelAndView;
        }

        String username = request.getParameter("IDCard");
        String password = request.getParameter("password");
        password = MD5Util.MD5(password);


        LoginUser loginUser = loginUserService.findByIDCard(username);

        if (loginUser != null) {
            if (password.equals(loginUser.getPassword())) {
                //登陆成功
                session.setAttribute("userIDCard", loginUser.getIDCard());
                session.setAttribute("userId", loginUser.getId());

                modelAndView.addObject("user", loginUser);
                modelAndView.addObject("message", msg);

                if (loginUser.getType().equals("admin")) {
                    //管理员
                    LoginInLog loginInLog = new LoginInLog();
                    String ip = IP.getIpAddr(request);
//                    String ipAddress = IP.getAddressByIP(ip);
                    loginInLog.setIp(ip);
//                    loginInLog.setAddress(ipAddress);
                    loginInLog.setLoginUser(loginUser);
                    loginInLog.setTime(new Date());
                    if (loginUser.getLoginIglooList().size() >= 10) {
                        loginUserService.deleteFristLoginInLog(loginUser);
                    }
                    msg = loginUserService.addLoginInlog(loginUser, loginInLog);
                    if (!msg.equals("success")) {
                        modelAndView.addObject("message", msg);
                        modelAndView.setViewName("/admin/result");
                    } else {
                        SessionAndIdCard.addUser(String.valueOf(loginUser.getId()), session.getId());
                        modelAndView.addObject("message", msg);
                        modelAndView.setViewName("redirect:/admin/adminCenter.do");
                    }
                } else {
                    //普通用户
                    modelAndView.addObject("message", msg);
                    modelAndView.setViewName("redirect:/user/index");
                }
            } else {
                msg = "密码错误";
                modelAndView.addObject("message", msg);
                modelAndView.setViewName("/admin/result");
            }
        } else {
            msg = "该会员不存在";
            modelAndView.addObject("message", msg);
            modelAndView.setViewName("/admin/result");
        }
        return modelAndView;
    }

    /**
     *
     * @param modelAndView
     * @param session
     * @return
     */
    @RequestMapping(value = "/adminCenter.do", method = RequestMethod.GET)
    public ModelAndView userCenter(ModelAndView modelAndView,@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                   @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, HttpSession session) {

        Page<LoginUser> loginUserPage = loginUserService.findAll(new PageRequest(pageNumber-1, pageSize));

        modelAndView.addObject("page", loginUserPage);
        modelAndView.setViewName("/admin/adminCenter");
        return modelAndView;
    }
    @RequestMapping(value = "/bankCard.do", method = RequestMethod.GET)
    public ModelAndView bankCard(ModelAndView modelAndView,@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                   @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, HttpSession session) {

        Page<BankCard> bankCardPage = bankCardService.findAll(new PageRequest(pageNumber-1, pageSize));

        modelAndView.addObject("page", bankCardPage);
        modelAndView.setViewName("/admin/bankCard");
        return modelAndView;
    }
    @RequestMapping(value = "/userLog.do", method = RequestMethod.GET)
    public ModelAndView userLog(ModelAndView modelAndView, HttpSession session) {
        List<LoginUser> loginUserList = loginUserService.findAll();
        modelAndView.addObject("userList", loginUserList);
        modelAndView.setViewName("/admin/userLog");
        return modelAndView;
    }
    @RequestMapping(value = "/bankLog.do", method = RequestMethod.GET)
    public ModelAndView bankLog(ModelAndView modelAndView, HttpSession session) {
        List<BankCard> bankList = bankCardService.findAll();
        modelAndView.addObject("bankList", bankList);
        modelAndView.setViewName("/admin/bankLog");
        return modelAndView;
    }

    @RequestMapping(value = "/getUserlog.do", method = RequestMethod.GET)
    public ModelAndView getUserlog(HttpServletRequest request, ModelAndView modelAndView, HttpSession session) {
        String loginUserIDCard = request.getParameter("IDCard");
        modelAndView.setViewName("/common/log");
        LoginUser loginUser = loginUserService.findByIDCard(loginUserIDCard);
        if(loginUser == null){
            modelAndView.addObject("msg", "错误的用户身份证");
            return modelAndView;
        }
        modelAndView.addObject("loginInLogList", loginUser.getLoginIglooList());
        return modelAndView;
    }


    @RequestMapping(value = "/getCardLog.do", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
//    @ResponseBody
    public ModelAndView getCardLog(HttpServletRequest request, ModelAndView modelAndView, HttpSession session) {
        String cardNumber = request.getParameter("cardNumber");
        BankCard bankCard = bankCardService.findByCardNumber(cardNumber);
        if (bankCard.getOperationList() == null) {
            modelAndView.addObject("message", "错误的银行卡号");
            modelAndView.setViewName("/user/CardLog");
            return modelAndView;
        }
        modelAndView.addObject("message", "success");
        modelAndView.addObject("cardLogList", bankCard.getOperationList());
        modelAndView.addObject("cardNumber", bankCard.getCardNumber());

        modelAndView.setViewName("/user/CardLog");
        return modelAndView;
    }
}
