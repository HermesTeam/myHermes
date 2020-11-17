package com.example.demo.bean;


import com.example.demo.bean.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Userdata  {
    private int userid;
    private String userName;
    private String userLoginname;
    private String userTel;
    private String userAddress;
    private String userDate;
    private String textOne;
    private String textTwo;

    //发布菜谱的个数
    private int myrecipesmun;
    //自己发布菜谱被点赞数量
    private int myzan;


    public Userdata(User user) {
        this.userid=user.getUserId();
        this.userName = user.getUserName();
        this.userLoginname = user.getUserLoginname();
        this.userTel = user.getUserTel();
        this.userAddress = user.getUserAddress();
        this.userDate = user.getUserDate();
        this.textOne = user.getTextOne();
        this.textTwo = user.getTextTwo();
    }
}
