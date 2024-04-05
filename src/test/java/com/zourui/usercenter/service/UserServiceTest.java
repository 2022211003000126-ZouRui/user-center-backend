package com.zourui.usercenter.service;
import java.util.Date;

import com.zourui.usercenter.model.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;
//    @Test
//     void testAddUser(){
//        User user=new User();
//
//        user.setUserName("dogyupi");
//        user.setUserAccount("123");
//        user.setAvatarUrl("4546");
//        user.setGender(0);
//        user.setUserPassword("xxx");
//        user.setPhone("123");
//        user.setEmail("456");
//
//        boolean save = userService.save(user);
//        System.out.println(user.getId());
//        Assertions.assertTrue(save);
//    }

   // @Test
//    void userRegister() {
//        String userAccount="yupi";
//        String userPassword="";
//        String checkPassword="123456";
//        //断言，junit.jupiter中
//        long l = userService.userRegister(userAccount, userPassword, checkPassword,String);
//        Assertions.assertEquals(-1,l);
//        userAccount="yu";
//        long l1 = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1,l);
//        userAccount="yupi";
//        userPassword="123456";
//        long l2 = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1,l);
//        userAccount="yu pi";
//        userPassword="12345678";
//        long l3 = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1,l);
//        checkPassword="123456789";
//        long l4 = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1,l);
//        userAccount="dogYupi";
//        checkPassword="12345678";
//        long l5 = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1,l);
//        userAccount="yupi";
//        long l6 = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertTrue(l6>0);//输入一个条件，true，则断言成功
    }