    package com.example.demo.login;


import com.example.demo.bean.User;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;



    /*
    * 查看用户是否存在，在记录用户行为（浏览收藏点赞）时候验证用的
    *
    *
    *
    * */
    public User exituser(int userid){return userRepository.findByUserId(userid);}





    //用户登录模块输入登录名和密码返回真假

    public User login(String name,String password){
        User users=userRepository.findByUserLoginnameAndUserPassword(name,password);
        if(users!=null)return users;
        //System.out.println(users.get(0).getUserandlabelsByUserId().get(0).getLabelValue());
        return null;
    }
    public boolean adduserlabel(int userid, int labelid){ return true; }

    //用户注册模块
    //这里需要防止loginname 重复
    public boolean zuche(String username,
                         String loginname,
                         String password,
                         String tel,
                         String address,
                         String date,
                         int menu,
                         int fans,
                         String one,
                         String two){
        if(userRepository.insetuser(username,loginname,password,tel,address,date,menu,fans,one,two)==1)
        return true;
        else return false;
    }

    //更新用户数据
    public int updateuser(String username,
                              String loginname,
                              String password,
                              String tel,
                              String address){
        return userRepository.updateuser(username,loginname,password,tel,address);
    }

    public User isExsitLoginname(String loginname){
        return userRepository.findUserByuserLoginname(loginname);
    }



}
