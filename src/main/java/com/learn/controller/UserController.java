package com.learn.controller;

import com.learn.domain.LoginUser;
import com.learn.domain.BankCard;
import com.learn.service.BankCardService;
import com.learn.service.LoginUserService;
import com.learn.common.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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

    @Autowired
    private LoginUserService loginUserService;

    @Autowired
    private BankCardService bankCardService;

    @RequestMapping(value = "/login.do", method = RequestMethod.GET)
    public ModelAndView login(ModelAndView modelAndView) {
        modelAndView.addObject("test", "sunmasd");
        modelAndView.setViewName("/user/login");
        return modelAndView;
    }

    @RequestMapping(value = "/doLogin.do", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView loginIn(HttpServletRequest request, ModelAndView modelAndView, HttpSession session) {
        String username = request.getParameter("IDCard");
        String password = request.getParameter("password");
        password = MD5Util.MD5(password);

        String msg = "success";

        LoginUser loginUser = loginUserService.findByIDCard(username);

        if (loginUser != null) {
            if (password.equals(loginUser.getPassword())) {
                //登陆成功
                session.setAttribute("userId", loginUser.getId());
                session.setAttribute("userIDCard", loginUser.getIDCard());

                modelAndView.addObject("user", loginUser);
                modelAndView.addObject("message", msg);
                if (loginUser.getType() == null || loginUser.getType().equals("admin")) {
                    //管理员
                    modelAndView.addObject("message", msg);
                    modelAndView.setViewName("/user/adminCenter");
                } else {
                    //普通用户
//                    session.setAttribute("cardList", loginUser.getCardList());
                    modelAndView.addObject("cardList", loginUser.getCardList());
                    modelAndView.addObject("message", msg);
                    modelAndView.setViewName("redirect:/user/userCenter.do");
                }
            } else {
                msg = "password error";
                modelAndView.addObject("message", msg);
                modelAndView.setViewName("/user/result");
            }
        } else {
            msg = "could not find this member";
            modelAndView.addObject("message", msg);
            modelAndView.setViewName("/user/result");
        }
        return modelAndView;
    }

    /**
     * 进入注册页面
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
     * 注册会员（没有加信息判断）
     * 2015年6月7日14:18:21
     * @param request
     * @param modelAndView
     * @return
     * @throws UnsupportedEncodingException
     * @throws ParseException
     */
    @RequestMapping(value = "/doRegister.do", method = RequestMethod.POST)
    public ModelAndView onRegister(HttpServletRequest request, ModelAndView modelAndView) throws UnsupportedEncodingException, ParseException {
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String IDCard = request.getParameter("IDCard");
        String birthdaystr = request.getParameter("birthday");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String sex = request.getParameter("sex");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthday = sdf.parse(birthdaystr);

        LoginUser user = loginUserService.findByIDCard(IDCard);
        if (user != null) {
            modelAndView.addObject("message", "会员已经被注册过了");
            modelAndView.setViewName("/user/login");
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
            modelAndView.setViewName("/user/login");
            return modelAndView;
        }

        modelAndView.addObject("message", "success");
        modelAndView.setViewName("/user/result");
        return modelAndView;
    }

    @RequestMapping(value = "/IDCardInquiry.do", method = RequestMethod.GET)
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
     * @param modelAndView
     * @return
     */
    @RequestMapping(value = "/index.do", method = RequestMethod.GET)
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.addObject("test", "sunmasd");
        modelAndView.setViewName("/user/index");
        return modelAndView;
    }

    /**
     * 进入会员中心页面
     * 2015年6月7日14:17:31
     * @param modelAndView
     * @param session
     * @return
     */
    @RequestMapping(value = "/userCenter.do", method = RequestMethod.GET)
    public ModelAndView userCenter(ModelAndView modelAndView, HttpSession session) {
        long loginUserId = (Long) session.getAttribute("userId");
        String loginUserIDCard = (String) session.getAttribute("userIDCard");
        LoginUser loginUser = loginUserService.findByIDCard(loginUserIDCard);
        if(loginUser.getId() != loginUserId){
            modelAndView.addObject("message", "用户信息错误");
            modelAndView.setViewName("/user/result");
        }
        List<BankCard> cardList = loginUser.getCardList();
        modelAndView.addObject("user", loginUser);
        modelAndView.addObject("cardList", cardList);
        modelAndView.setViewName("/user/userCenter");
        return modelAndView;
    }

    /**
     * 进入信息修改页面
     * 2015年6月7日14:17:11
     * @param modelAndView
     * @param session
     * @return
     */
    @RequestMapping(value = "/changesInfo.do", method = RequestMethod.GET)
    public ModelAndView changesInfo(ModelAndView modelAndView, HttpSession session) {
        long loginUserId = (Long) session.getAttribute("userId");
        String loginUserIDCard = (String) session.getAttribute("userIDCard");
        LoginUser loginUser = loginUserService.findByIDCard(loginUserIDCard);
        if(loginUser.getId() != loginUserId){
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
     * @param request
     * @param modelAndView
     * @param session
     * @return
     */
    @RequestMapping(value = "/doChangeUserInfo.do", method = RequestMethod.GET)
    @ResponseBody
    public String doChangeUserInfo(HttpServletRequest request, ModelAndView modelAndView, HttpSession session) {
        String type = request.getParameter("type");
        String newVal = request.getParameter("newVal");
        long loginUserId = (Long) session.getAttribute("userId");
        String loginUserIDCard = (String) session.getAttribute("userIDCard");
        LoginUser loginUser = loginUserService.findByIDCard(loginUserIDCard);
        if(loginUser.getId() != loginUserId){
            return  "用户信息错误";
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
     * @param request
     * @param modelAndView
     * @param session
     * @return
     */
    @RequestMapping(value = "/addBankCard.do", method = RequestMethod.GET)
    @ResponseBody
    public String addBankCard(HttpServletRequest request, ModelAndView modelAndView, HttpSession session) {
        String message;
        long loginUserId = (Long) session.getAttribute("userId");
        String loginUserIDCard = (String) session.getAttribute("userIDCard");
        LoginUser loginUser = loginUserService.findByIDCard(loginUserIDCard);
        if(loginUser.getId() != loginUserId){
            return  "用户信息错误";
        }
        if (loginUser.getCardList().size() >= 10) {
            return "每个账户最多绑定10个银行卡号";
        }
        BankCard lastCard = bankCardService.findLast();
        BankCard newCard = new BankCard();
        newCard.setCardNumber(String.valueOf(Long.parseLong(lastCard.getCardNumber()) + 1));
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
     * @param request
     * @param modelAndView
     * @param session
     * @return
     */
    @RequestMapping(value = "/doLossCard.do", method = RequestMethod.GET)
    @ResponseBody
    public String doLossCard(HttpServletRequest request, ModelAndView modelAndView, HttpSession session) {
        String message = "error";
        long loginUserId = (Long) session.getAttribute("userId");
        String loginUserIDCard = (String) session.getAttribute("userIDCard");
        LoginUser loginUser = loginUserService.findByIDCard(loginUserIDCard);
        if(loginUser.getId() != loginUserId){
            return  "用户信息错误";
        }
        String cardNumber = request.getParameter("cardNumber");
        BankCard lossCard = bankCardService.findByCardNumber(cardNumber);
        for (int i = 0; i < loginUser.getCardList().size(); i++){
            if (loginUser.getCardList().get(i).getCardNumber().equals(cardNumber)) {
                message = "success";
                break;
            }
        }
        if(!message.equals("success")){
            return "该会员没有此卡号： " + cardNumber;
        }
        lossCard.setActiveFlag("reportLoss");
        message = bankCardService.save(lossCard);
        if (message.equals("success")) {
            session.setAttribute("user", loginUser);
        }
        return message;
    }

    /**
     * 删除卡号
     * 2015年6月7日14:16:11
     * @param request
     * @param modelAndView
     * @param session
     * @return
     */
    @RequestMapping(value = "/deleteBankCard.do", method = RequestMethod.GET)
    @ResponseBody
    public String deleteBankCard(HttpServletRequest request, ModelAndView modelAndView, HttpSession session) {
        String message = "error";
        long loginUserId = (Long) session.getAttribute("userId");
        String loginUserIDCard = (String) session.getAttribute("userIDCard");
        LoginUser loginUser = loginUserService.findByIDCard(loginUserIDCard);
        if(loginUser.getId() != loginUserId){
            return  "用户信息错误";
        }
        String cardNumber = request.getParameter("cardNumber");
        String password = request.getParameter("password");
        if( password == null || !MD5Util.MD5(password).equals(loginUser.getPassword())){
            return "密码错误";
        }
        BankCard deleteCard = null;
        for (int i = 0; i < loginUser.getCardList().size(); i++){
            if (loginUser.getCardList().get(i).getCardNumber().equals(cardNumber)) {
                deleteCard = loginUser.getCardList().get(i);
                message = "success";
                break;
            }
        }
        if(!message.equals("success")){
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
     * @param modelAndView
     * @param session
     * @return
     */
    @RequestMapping(value = "/operation.do", method = RequestMethod.GET)
    public ModelAndView operation(ModelAndView modelAndView,HttpSession session) {
        long loginUserId = (Long) session.getAttribute("userId");
        String loginUserIDCard = (String) session.getAttribute("userIDCard");
        LoginUser loginUser = loginUserService.findByIDCard(loginUserIDCard);
        if(loginUser.getId() != loginUserId){
            modelAndView.addObject("message", "用户信息错误");
            modelAndView.setViewName("/user/result");
        }
        modelAndView.addObject("user", loginUser);
        modelAndView.addObject("user", loginUser.getCardList());
        modelAndView.setViewName("/user/operation");
        return modelAndView;
    }

    @RequestMapping(value = "/logout.do", method = RequestMethod.GET)
    public ModelAndView logout(ModelAndView modelAndView,HttpSession session) {
        session.removeAttribute("userId");
        session.removeAttribute("userIDCard");
        modelAndView.setViewName("redirect:/user/userCenter.do");
        return modelAndView;
    }
}

