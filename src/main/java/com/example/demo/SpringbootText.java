package com.example.demo;


import com.example.demo.bean.*;
import com.example.demo.hbaseutil.HBaseService;

import com.example.demo.recipse.RecipseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class SpringbootText {
    //测试hbaseserver
    @Autowired
    HBaseService hBaseService;

    @Autowired
    RecipseService recipseService;




    @RequestMapping("/test/recipesname")
    public CommonResult testrecipesname(String name){
        return new CommonResult(200,"成功",recipseService.gethbaseidlikename(name));
    }






    /*
    * 测试hbaseservice里边的updata的方法的
    * */


    /*
    * 功能：得到一个菜谱的全部信息
    * 参数说明：recipesid（菜谱的id）
    * /localhost:8848/text/recipes?reciepsid=naibaodoufu
    * */
    @RequestMapping("/text/recipes")
    public Map<String,String> textrecipes(String reciepsid){
        return hBaseService.get("recipes",reciepsid);
    }


    /*
    * 功能：得到一个用户在数据库里全部用户行为
    * 参数说明：userid(用户的id)
    * url:localhost:8848/text/ation?userid=1
    * */
    @RequestMapping("/text/ation")
    public Map<String,String> textation(int userid){
        return hBaseService.get(AtionZhiduan.ation,userid+"");
    }



    @RequestMapping("/set")
    public void set(){
         printlnmap(hBaseService.get(Zhiduan.hrecipes,"yangchunsanyuedemeishizhengyuqian"));
         //put(Hrecipes hrecipes)
        System.out.println("***************************************put(Hrecipes hrecipes):");
        Hrecipes recipes=hBaseService.getrecipes(Zhiduan.hrecipes,"yangchunsanyuedemeishizhengyuqian");
        recipes.setRecipesid("123");
        recipes.setUserid("3");
        hBaseService.put(recipes);
        printlnmap(hBaseService.get(Zhiduan.hrecipes,"123"));
        System.out.println("/n/n/n***************************************public void setlist(List<String[]> zhi,String rowkey,List<String> value):");
        List<String[]> zhiduan=new ArrayList<String[]>();
        zhiduan.add(Zhiduan.pinglunid);
        zhiduan.add(Zhiduan.pinglun);
        zhiduan.add(Zhiduan.pinglun0);
        List<String> comvalue=new ArrayList<String>();
        comvalue.add("3");
        comvalue.add("测试评论模块");
        comvalue.add("1");
        hBaseService.setlist(zhiduan,"123",comvalue);
        printlnmap(hBaseService.get(Zhiduan.hrecipes,"123"));
        System.out.println("/n/n/n***************************************public void puttwe(String []cellone,String []celltwe,String rowkey,String valueone,String valuetwe):");
        hBaseService.puttwe(Zhiduan.recipsename,Zhiduan.kouwei,"123","更改后名字","更改后的口味");
        printlnmap(hBaseService.get(Zhiduan.hrecipes,"123"));






    }

    public void printlnmap(Map<String,String> map){
        for (Map.Entry<String,String> entry : map.entrySet()) {
            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());

        }
    }



    /*
    * 测试hbaseservice里面的查询方法的
    * */
    @RequestMapping("/get")
    public  void get(){

        //get(String[] cell ,String rowName)
        System.out.println("get(String[] cell ,String rowName):");
        System.out.println(hBaseService.get(Zhiduan.recipsename,"yangchunsanyuedemeishizhengyuqian"));



        //Hrecipes getrecipes(String tableName, String rowName)
        System.out.println("Hrecipes getrecipes(String tableName, String rowName):");
        System.out.println(hBaseService.getrecipes(Zhiduan.hrecipes,"yangchunsanyuedemeishizhengyuqian"));



        //Hrecipes getrecipes(String tableName, String rowName)
        System.out.println("Hrecipes getrecipes(String tableName, String rowName):");
        System.out.println(hBaseService.getrecipes(Zhiduan.hrecipes,"yangchunsanyuedemeishizhengyuqian"));

        //public Map<String, String> get(String tableName, String rowName)
        System.out.println("public Map<String, String> get(String tableName, String rowName):");
        System.out.println(hBaseService.get(Zhiduan.hrecipes,"yangchunsanyuedemeishizhengyuqian"));


    }







}
