package com.example.demo.repository;

import com.example.demo.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User,Integer> {

    //通过用户id找到用户
    @Query("select s from User s where s.userId=?1")
    public User findByUserId(int id);

    //登录模块-用户名密码-用户user
    @Query("select s from User s where s.userLoginname=:name and s.userPassword=:password")
    public User findByUserLoginnameAndUserPassword(@Param("name")String name, @Param("password")String password);

    //用户注册
    @Transactional
    @Modifying
    @Query(value = "insert into user(user_name, user_loginname, user_password, user_tel, user_address, user_date, user_menu, user_fans, text_one, text_two) values(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10)",nativeQuery = true)
    public int insetuser(String username,String loginname,String password,String tel,String address,String date,int menu,int fans,String one,String two);

    @Transactional
    @Modifying
    @Query(value = "update user set user_name=?1,user_loginname=?2,user_password=?3,user_tel=?4,user_address=?5 where user_loginname=?2",nativeQuery = true)
    public int updateuser(String username,String loginname,String password,String tel,String address);

    @Query(value = "select u from User u where  u.userLoginname=?1")
    User findUserByuserLoginname(String loginname);
}
