package org.example.service;

import org.example.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public List<String> getAllUsers() {

        String hello = userDao.hello();
        System.out.println("hello = " + hello);

        ArrayList<String> users = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            users.add("user_" + i);
        }
        return users;
    }
}
