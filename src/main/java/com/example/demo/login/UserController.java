package com.example.demo.login;

import com.example.demo.bean.*;
import com.example.demo.hbaseutil.HBaseService;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
@ResponseBody
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;



    @Autowired
    HBaseService hBaseService;

    @Autowired
    HbaseTemplate hbaseTemplate;


    /*
    * 功能：删除一些标签（喜欢和不喜欢的都可以删除）
    * 参数：userid（用户id），labelname（标签值）
    * 步骤：
    * 1
    *
    *
    * */
    @RequestMapping("/shanchulabel")
    public CommonResult shanchulabel(String labelname,int userid){
        if(labelname==null||labelname.equals("")||userid<=0)return new CommonResult(416,"参数错误！");
        Map<String,String> labelmap=hBaseService.getfamily(AtionZhiduan.ation,userid+"",AtionZhiduan.label);
        for(Map.Entry<String, String> entry : labelmap.entrySet()){
            String mapKey = entry.getKey();
            String mapValue = entry.getValue();
            if(mapValue.equals(labelname)) {
                hbaseTemplate.delete(AtionZhiduan.ation,userid+"", AtionZhiduan.label, mapKey.split("_")[1]);
                return new CommonResult(200,"成功！");
            }
        }
        return new CommonResult(416,"没有这个标签");
    }




    /*
     * 功能：取消点赞
     * 参数：useri（用户id），recipesid（菜谱id）
     * 步骤：
     * 1判断参数是否合法
     * 2通过userid得到myrecipes列族
     * 3便利map比对是否存在recipesid
     * 4得到recipesid的族修改为*
     * url：
     * */
    @RequestMapping("/quxiaodianzan")
    public CommonResult quxiaodianzan(int userid,String recipesid){
        if(recipesid==null||recipesid.equals("")||userid<=0)return new CommonResult(416,"参数攒错了");
        try {
            Map<String,String> listation=hBaseService.getfamily(AtionZhiduan.ation,userid+"",AtionZhiduan.like);
            String mapkey=AtionZhiduan.zan[0];
            int x=1;
            while(listation.get(mapkey+x)!=null){
                if(recipesid.equals(listation.get(mapkey+x)))break;
                x++;
            }
            if(x<=listation.size()){
                String aa[]=AtionZhiduan.zan.clone();
                aa[0]=(mapkey+x);
                hBaseService.updateCell(aa,userid+"","*");
            }
            else return new CommonResult(200,"用户根本就没有点赞这个菜谱想骗我？");
        }
        catch (Exception e){
            return new CommonResult(500,e.toString());
        }
        return new CommonResult(200,"修改成功！");


    }







    /*
    * 功能：取消收藏
    * 参数：useri（用户id），recipesid（菜谱id）
    * 步骤：
    * 1判断参数是否合法
    * 2通过userid得到myrecipes列族
    * 3便利map比对是否存在recipesid
    * 4得到recipesid的族修改为*
    * url：
    * */
    @RequestMapping("/quxiaoshoucang")
    public CommonResult quxiaoshoucang(int userid,String recipesid){
        if(recipesid==null||recipesid.equals("")||userid<=0)return new CommonResult(416,"参数攒错了");
        try {
        Map<String,String> listation=hBaseService.getfamily(AtionZhiduan.ation,userid+"",AtionZhiduan.myrecipes);
        String mapkey=AtionZhiduan.collect[0];
        int x=1;
        while(listation.get(mapkey+x)!=null){
            if(recipesid.equals(listation.get(mapkey+x)))break;
            x++;
        }
        if(x<=listation.size()){
            String aa[]=AtionZhiduan.collect.clone();
            aa[0]=(mapkey+x);
            hBaseService.updateCell(aa,userid+"","*");
        }
        else return new CommonResult(200,"用户根本就没有收藏这个菜谱想骗我？");
        }
        catch (Exception e){
            return new CommonResult(500,e.toString());
        }
        return new CommonResult(200,"修改成功！");


    }



    /*
    * 功能：得到用户的全部标签
    *
    * 参数：userid（用户id）
    * */
    @RequestMapping("/getuserlabel")
        public CommonResult getuserlabel(@Min(0) int userid){
        if(userid<=0)return new CommonResult(416,"userid不能为负数");
        Map<String,String> datamap=hBaseService.getfamily(AtionZhiduan.ation,userid+"",AtionZhiduan.label);

        return new CommonResult(200,"成功！",datamap);
    }


    /*
     * 功能：得到点赞的浏览记录（暂时没有按照时间排序）
     * 参数：userid(用户id)
     * 步骤：
     * 1检查userid
     * 2得到ation_like这个列族的map
     * 3遍历
     * 4放入list
     * */
    @RequestMapping("/getmydianzan")
    public CommonResult getmydianzan(int userid){
        if(userid<=0)return new CommonResult(416,"userid不能为负数");
        Map<String,String> datamap=hBaseService.getfamily(AtionZhiduan.ation,userid+"",AtionZhiduan.like);
        String mapkey=AtionZhiduan.like+"_"+AtionZhiduan.zan[0];
        List<Hrecipes> hrecipesList=new ArrayList<Hrecipes>();
        int x=1;
        while(datamap.get(mapkey+x)!=null){
            Hrecipes aa=null;
            if(datamap.get(mapkey+x).equals("*"))aa=hBaseService.getrecipes(Zhiduan.hrecipes,datamap.get(mapkey+x));
            if(aa!=null)hrecipesList.add(aa);
            x++;
        }
        return new CommonResult(200,"成功！",hrecipesList);
    }






    /*
     * 功能：得到用户的浏览记录（暂时没有按照时间排序）
     * 参数：userid(用户id)
     * 步骤：
     * 1检查userid
     * 2得到ation_like这个列族的map
     * 3遍历
     * 4放入list
     * */
    @RequestMapping("/getmyliulan")
    public CommonResult getmyliulan(int userid){
        if(userid<=0)return new CommonResult(416,"userid不能为负数");
        Map<String,String> datamap=hBaseService.getfamily(AtionZhiduan.ation,userid+"",AtionZhiduan.like);
        String mapkey=AtionZhiduan.like+"_"+AtionZhiduan.look[0];
        List<Hrecipes> hrecipesList=new ArrayList<Hrecipes>();
        int x=1;
        while(datamap.get(mapkey+x)!=null){
            Hrecipes aa=hBaseService.getrecipes(Zhiduan.hrecipes,datamap.get(mapkey+x));
            if(aa!=null)hrecipesList.add(aa);
            x++;
        }
        return new CommonResult(200,"成功！",hrecipesList);
    }


    /*
     * 功能：得到用户的发布的菜谱
     * 参数：userid(用户id)
     * 步骤：
     * 1检查userid
     * 2得到ation_myrecipes这个列族的map
     * 3遍历
     * */
    @RequestMapping("/getmyrecipes")
    public CommonResult getmyrecipes(int userid){
        if(userid<=0)return new CommonResult(416,"userid不能为负数");
        Map<String,String> datamap=hBaseService.getfamily(AtionZhiduan.ation,userid+"",AtionZhiduan.myrecipes);
        String mapkey=AtionZhiduan.myrecipes+"_"+AtionZhiduan.my[0];
        List<Hrecipes> hrecipesList=new ArrayList<Hrecipes>();
        int x=1;
        while(datamap.get(mapkey+x)!=null){
            Hrecipes aa=hBaseService.getrecipes(Zhiduan.hrecipes,datamap.get(mapkey+x));
            if(aa!=null)hrecipesList.add(aa);
            x++;
        }
        return new CommonResult(200,"成功！",hrecipesList);
    }




    /*
    * 功能：得到用户的收藏菜谱
    * 参数：userid(用户id)
    * 步骤：
    * 1检查userid
    * 2得到ation_myrecipes这个列族的map
    * 3遍历
    * url:http://localhost:8848/user/getshouchang?userid=3
    * */
    @RequestMapping("/getshouchang")
    public CommonResult getshouchang(int userid){
        if(userid<=0)return new CommonResult(416,"userid不能为负数");
        Map<String,String> datamap=hBaseService.getfamily(AtionZhiduan.ation,userid+"",AtionZhiduan.myrecipes);
        String mapkey=AtionZhiduan.myrecipes+"_"+AtionZhiduan.collect[0];
        List<Hrecipes> hrecipesList=new ArrayList<Hrecipes>();
        int x=1;
        while(datamap.get(mapkey+x)!=null){
            Hrecipes aa=null;
            if(datamap.get(mapkey+x).equals("*"))aa=hBaseService.getrecipes(Zhiduan.hrecipes,datamap.get(mapkey+x));
            if(aa!=null)hrecipesList.add(aa);
            x++;
        }
        return new CommonResult(200,"成功！",hrecipesList);
    }




    /*
     * 功能：用户增加不喜欢的标签
     * 参数：userid（用户id），label(标签值)
     * 步骤：
     * 1请求参数判断
     * 2得到ation中notlab0的值
     * 3如果notlab0是空的，就认为没有这个用户
     * 4如果lab0不是空，将label存入数据库
     * */
    @RequestMapping("/addnotlabel")
    public CommonResult addnotlabel(int userid,String label){
        if(label==null||label.equals("")||userid<=0)return new CommonResult(416,"用户参数错误");
        String strlabel0=hBaseService.get(AtionZhiduan.notlab0,userid+"");
        //hBaseService.updateCell(AtionZhiduan.notlab0,userid+"","0");
        if(strlabel0.equals(""))return new CommonResult(416,"用户参数错误");
        hBaseService.puttwe(AtionZhiduan.notlab0,AtionZhiduan.notlab,userid+"",Integer.parseInt(strlabel0)+1+"",label);
        return new CommonResult(200,"成功！");
    }



    /*
    * 功能：用户增加喜欢的标签
    *
    * 参数：userid（用户id），label(标签值)
    *
    * 步骤：
    * 1请求参数判断
    * 2得到ation中lab0的值
    * 3如果lab0是空的，就认为没有这个用户
    * 4如果lab0不是空，将label存入数据库
    *
    * */
    @RequestMapping("/addlabel")
    public CommonResult addlabel(int userid,String label){
        String strlabel0=hBaseService.get(AtionZhiduan.lab0,userid+"");
        if(strlabel0.equals(""))return new CommonResult(416,"用户参数错误");
        hBaseService.puttwe(AtionZhiduan.lab0,AtionZhiduan.lab,userid+"",Integer.parseInt(strlabel0)+1+"",label);
        return new CommonResult(200,"成功！");
    }










    /*
    * 功能：根据用户id得到用户的一些数据
    * 参数：id（用户id）
    * 步骤：
    * 1根据用户id去mysql里面查到用户数据
    * 2根据用户id去hbase里面得到用户的点赞数量和发布的菜谱数量
    * 3封装进userdata再封装进CommonResult
    *
    * */
    @RequestMapping("/prief")

    public CommonResult getprief(int id){


        User users=userService.exituser(id);
        if(users==null) return new CommonResult(416,"用户id错误！");
        String zan=hBaseService.get(AtionZhiduan.myzan,id+"");
        String myrecipes=hBaseService.get(AtionZhiduan.my0,id+"");
        Userdata user=new Userdata(users);
        if(!zan.equals(""))user.setMyzan(Integer.parseInt(zan));else user.setMyzan(0);
        if(myrecipes.equals(""))user.setMyrecipesmun(0);else user.setMyrecipesmun(Integer.parseInt(myrecipes));

        return new CommonResult(200,"成功",user);
    }




    /*
    * 功能：用户登录
    * 参数说明：nanme（用户登录名）password（用户密码）
    * 逻辑：
    * 1判断用户名密码不能为空
    * 2通过name和password找到用户
    * 3判断找到的用户是否为空，空则返回错误信息
    * 4用户非空则返回user的信息
    * url
    * */
    //用户登录用的,loginname唯一
    @RequestMapping("/login")
    @ResponseBody
    public CommonResult login(String name, String password){
        if(name.equals("")||password.equals(""))return new CommonResult(416,"空用户或空密码你也好意识登陆？");
        User user=userService.login(name,password);
        if(user==null)return new CommonResult(416,"用户名或者密码错误！");
        return new CommonResult(200,"成功！",user);
    }




    //注册模块setlist
    //test: http://localhost:8080/adduser?username=xsm&loginname=xsm&password=123&tel=13714555328

    /*
    * 功能：用户的注册模块
    * 参数说明：username（用户的真实姓名）
    *          loginname（用户的登陆名）
    *          password（用户的密码）
    *          tel（用户电话）
    * 逻辑：
    * 1判断loginname和password不能为空
    * 2调用userservice注册模块
    * 3注册完成后得到mysql中userid作为hbase中ation的rowkey初始化ation
    * */

    @RequestMapping("/adduser")
    @ResponseBody
    public CommonResult adduser(String username,
                                String loginname,
                                String password,
                                String tel){
        if(loginname.equals("")||password.equals(""))return new CommonResult(400,"用户名密码不规范！");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式,用户注册时间
        userService.zuche(username,loginname,password,tel,"",df.format(new Date()),0,0,"","");
        int userid=userRepository.findByUserLoginnameAndUserPassword(loginname,password).getUserId();
        List<String[]> zhiduan=new ArrayList<String[]>();
        List<String> values=new ArrayList<>();
        zhiduan.add(AtionZhiduan.my0);
        zhiduan.add(AtionZhiduan.myzan);
        zhiduan.add(AtionZhiduan.collect0);
        zhiduan.add(AtionZhiduan.look0);
        zhiduan.add(AtionZhiduan.ping0);
        zhiduan.add(AtionZhiduan.zan0);
        zhiduan.add(AtionZhiduan.lab0);
        zhiduan.add(AtionZhiduan.notlab0);
        for(int x=1;x<=zhiduan.size();x++)
            values.add("0");
        hBaseService.setlist(zhiduan,userid+"",values);

        return new CommonResult(200,"成功");
    }

    /*
    * 功能：在注册前判断用户名是否存在
    * 参数说明：loginname（用户登录名）
    * */
    //ajax 异步查询loginname是否存在
    @RequestMapping("/isexsitloginname")
    @ResponseBody
    public boolean isExsitLoginname(String loginname){
        if(userService.isExsitLoginname(loginname)==null)
            return false;
        else
            return true;
    }

    @RequestMapping("/updateuser")
    @ResponseBody
    public int updateuser(String username,
                          String loginname,
                          String password,
                          String tel,
                          String useraddress){
        return userService.updateuser(username,loginname,password,tel,useraddress);
    }
}
