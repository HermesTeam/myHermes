package com.example.demo.bean;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
public class User {
    //用户id
    private int userId;
    //用户名
    private String userName;
    //用户登录名
    private String userLoginname;
    //用户密码
    private String userPassword;
    //用户电话
    private String userTel;
    //用户地址
    private String userAddress;
    //注册时间
    private String userDate;
    //收藏数量
    private Integer userMenu;
    //粉丝数量
    private Integer userFans;
    //头像的url地址
    private String textOne;
    //关注数量
    private String textTwo;

    @Id
        @GeneratedValue
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "user_loginname")
    public String getUserLoginname() {
        return userLoginname;
    }

    public void setUserLoginname(String userLoginname) {
        this.userLoginname = userLoginname;
    }

    @Basic
    @Column(name = "user_password")
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Basic
    @Column(name = "user_tel")
    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    @Basic
    @Column(name = "user_address")
    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    @Basic
    @Column(name = "user_date")
    public String getUserDate() {
        return userDate;
    }

    public void setUserDate(String userDate) {
        this.userDate = userDate;
    }

    @Basic
    @Column(name = "user_menu")
    public Integer getUserMenu() {
        return userMenu;
    }

    public void setUserMenu(Integer userMenu) {
        this.userMenu = userMenu;
    }

    @Basic
    @Column(name = "user_fans")
    public Integer getUserFans() {
        return userFans;
    }

    public void setUserFans(Integer userFans) {
        this.userFans = userFans;
    }

    @Basic
    @Column(name = "text_one")
    public String getTextOne() {
        return textOne;
    }

    public void setTextOne(String textOne) {
        this.textOne = textOne;
    }

    @Basic
    @Column(name = "text_two")
    public String getTextTwo() {
        return textTwo;
    }

    public void setTextTwo(String textTwo) {
        this.textTwo = textTwo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId &&
                Objects.equals(userName, user.userName) &&
                Objects.equals(userLoginname, user.userLoginname) &&
                Objects.equals(userPassword, user.userPassword) &&
                Objects.equals(userTel, user.userTel) &&
                Objects.equals(userAddress, user.userAddress) &&
                Objects.equals(userDate, user.userDate) &&
                Objects.equals(userMenu, user.userMenu) &&
                Objects.equals(userFans, user.userFans) &&
                Objects.equals(textOne, user.textOne) &&
                Objects.equals(textTwo, user.textTwo);
    }


}
