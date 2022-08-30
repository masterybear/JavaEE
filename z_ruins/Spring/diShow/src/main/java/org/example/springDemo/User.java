package org.example.springDemo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class User {
    private String username;

    private String email;
    private Integer id;
    private Cat cat;
    private Cat[] cats;
    private List<String> hobby;
    private Map<String, Object> details;
    private Properties info;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", id=" + id +
                ", cat=" + cat +
                ", cats=" + Arrays.toString(cats) +
                ", hobby=" + hobby +
                ", details=" + details +
                ", info=" + info +
                '}';
    }

    public Properties getInfo() {
        return info;
    }

    public void setInfo(Properties info) {
        this.info = info;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public Cat[] getCats() {
        return cats;
    }

    public void setCats(Cat[] cats) {
        this.cats = cats;
    }

    public List<String> getHobby() {
        return hobby;
    }

    public void setHobby(List<String> hobby) {
        this.hobby = hobby;
    }

    public User() {
        System.out.println("------------init-------------");
    }

    public User(String username, String email, Integer id) {
        this.username = username;
        this.email = email;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }
}
