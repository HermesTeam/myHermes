package com.example.demo.recommend;

import com.example.demo.bean.Hrecipes;
import com.example.demo.bean.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/recommend")
public class RecommendController {


    @Autowired
    RecommendService recommendService;
    /*
    * 功能：推荐菜谱（现在数据是写死的是有一个用户有数据用户名qwl密码123）
    *参数：id（用户id）
    *步骤：
    * 1判断id是否合法
    * 2从habse的commend里面那对推荐列表
    * 3通过推荐列表里面的菜谱id查找菜谱信息
    * 4将recipes封装成list传给前段
    * */

    @RequestMapping("/myrecipes")
    @ResponseBody
    public CommonResult recommend(int id){
        if(id<=0)return new CommonResult(416,"这样的userid你也好意识请求！");
        CommonResult<List> result=new CommonResult<List>();
        try{
            List<Hrecipes> my=recommendService.newtuijian(id);
            if(my.size()>0){
                result.setData(my);
                result.setCode(200);
                result.setMessage("正常！");
            }
            else{
                result.setCode(416);
                result.setMessage("用户id异常，请核对信息");
            }
        }
        catch (Exception e){
            result.setCode(200);
            result.setMessage("服务器出了点小问题。请联系联系后端人员！");
        }
        return result;
    }




}
