package com.learn.repository;

/**
 * Created by Yi on 2015/5/24.
 */

import com.learn.domain.BankCard;
import com.learn.domain.LoginInLog;
import com.learn.domain.LoginUser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * ���ܲ���
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("/applicationContext.xml")
//�ع�
@TransactionConfiguration(defaultRollback = false)
// �ǵ�Ҫ��XML�ļ�����������Ŷ~~~���ǲ���ע��ķ�ʽ
@Transactional
public class LoginUserDaoTest {

    @Autowired
    private LoginUserDao loginUserDao;
    @Autowired
    private BankCardDao bankCardDao;
    @Autowired
    private LoginInlogDao loginInlogDao;

    @Test
    public void saveLoginUserTest() {

        LoginUser u1 = new LoginUser();
        BankCard c1 = new BankCard();
        BankCard c2 = new BankCard();
        c1.setCardNumber("c1");
        c2.setCardNumber("c2");
        c1.setBalance(10);
        c2.setBalance(5);

        c1 = bankCardDao.save(c1);
        c2 = bankCardDao.save(c2);
        List<BankCard> list = new ArrayList<BankCard>();
        list.add(c1);
        list.add(c2);

        u1.setCardList(list);
        u1.setIDCard("test");
        u1.setPassword("123456");
        loginUserDao.save(u1);
    }

    @Test
    public void findByUserNameTest() {
        LoginUser u1 = loginUserDao.findByIDCard("111111111111111111");
        u1.getCardList();
        Assert.assertEquals(1, u1.getCardList().size());
    }

    @Test
    public void save() {
        LoginUser loginUser = new LoginUser();
        loginUser.setSex("male2");
        loginUser.setPhone("1232");
        loginUser.setName("test2");
        loginUser.setPassword("1232");
        loginUser.setId(3);
        LoginUser u2 = loginUserDao.save(loginUser);
        Assert.assertEquals(u2.getName(), "name1");
    }

    @Test
    public void saveAndFlush() {
        LoginUser loginUser = new LoginUser();
        loginUser.setSex("male2");
        loginUser.setPhone("1232");
        loginUser.setName("name2");
        loginUser.setPassword("1232");
        loginUser.setId(3);
        LoginUser u2 = loginUserDao.saveAndFlush(loginUser);
        Assert.assertEquals(u2.getName(), "name1");
    }

    @Test
    public void delete() {
        LoginUser u1 = loginUserDao.findByIDCard("test1");
        loginUserDao.delete(u1);
        LoginUser u2 = loginUserDao.findByIDCard("test1");
        Assert.assertEquals(u2, null);

    }

    @Test
    public void saveBankCard() {
        BankCard bankCard = new BankCard();
        bankCard.setCardNumber("test1");
        bankCard.setBalance(123);
        try {
            bankCardDao.save(bankCard);
        } catch (Exception e) {
            Assert.assertEquals("1", e.getMessage());
        }
    }

    @Test
    public void findByCardNumber() {
        BankCard bankCard = bankCardDao.findByCardNumber("123");
        Assert.assertEquals("123", bankCard.getBalance());
    }

    @Test
    public void findLastTest() {
        List<BankCard> bankCard = bankCardDao.findAll();
        Assert.assertEquals("123", bankCard.get(bankCard.size() - 1).getBalance());

    }

    @Test
    public void addCard() {
        LoginUser u1 = loginUserDao.findByIDCard("123456");
        BankCard bankCard = new BankCard();
        bankCard.setCardNumber("000003");
        u1.addCardList(bankCard);
        bankCardDao.save(bankCard);
        BankCard aa = bankCardDao.findByCardNumber("000003");
        LoginUser u2 = loginUserDao.findByIDCard("123456");
        Assert.assertEquals("1", u2.getCardList().size());
    }

    @Test
    public void deleteCard() {
        LoginUser u1 = loginUserDao.findByIDCard("123456");
        BankCard bankCard = bankCardDao.findByCardNumber("127");
        u1.removeCardList(bankCard);
        loginUserDao.save(u1);
        bankCardDao.delete(bankCard);
        LoginUser u2 = loginUserDao.findByIDCard("123456");
        Assert.assertEquals("1", u2.getCardList().size());
    }

//    @Test
//    public void findLogBtUserId() {
//        PageRequest pageRequest = new PageRequest(0, 3, new Sort(Sort.Direction.DESC, "id"));
//        Page<LoginInLog> page = loginInlogDao.findByUserId(1l,pageRequest);
//        Assert.assertEquals(page.getContent().get(0).getTime(), new Date());
//    }
}
