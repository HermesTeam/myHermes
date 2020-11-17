package com.example.demo.hbaseutil;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;





@RestController
public class TextController {
   @Autowired
    public HBaseService hBaseService;
//localhost:8080/getdata
   @GetMapping("/getdata")
    public String hello(){
       System.out.println("测试开始!!!!!!!!!!!!!!!!!!!!!!!!!!!"+System.getProperty("hadoop.home.dir"));
       Map<String,String> aa=hBaseService.get("hrecipes","yangchunsanyuedemeishizhengyuqian");

       for (Map.Entry<String,String> entry : aa.entrySet()) {
           System.out.println("Key = " + entry.getKey() +"    class:"+entry.getValue().getClass() +"     Value = " + entry.getValue());
       }

       return "成功了";
    }


}
