package com.example.demo.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hrecipes implements Serializable{
    private String recipesid="";//菜谱id
    private String caiming="";//菜名
    private String gongyi="";//工艺
    private String userid="";//发菜谱的用户id
    private String kouwei="";//口味
    private String kaluli="0";//卡路里
    private String tanshui="0";//碳水化合物
    private String danbai="0";//蛋白质
    private ArrayList<String> biaoqian=new ArrayList<String>();//标签
    private ArrayList<String> fuliao=new ArrayList<String>();//辅料
    private String pinglun0="0";//评论数量
    private int liulan=0;//浏览量
    private int shoucang=0;//收藏数量
    private int dianzan=0;//点赞数量
    private String fengxiang="0";//分享
    private float pingfen=0;//评分
    private String chuju="";//厨具
    private String jieshao="";//介绍
    private ArrayList<String> buzoutext=new ArrayList<String>();//步骤
    private ArrayList<String> buzhouimg=new ArrayList<String>();//步骤图片
    private ArrayList<String> zhutitu=new ArrayList<String>();//主题图
    private String nandu="0";//难度
    private String renshu="0";//人数
    private String zhunbeitime="0";//准备时间
    private String zhizuotime="0";//制作时间

    /*
    * dtail是用来存放有两组数据合并使用的量
    * Map<String("zhuliao"),Map<String("主料名"),String("重量")>>
    *Map<String("pinglun"),Map<String("评论人的id"),String("评论内容")>>
    * */
    private Map<String,Map<String,String>> dtail=new HashMap<String,Map<String,String>>();
}
