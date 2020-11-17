package com.example.demo.bean;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HUser implements Serializable {
    private int userId;
    private String userName;
    private String userLoginname;
    private String userPassword;
    private String userTel;
    private String userAddress;
    private String userDate;
    private Integer userMenu;
    private Integer userFans;
    private String textOne;
    private String textTwo;
    private List<String> userandlabelsByUserId;
}
