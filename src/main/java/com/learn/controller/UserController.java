package com.learn.controller;

import com.learn.common.utils.*;
import com.learn.domain.BankCardOperationLog;
import com.learn.domain.LoginInLog;
import com.learn.domain.LoginUser;
import com.learn.domain.BankCard;
import com.learn.service.BankCardService;
import com.learn.service.LoginUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Yi on 2015/5/23.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {
    protected static final long SENDEMAILTIMEOUT = 30000;
    protected static final long EMAILTIME = 86400000;//24小时

    @Autowired
    private LoginUserService loginUserService;

    @Autowired
    private BankCardService bankCardService;

    @RequestMapping(value = "/result.do", method = RequestMethod.GET)
    public ModelAndView result(ModelAndView modelAndView, HttpServletRequest request) throws UnsupportedEncodingException {
        String msg = request.getParameter("message");
        //解决GET传值的
        byte bb[];
        bb = msg.getBytes("ISO-8859-1"); //以"ISO-8859-1"方式解析name字符串
        msg = new String(bb, "UTF-8"); //再用"utf-8"格式表示name

        modelAndView.addObject("message", msg);
        modelAndView.setViewName("/user/result");
        return modelAndView;
    }

//    @RequestMapping(value = "/login.do", method = RequestMethod.GET)
//    public ModelAndView login(ModelAndView modelAndView) {
//        modelAndView.addObject("test", "sunmasd");
//        modelAndView.setViewName("/user/login");
//        return modelAndView;
//    }

    @RequestMapping(value = "/doLogin.do", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView loginIn(HttpServletRequest request, ModelAndView modelAndView, HttpSession session) {
        String msg = "success";
        String kaptchaCode = (String) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        String captcha = request.getParameter("kaptcha");
        if (!captcha.equals(kaptchaCode)) {
            msg = "验证码错误";
            modelAndView.addObject("message", msg);
            modelAndView.setViewName("redirect:/user/result.do");
            return modelAndView;
        }
        String username = request.getParameter("IDCard");
        String password = request.getParameter("password");
        password = MD5Util.MD5(password);
        LoginUser loginUser = loginUserService.findByIDCard(username);
        if (loginUser != null) {
            if (password.equals(loginUser.getPassword())) {
                //登陆成功
                session.setAttribute("userId", loginUser.getId());
                session.setAttribute("userIDCard", loginUser.getIDCard());
                session.setAttribute("user", loginUser);

                modelAndView.addObject("user", loginUser);
                modelAndView.addObject("message", msg);
                if (loginUser.getType() == null || loginUser.getType().equals("admin")) {
                    //管理员
                    modelAndView.addObject("message", msg);
                    modelAndView.setViewName("redirect:/admin/index.do");
                } else {
                    //普通用户
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
                        modelAndView.setViewName("redirect:/user/result.do");
                    } else {
                        SessionAndIdCard.addUser(String.valueOf(loginUser.getId()), session.getId());
                        modelAndView.addObject("message", msg);
                        modelAndView.setViewName("redirect:/user/userCenter.do");
                    }
                }
            } else {
                msg = "密码错误";
                modelAndView.addObject("message", msg);
                modelAndView.setViewName("redirect:/user/result.do");
            }
        } else {
            msg = "该会员不存在";
            modelAndView.addObject("message", msg);
            modelAndView.setViewName("redirect:/user/result.do");
        }
        return modelAndView;
    }

    /**
     * 进入注册页面
     *
     * @param modelAndView
     * @return
     */
    @RequestMapping(value = "/register.do", method = RequestMethod.GET)
    public ModelAndView register(ModelAndView modelAndView) {
        modelAndView.addObject("test", "sunmasd");
        modelAndView.setViewName("/user/register");
        return modelAndView;
    }

    /**
     * 注册会员
     * 2015年6月7日14:18:21
     *
     * @param request
     * @param modelAndView
     * @return
     * @throws UnsupportedEncodingException
     * @throws ParseException
     */
    @RequestMapping(value = "/doRegister.do", method = RequestMethod.POST)
    public ModelAndView onRegister(HttpServletRequest request, ModelAndView modelAndView, HttpSession session) throws UnsupportedEncodingException, ParseException {
        String kaptchaCode = (String) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        String captcha = request.getParameter("kaptcha");
        if (!captcha.equals(kaptchaCode)) {
            modelAndView.addObject("message", "success");
            modelAndView.setViewName("redirect:/user/result.do");
            return modelAndView;
        }
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String IDCard = request.getParameter("IDCard");
        String birthdaystr = request.getParameter("birthday");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String sex = request.getParameter("sex");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthday = sdf.parse(birthdaystr);
        LoginUser checkUser = new LoginUser();
        checkUser.setPassword(password);
        checkUser.setEmail(email);
        checkUser.setIDCard(IDCard);
        checkUser.setName(name);
        checkUser.setPhone(phone);
        checkUser.setSex(sex);
        //用户信息检查
        String checkMsg = loginUserService.checkUser(checkUser, birthdaystr);
        if (!checkMsg.equals("success")) {
            modelAndView.addObject("message", checkMsg + "错误");
            modelAndView.setViewName("redirect:/user/result.do");
            return modelAndView;
        }
        //判断用户是否注册
        LoginUser user = loginUserService.findByIDCard(IDCard);
        if (user != null) {
            modelAndView.addObject("message", "会员已经被注册过了");
            modelAndView.setViewName("redirect:/user/result.do");
            return modelAndView;
        }
        password = MD5Util.MD5(password);
        LoginUser loginUser = new LoginUser();
        loginUser.setPassword(password);
        loginUser.setEmail(email);
        loginUser.setIDCard(IDCard);
        loginUser.setName(name);
        loginUser.setPhone(phone);
        loginUser.setSex(sex);
        loginUser.setType("user");
        loginUser.setTime(new Date());
        loginUser.setBirthday(birthday);


        try {
            loginUserService.save(loginUser);
        } catch (Exception e) {
            modelAndView.addObject("message", e.getMessage());
            modelAndView.setViewName("redirect:/user/result.do");
            return modelAndView;
        }

        modelAndView.addObject("message", "success");
        modelAndView.setViewName("redirect:/user/result.do");
        return modelAndView;
    }

    @RequestMapping(value = "/IDCardInquiry.do", method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String IDCardInquiry(ModelAndView modelAndView, String IDCard) {
        LoginUser user = loginUserService.findByIDCard(IDCard);
        String msg;
        if (user != null) {
            msg = "error";
        } else {
            msg = "success";

        }
        return msg;
    }

    /**
     * 进入会员首页
     *
     * @param modelAndView
     * @return
     */
    @RequestMapping(value = "/index.do", method = RequestMethod.GET)
    public ModelAndView index(ModelAndView modelAndView) {
//        modelAndView.addObject("test", "sunmasd");
        modelAndView.setViewName("/user/index");
        return modelAndView;
    }

    /**
     * 进入会员中心页面
     * 2015年6月7日14:17:31
     *
     * @param modelAndView
     * @param session
     * @return
     */
    @RequestMapping(value = "/userCenter.do", method = RequestMethod.GET)
    public ModelAndView userCenter(ModelAndView modelAndView, HttpSession session) {
        long loginUserId = (Long) session.getAttribute("userId");
        String loginUserIDCard = (String) session.getAttribute("userIDCard");
        LoginUser loginUser = loginUserService.findByIDCard(loginUserIDCard);
        if (loginUser.getId() != loginUserId) {
            modelAndView.addObject("message", "用户信息错误");
            modelAndView.setViewName("/user/result");
        }
        List<BankCard> cardList = loginUser.getCardList();
        List<LoginInLog> loginInLogList = loginUser.getLoginIglooList();

        modelAndView.addObject("user", loginUser);
        modelAndView.addObject("cardList", cardList);
        modelAndView.addObject("loginInLogList", loginInLogList);

        modelAndView.setViewName("/user/userCenter");
        return modelAndView;
    }

    /**
     * 进入信息修改页面
     * 2015年6月7日14:17:11
     *
     * @param modelAndView
     * @param session
     * @return
     */
    @RequestMapping(value = "/changesInfo.do", method = RequestMethod.GET)
    public ModelAndView changesInfo(ModelAndView modelAndView, HttpSession session) {
        long loginUserId = (Long) session.getAttribute("userId");
        String loginUserIDCard = (String) session.getAttribute("userIDCard");
        LoginUser loginUser = loginUserService.findByIDCard(loginUserIDCard);
        if (loginUser.getId() != loginUserId) {
            modelAndView.addObject("message", "用户信息错误");
            modelAndView.setViewName("/user/result");
        }
        List<BankCard> cardList = loginUser.getCardList();
        modelAndView.addObject("user", loginUser);
        modelAndView.addObject("cardList", cardList);
        modelAndView.setViewName("/user/changesInfo");
        return modelAndView;
    }

    /**
     * 修改个人基本信息
     * 2015年6月7日14:16:53
     *
     * @param request
     * @param modelAndView
     * @param session
     * @return
     */
    @RequestMapping(value = "/doChangeUserInfo.do", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String doChangeUserInfo(HttpServletRequest request, ModelAndView modelAndView, HttpSession session) {
        String type = request.getParameter("type");
        String newVal = request.getParameter("newVal");
        long loginUserId = (Long) session.getAttribute("userId");
        String loginUserIDCard = (String) session.getAttribute("userIDCard");
        LoginUser loginUser = loginUserService.findByIDCard(loginUserIDCard);
        if (loginUser.getId() != loginUserId) {
            return "用户信息错误";
        }
        String message;
        if (type.equals("phone")) {
            if (!newVal.equals(loginUser.getPhone())) {
                loginUser.setPhone(newVal);
            } else {
                return "没有做任何修改。";
            }
        } else if (type.equals("email")) {
            if (!newVal.equals(loginUser.getEmail())) {
                loginUser.setEmail(newVal);
            } else {
                return "没有做任何修改。";
            }
        } else {
            return "操作类型错误！";
        }
        message = loginUserService.save(loginUser);
        if (message.equals("success")) {
            session.setAttribute("user", loginUser);
        }
        return message;
    }

    /**
     * 增加卡号
     * 2015年6月7日14:16:31
     *
     * @param request
     * @param modelAndView
     * @param session
     * @return
     */
    @RequestMapping(value = "/addBankCard.do", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String addBankCard(HttpServletRequest request, ModelAndView modelAndView, HttpSession session) {
        String message;
        long loginUserId = (Long) session.getAttribute("userId");
        String loginUserIDCard = (String) session.getAttribute("userIDCard");
        LoginUser loginUser = loginUserService.findByIDCard(loginUserIDCard);
        if (loginUser.getId() != loginUserId) {
            return "用户信息错误";
        }
        if (loginUser.getCardList().size() >= 10) {
            return "每个账户最多绑定10个银行卡号";
        }
        BankCard lastCard = bankCardService.findLast();
        BankCard newCard = new BankCard();
        String num = lastCard.getCardNumber();
        num = CardUtil.getNextCardNumber(num);
        newCard.setCardNumber(num);
        newCard.setTime(new Date());
        newCard.setBalance(0);
        newCard.setActiveFlag("normal");
        newCard.setLoginUser(loginUser);
//        newCard.setId(lastCard.getId()+1);
//        newCard.setLoginUser(loginUser);
        message = loginUserService.addBankCard(loginUser, newCard);
        if (message.equals("success")) {
            session.setAttribute("user", loginUser);
        }
        return message;
    }

    /**
     * 挂失卡号
     * 2015年6月7日14:16:22
     *
     * @param request
     * @param modelAndView
     * @param session
     * @return
     */
    @RequestMapping(value = "/doLossCard.do", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String doLossCard(HttpServletRequest request, ModelAndView modelAndView, HttpSession session) {
        String message = "error";
        long loginUserId = (Long) session.getAttribute("userId");
        String loginUserIDCard = (String) session.getAttribute("userIDCard");
        LoginUser loginUser = loginUserService.findByIDCard(loginUserIDCard);
        if (loginUser.getId() != loginUserId) {
            return "用户信息错误";
        }
            String cardNumber = request.getParameter("cardNumber");
            BankCard lossCard = bankCardService.findByCardNumber(cardNumber);
        for (int i = 0; i < loginUser.getCardList().size(); i++) {
            if (loginUser.getCardList().get(i).getCardNumber().equals(cardNumber)) {
                message = "success";
                break;
            }
        }
        if (!message.equals("success")) {
            return "该会员没有此卡号： " + cardNumber;
        }
        if(!lossCard.getActiveFlag().equals("normal")){
            return "该卡号目前状态无法执行挂失操作。";
        }
        lossCard.setActiveFlag("reportLoss");
        BankCardOperationLog bankCardOperationLog = new BankCardOperationLog();
        bankCardOperationLog.setTime(new Date());
        bankCardOperationLog.setOperationMoney(0);
        bankCardOperationLog.setOperationType("reportLoss");
        bankCardOperationLog.setBankCard(lossCard);
        message = bankCardService.addOperationLog(lossCard, bankCardOperationLog);
        if (message.equals("success")) {
            session.setAttribute("user", loginUser);
        }
        return message;
    }
    @RequestMapping(value = "/doFindCard.do", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String doFindCard(HttpServletRequest request, ModelAndView modelAndView, HttpSession session) {
        String message = "error";
        long loginUserId = (Long) session.getAttribute("userId");
        String loginUserIDCard = (String) session.getAttribute("userIDCard");
        LoginUser loginUser = loginUserService.findByIDCard(loginUserIDCard);
        if (loginUser.getId() != loginUserId) {
            return "用户信息错误";
        }
        String cardNumber = request.getParameter("cardNumber");
        BankCard lossCard = bankCardService.findByCardNumber(cardNumber);
        for (int i = 0; i < loginUser.getCardList().size(); i++) {
            if (loginUser.getCardList().get(i).getCardNumber().equals(cardNumber)) {
                message = "success";
                break;
            }
        }
        if (!message.equals("success")) {
            return "该会员没有此卡号： " + cardNumber;
        }
        if(!lossCard.getActiveFlag().equals("loss")){
            return "该卡号目前状态无法执行找回操作。";
        }
        lossCard.setActiveFlag("find");

        BankCardOperationLog bankCardOperationLog = new BankCardOperationLog();
        bankCardOperationLog.setTime(new Date());
        bankCardOperationLog.setOperationMoney(0);
        bankCardOperationLog.setOperationType("find");
        bankCardOperationLog.setBankCard(lossCard);
        message = bankCardService.addOperationLog(lossCard, bankCardOperationLog);

        if (message.equals("success")) {
            session.setAttribute("user", loginUser);
        }
        return message;
    }
    /**
     * 删除卡号
     * 2015年6月7日14:16:11
     *
     * @param request
     * @param modelAndView
     * @param session
     * @return
     */
    @RequestMapping(value = "/deleteBankCard.do", method = RequestMethod.GET,produces = "application/json;charset=utf-8" )
    @ResponseBody
    public String deleteBankCard(HttpServletRequest request, ModelAndView modelAndView, HttpSession session) {
        String message = "error";
        long loginUserId = (Long) session.getAttribute("userId");
        String loginUserIDCard = (String) session.getAttribute("userIDCard");
        LoginUser loginUser = loginUserService.findByIDCard(loginUserIDCard);
        if (loginUser.getId() != loginUserId) {
            return "用户信息错误";
        }
        String cardNumber = request.getParameter("cardNumber");
        String password = request.getParameter("password");
        if (password == null || !MD5Util.MD5(password).equals(loginUser.getPassword())) {
            return "密码错误";
        }
        BankCard deleteCard = null;
        for (int i = 0; i < loginUser.getCardList().size(); i++) {
            if (loginUser.getCardList().get(i).getCardNumber().equals(cardNumber)) {
                deleteCard = loginUser.getCardList().get(i);
                message = "success";
                break;
            }
        }
        if (!message.equals("success")) {
            return "该会员没有此卡号： " + cardNumber;
        }
//        newCard.setLoginUser(loginUser);
        message = loginUserService.deleteBankCard(loginUser, deleteCard);
        if (message.equals("success")) {
            session.setAttribute("user", loginUser);
        }
        return message;
    }

    /**
     * 进入操作页面
     * 2015年6月7日14:15:55
     *
     * @param modelAndView
     * @param session
     * @return
     */
    @RequestMapping(value = "/operation.do", method = RequestMethod.GET)
    public ModelAndView operation(ModelAndView modelAndView, HttpSession session) {
        long loginUserId = (Long) session.getAttribute("userId");
        String loginUserIDCard = (String) session.getAttribute("userIDCard");
        LoginUser loginUser = loginUserService.findByIDCard(loginUserIDCard);
        if (loginUser.getId() != loginUserId) {
            modelAndView.addObject("message", "用户信息错误");
            modelAndView.setViewName("/user/result");
        }
        modelAndView.addObject("user", loginUser);
        modelAndView.addObject("cardList", loginUser.getCardList());
        modelAndView.setViewName("/user/operation");
        return modelAndView;
    }

    /**
     * 存取款
     *
     * @param request
     * @param modelAndView
     * @param session
     * @return
     */
    @RequestMapping(value = "/depositOrDeaw.do", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String depositOrDeaw(HttpServletRequest request, ModelAndView modelAndView, HttpSession session) {
        long loginUserId = (Long) session.getAttribute("userId");
        String loginUserIDCard = (String) session.getAttribute("userIDCard");
        LoginUser loginUser = loginUserService.findByIDCard(loginUserIDCard);
        if (loginUser.getId() != loginUserId) {
            modelAndView.addObject("message", "用户信息错误");
            modelAndView.setViewName("/user/result");
        }
        String cardNumber = request.getParameter("cardNumber");
        String operationType = request.getParameter("operationType");
        float money = Float.valueOf(request.getParameter("money"));
        if (money >= 10000000) {
            return "单次操作金额过大,请不要超过一千万";
        }
        BankCard bankCard = bankCardService.findByCardNumber(cardNumber);
        if (bankCard == null || loginUser.getCardList().indexOf(bankCard) == -1) {
            return "错误的银行卡号";
        }
        String msg = bankCardService.depositOrDraw(bankCard, operationType, money);
        return msg;
    }

    /**
     * 转账
     *
     * @param request
     * @param modelAndView
     * @param session
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/transfer.do", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String transfer(HttpServletRequest request, ModelAndView modelAndView, HttpSession session) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        long loginUserId = (Long) session.getAttribute("userId");
        String loginUserIDCard = (String) session.getAttribute("userIDCard");
        LoginUser loginUser = loginUserService.findByIDCard(loginUserIDCard);
        if (loginUser.getId() != loginUserId) {
            modelAndView.addObject("message", "用户信息错误");
            modelAndView.setViewName("/user/result");
        }
        String cardNumber = request.getParameter("cardNumber");
        String dstCardNumber = request.getParameter("dstCardNumber");
        String name = request.getParameter("name");
        byte bb[];
        bb = name.getBytes("ISO-8859-1"); //以"ISO-8859-1"方式解析name字符串
        name = new String(bb, "UTF-8"); //再用"utf-8"格式表示name
        float money = Float.valueOf(request.getParameter("money"));
        if (money >= 10000000) {
            return "单次操作金额过大,请不要超过一千万";
        }
        BankCard bankCard = bankCardService.findByCardNumber(cardNumber);
        BankCard dstBankCard = bankCardService.findByCardNumber(dstCardNumber);
        if (bankCard.getOperationList() == null || loginUser.getCardList().indexOf(bankCard) == -1) {
            return "错误的银行卡号";
        } else if (dstBankCard == null) {
            return "错误的转入银行卡号";
        } else if (!dstBankCard.getLoginUser().getName().equals(name)) {
            return "存入卡号持有者姓名不正确";
        }
        String msg = bankCardService.transfer(bankCard, dstBankCard, money);
        return msg;
    }


    @RequestMapping(value = "/getCardLog.do", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
//    @ResponseBody
    public ModelAndView getCardLog(HttpServletRequest request, ModelAndView modelAndView, HttpSession session) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        long loginUserId = (Long) session.getAttribute("userId");
        String loginUserIDCard = (String) session.getAttribute("userIDCard");
        LoginUser loginUser = loginUserService.findByIDCard(loginUserIDCard);
        if (loginUser.getId() != loginUserId) {
            modelAndView.addObject("message", "用户信息错误");
            modelAndView.setViewName("/user/result");
        }
        String cardNumber = request.getParameter("cardNumber");
        System.out.printf("%s",cardNumber);
        BankCard bankCard = bankCardService.findByCardNumber(cardNumber);
        if (bankCard.getOperationList() == null || loginUser.getCardList().indexOf(bankCard) == -1) {
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

    /**
     * 登出
     *
     * @param modelAndView
     * @param session
     * @return
     */
    @RequestMapping(value = "/logout.do", method = RequestMethod.GET)
    public ModelAndView logout(ModelAndView modelAndView, HttpSession session) {
        String userId = String.valueOf((Long) session.getAttribute("userId"));
        SessionAndIdCard.removeUser(userId, session.getId());
        session.removeAttribute("userId");
        session.removeAttribute("userIDCard");
        modelAndView.setViewName("redirect:/user/index.do");
        return modelAndView;
    }

    /**
     * 忘记密码页面跳转2015年6月17日19:27:03
     *
     * @param request
     * @param modelAndView
     * @param session
     * @return
     */
    @RequestMapping(value = "/forgetPassword.do", method = RequestMethod.GET)
    public ModelAndView forget(HttpServletRequest request, ModelAndView modelAndView, HttpSession session) {
//        String userId = String.valueOf((Long) session.getAttribute("userId"));
//        SessionAndIdCard.removeUser(userId, session.getId());

        modelAndView.setViewName("/user/forgetPassword");
        return modelAndView;
    }

    /**
     * 邮箱发送
     *
     * @param modelAndView
     * @param session
     * @return
     */
    @RequestMapping(value = "/sendEmail.do", method = RequestMethod.POST)
    public ModelAndView sendEmail(HttpServletRequest request, ModelAndView modelAndView, HttpSession session) {
//        String userId = String.valueOf((Long) session.getAttribute("userId"));
//        SessionAndIdCard.removeUser(userId, session.getId());
        String kaptchaCode = (String) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        String captcha = request.getParameter("kaptcha");
        if (!captcha.equals(kaptchaCode)) {
            modelAndView.addObject("message", "验证码错误");
            modelAndView.setViewName("/user/result");
            return modelAndView;
        }
        String IDCard = request.getParameter("IDCard");
        String email = request.getParameter("email");

        LoginUser loginUser = loginUserService.findByIDCard(IDCard);
        if (loginUser == null) {
            modelAndView.addObject("message", "用户名不存在");
            modelAndView.setViewName("/user/result");
            return modelAndView;
        } else if (!loginUser.getEmail().equals(email)) {
            modelAndView.addObject("message", "邮箱错误");
            modelAndView.setViewName("/user/result");
            return modelAndView;
        }
        if (session.getAttribute("emailStartTime") == null) {
            session.setAttribute("emailStartTime", System.currentTimeMillis());
        } else {
            if (!checkEmailTimeOut(System.currentTimeMillis(), (Long) session.getAttribute("emailStartTime"))) {
                modelAndView.addObject("message", "邮件已经发送过,注意查收！(30秒内不能连续发送)");
                modelAndView.setViewName("/user/result");
                return modelAndView;
            } else {
                session.setAttribute("emailStartTime", System.currentTimeMillis());
            }
        }
        String URL = request.getRequestURL().toString().replaceAll(request.getRequestURI().toString(), "");
        String msg = loginUserService.sendEmail(email, IDCard, loginUser.getId(), URL);
        if (msg.equals("success")) {
            modelAndView.addObject("message", "发送成功");
        } else {
            modelAndView.addObject("message", "发送失败，请稍后在尝试。");
        }
        modelAndView.setViewName("/user/result");
        return modelAndView;
    }

    /**
     * 进入新密码设置页面2015年6月17日19:26:43
     *
     * @param request
     * @param modelAndView
     * @param session
     * @return
     */
    @RequestMapping(value = "/setNewPassword.do", method = RequestMethod.GET)
    public ModelAndView setNewPassword(HttpServletRequest request, ModelAndView modelAndView, HttpSession session) {
//        String userId = String.valueOf((Long) session.getAttribute("userId"));
//        SessionAndIdCard.removeUser(userId, session.getId());
        String time = request.getParameter("time");
        String id = request.getParameter("id");
        String IDCard = request.getParameter("IDCard");
        modelAndView.setViewName("/user/result");
        if (id == null || time == null || IDCard == null) {
            modelAndView.addObject("message", "请不要篡改网址！");
            return modelAndView;
        }
        id = DigestUtil.Decrypt(id);
        time = DigestUtil.Decrypt(time);
        IDCard = DigestUtil.Decrypt(IDCard);
        LoginUser loginUser = loginUserService.findByIDCard(IDCard);
        if (!String.valueOf(loginUser.getId()).equals(id)) {
            modelAndView.addObject("message", "数据出错，可能网址有问题！");
            return modelAndView;
        }
        long now = System.currentTimeMillis();
        long send = Long.parseLong(time);
        if (now - send > EMAILTIME) {
            modelAndView.addObject("message", "这个网址过时了，请重新发送邮件获得网址。");
            return modelAndView;
        }
        session.setAttribute("newPasswordIDCard", IDCard);
        modelAndView.setViewName("/user/setNewPassword");
        return modelAndView;
    }

    /**
     * 设置新密码操作
     *
     * @param request
     * @param modelAndView
     * @param session
     * @return
     */
    @RequestMapping(value = "/doSetNewPassword.do", method = RequestMethod.POST)
    public ModelAndView doSetNewPassword(HttpServletRequest request, ModelAndView modelAndView, HttpSession session) {
//        String userId = String.valueOf((Long) session.getAttribute("userId"));
//        SessionAndIdCard.removeUser(userId, session.getId());
        String kaptchaCode = (String) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        String captcha = request.getParameter("kaptcha");
        modelAndView.setViewName("/user/result");
        if (!captcha.equals(kaptchaCode)) {
            modelAndView.addObject("message", "验证码错误");
            return modelAndView;
        }
        String IDCard = (String) session.getAttribute("newPasswordIDCard");
        LoginUser loginUser = loginUserService.findByIDCard(IDCard);
        if (loginUser == null) {
            session.removeAttribute("newPasswordIDCard");
            modelAndView.addObject("message", "数据出错，请重新发送邮箱设置密码");
            return modelAndView;
        }
        String psd = request.getParameter("password");
        String psd2 = request.getParameter("password2");
        if(psd == null){
            modelAndView.addObject("message", "密码为空");
            return modelAndView;
        }
        if(psd2 == null || !psd.equals(psd2)){
            modelAndView.addObject("message", "两次密码不同");
            return modelAndView;
        }

        loginUser.setPassword(psd);
        String msg  = loginUserService.checkUser(loginUser);
        loginUser.setPassword(MD5Util.MD5(psd));
        if(!msg.equals("success")){
            modelAndView.addObject("message",msg + "格式不对");
            return modelAndView;

        }
        msg = loginUserService.save(loginUser);
        modelAndView.addObject("message",msg);
        session.removeAttribute("newPasswordIDCard");
        return modelAndView;
    }


    /**
     * 发送邮件时间检查
     *
     * @param now
     * @param end
     * @return
     */
    private boolean checkEmailTimeOut(long now, long end) {
        return (now - end) > SENDEMAILTIMEOUT;
    }

}

