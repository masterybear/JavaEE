package org.example.springDemo;

import org.example.service.UserService;
import org.example.springDemo.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

//        m1(ctx);
//        m2(ctx);
//        m3(ctx);
//        m4(ctx);
        m5(ctx);
    }

    private static void m5(ClassPathXmlApplicationContext ctx) {
        UserService userService = ctx.getBean(UserService.class);
        List<String> allUsers = userService.getAllUsers();
        System.out.println("user_" + allUsers);
    }

    private static void m4(ClassPathXmlApplicationContext ctx) {
        User user4 =(User) ctx.getBean("user4");
        System.out.println("user4 = " + user4);
        System.out.println("--------------------------------------------");
    }

    private static void m3(ClassPathXmlApplicationContext ctx) {
        User user3 = (User) ctx.getBean("user3");
        System.out.println("user3 = " + user3);
        System.out.println("--------------------------------------------");
    }

    private static void m2(ClassPathXmlApplicationContext ctx) {
        User user2 = (User) ctx.getBean("user2");
        System.out.println("user2 = " + user2);
        System.out.println("--------------------------------------------");
    }

    private static void m1(ClassPathXmlApplicationContext context) {
        User user1 = (User) context.getBean("user1");
        System.out.println("user1 = " + user1);
        System.out.println("--------------------------------------------");
    }
}
