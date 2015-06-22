package com.learn.service.impl;

import com.learn.common.utils.DigestUtil;
import com.learn.common.utils.PropertyUtil;
import com.learn.common.utils.email.MailSenderInfo;
import com.learn.common.utils.email.SimpleMailSender;
import com.learn.domain.BankCard;
import com.learn.domain.LoginInLog;
import com.learn.domain.LoginUser;
import com.learn.repository.BankCardDao;
import com.learn.repository.LoginInlogDao;
import com.learn.repository.LoginUserDao;
import com.learn.service.LoginUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yi on 2015/5/24.
 */
@Service
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class LoginUserServiceImpl implements LoginUserService {

    @Resource
    private LoginUserDao loginUserDao;
    @Resource
    private LoginInlogDao loginInlogDao;
    @Resource
    private BankCardDao bankCardDao;

    /**
     * @param username
     * @return
     */
    public LoginUser findByIDCard(String username) {
        return loginUserDao.findByIDCard(username);
    }

    /**
     * @param loginUser
     * @return
     */
    public String save(LoginUser loginUser) {
        try {
            loginUserDao.save(loginUser);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }

    public String addBankCard(LoginUser loginUser, BankCard bankCard) {
        loginUser.addCardList(bankCard);
        try {
            bankCardDao.save(bankCard);
            loginUserDao.save(loginUser);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }

    public String addLoginInlog(LoginUser loginUser, LoginInLog loginInLog) {
        loginUser.addLoginIglooList(loginInLog);
        try {
            loginInlogDao.save(loginInLog);
            loginUserDao.save(loginUser);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }
    public List<LoginUser> findAll(){
        return loginUserDao.findAll();
    }
    public Page<LoginUser> findAll(PageRequest pageRequest) {
        return loginUserDao.findAll(pageRequest);
    }
    public String deleteBankCard(LoginUser loginUser, BankCard bankCard) {
        if(bankCard.getBalance() > 1){
            return "账户余额大于1元，请转账后再删除";
        }
        loginUser.removeCardList(bankCard);
        try {
            loginUserDao.save(loginUser);
            bankCardDao.delete(bankCard);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }

    public String deleteFristLoginInLog(LoginUser loginUser) {
        LoginInLog loginInLog = loginUser.getLoginIglooList().get(0);
        loginUser.removeLoginIglooList(loginInLog);
        try {
            loginUserDao.save(loginUser);
            loginInlogDao.delete(loginInLog);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }

    public String deleteLoginInLog(LoginUser loginUser, LoginInLog loginInLog) {
        loginUser.removeLoginIglooList(loginInLog);
        try {
            loginUserDao.save(loginUser);
            loginInlogDao.delete(loginInLog);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }

    public String delete(LoginUser loginUser) {
        try {
            loginUserDao.delete(loginUser);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }

    private boolean check(String val, String exp) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(exp);
        matcher = pattern.matcher(val);
        return matcher.find();
    }

    public String checkUser(LoginUser loginUser, String birthday) {
        String msg = "success";

        String nameRegExp = "^[\\x{4e00}-\\x{9fa5}]{1,15}$";
        String IDCardRegExp = "^[0-9]{17,17}[0-9|X|x]{1,1}$";
        String phoneRegExp = "^[0-9]{11,11}$";
        String emailRegRxp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        String birthdayRegExp = "((^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._])(10|12|0?[13578])([-\\/\\._])(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._])(11|0?[469])([-\\/\\._])(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._])(0?2)([-\\/\\._])(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([3579][26]00)([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([1][89][0][48])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([2-9][0-9][0][48])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([1][89][2468][048])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([2-9][0-9][2468][048])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([1][89][13579][26])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([2-9][0-9][13579][26])([-\\/\\._])(0?2)([-\\/\\._])(29)$))";
        String passwordRegExp = "^[^ˇ|^～]{6,16}$";
        String sexRegExp = "^(male|female)$";
        if (!check(loginUser.getIDCard(), IDCardRegExp)) {
            return "身份证";
        }
        if (!check(loginUser.getPhone(), phoneRegExp)) {
            return "手机";
        }
        if (!check(loginUser.getEmail(), emailRegRxp)) {
            return "email";
        }
        if (!check(loginUser.getPassword(), passwordRegExp)) {
            return "密码";
        }
        if (!check(loginUser.getSex(), sexRegExp)) {
            return "性别";
        }
        if (!check(birthday, birthdayRegExp)) {
            return "生日";
        }
        if (!check(loginUser.getName(), nameRegExp)) {
            return "姓名";
        }
        return msg;
    }
    public String checkUser(LoginUser loginUser) {
        String msg = "success";

        String nameRegExp = "^[\\x{4e00}-\\x{9fa5}]{1,15}$";
        String IDCardRegExp = "^[0-9]{17,17}[0-9|X|x]{1,1}$";
        String phoneRegExp = "^[0-9]{11,11}$";
        String emailRegRxp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        String passwordRegExp = "^[^ˇ|^～]{6,16}$";
        String sexRegExp = "^(male|female)$";
        if (!check(loginUser.getIDCard(), IDCardRegExp)) {
            return "身份证";
        }
        if (!check(loginUser.getPhone(), phoneRegExp)) {
            return "手机";
        }
        if (!check(loginUser.getEmail(), emailRegRxp)) {
            return "email";
        }
        if (!check(loginUser.getPassword(), passwordRegExp)) {
            return "密码";
        }
        if (!check(loginUser.getSex(), sexRegExp)) {
            return "性别";
        }
        if (!check(loginUser.getName(), nameRegExp)) {
            return "姓名";
        }
        return msg;
    }
    public String sendEmail(String toEmail, String IDCard, long userId, String URL) {
        String time = String.valueOf(System.currentTimeMillis());
        String id = String.valueOf(userId);
        time = DigestUtil.Encrypt(time);
        IDCard = DigestUtil.Encrypt(IDCard);
        id = DigestUtil.Encrypt(id);
        String address = URL + "/user/setNewPassword.do?IDCard=" + IDCard + "&time=" + time + "&id=" + id;
        StringBuffer stringBuffer = new StringBuffer("尊敬的会员：");
        stringBuffer.append("<p>尊敬的会员： 请点击后面的地址，进入后课重新设置新的密码。<a href=\"");
        stringBuffer.append(address);
        stringBuffer.append("\">");
        stringBuffer.append(address);
        stringBuffer.append("</a>\n" +
                "  如果您在电子邮件中无法点击此地址，请将此地址复制到浏览器的地址栏中并回车确认。\n" +
                "   此邮件无需回复，如需与快存银行联系，请拔打服务热线 123456。</p>");
        //这个类主要是设置邮件
        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost(PropertyUtil.getEmail("serverHost"));
        mailInfo.setMailServerPort("25");
        mailInfo.setValidate(true);
        mailInfo.setUserName(PropertyUtil.getEmail("username"));//用户名
        mailInfo.setPassword(PropertyUtil.getEmail("password"));//您的邮箱密码
        mailInfo.setFromAddress(PropertyUtil.getEmail("username")); //发送者
        mailInfo.setToAddress(toEmail);  //接受者
        mailInfo.setSubject("快存银行密码找回");      //标题
        mailInfo.setContent(stringBuffer.toString());      //内容
        //这个类主要来发送邮件
        SimpleMailSender sms = new SimpleMailSender();
        try {
            sms.sendHtmlMail(mailInfo);//发送html格式
//            sms.sendHtmlMail(mailInfo);//发送文体格式
        } catch (Exception e) {
            return e.getMessage();
        }

        return "success";
    }
}
