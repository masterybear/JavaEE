package org.example.javaConfig;

import org.example.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class JavaConfigTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(JavaConfig.class);
        SayHello sayHello = ctx.getBean("sayHello", SayHello.class);
        System.out.println(sayHello.sayHello("张三"));
        UserService userService = ctx.getBean(UserService.class);
        List<String> allUsers = userService.getAllUsers();
        System.out.println("allUsers: " + allUsers);
    }
}
