package com.learn;

import com.learn.common.utils.PropertyUtil;
import com.learn.common.utils.email.MailSenderInfo;
import com.learn.common.utils.email.SimpleMailSender;

/**
 * Created by Yi on 2015/6/16.
 */
public class EmailTest {
    public static void main(String[] args){
        String address = "https://www.baidu.com";
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

        mailInfo.setFromAddress("sunyiex@163.com"); //发送者
        mailInfo.setToAddress("617120280@qq.com");  //接受者
        mailInfo.setSubject("setSubject");      //标题
        mailInfo.setContent(stringBuffer.toString());      //内容
        //这个类主要来发送邮件
        SimpleMailSender sms = new SimpleMailSender();
        sms.sendHtmlMail(mailInfo);//发送文体格式
//        sms.sendHtmlMail(mailInfo);//发送html格式
    }
}
