package com.example.demo.recipse;


import com.example.demo.Pinyin;
import com.example.demo.bean.*;
import com.example.demo.hbaseutil.HBaseService;
import com.example.demo.login.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/recipse")
@ResponseBody
public class RecipseController {

    @Autowired
    HbaseTemplate hbaseTemplate;

    @Autowired
    HBaseService hBaseService;

    @Autowired
    UserService userService;


    @Autowired
    RecipseService recipseService;







    /*
     * 功能：通过菜谱模糊查询菜谱
     * 参数：likename
     * 步骤：
     * 1.判断参数
     * 2，通过likename在mysql的到rowkey
     * 3封装成list《hrecipes》
     *url：/recipse/recipesbylikename?likename=
     *
     * */
    @RequestMapping("/recipesbylikename")
    public CommonResult recipesbylikename(String likename){
        if(likename==null||likename.equals(""))return new CommonResult(416,"likename为空或者没有这个阐述");
        return new CommonResult(200,"成功！",recipseService.gethbaseidlikename(likename));
    }

    /*
    * 功能：通过菜谱模糊查询菜谱id
    * 参数：likename
    * 步骤：
    * 1.判断参数
    * 2，通过likename在mysql的到rowkey
    * 3封装成list《string返回》
    *url:/recipse/rowkeybylikename?likename=
    *
    * */
    @RequestMapping("/rowkeybylikename")
    public CommonResult rowkeybylikename(String likename){
        if(likename==null||likename.equals(""))return new CommonResult(416,"likename为空或者没有这个阐述");
        return new CommonResult(200,"成功！",recipseService.gethbaseidlikename(likename));
    }



    /*
    * 功能：通过一些标签得到一些类
    * 参数：label（标签值）
    * 步骤：
    *
    *
    * */
    @RequestMapping("/getlabeltorecipes")
    public CommonResult getlabeltorecipes(String label){
        if(label==null||label.equals(""))return new CommonResult(416,"标签为空也传过来麻烦我？");
        Map<String,String> myrecipesids=hBaseService.get(LabelZhiduan.label,label);
        if(myrecipesids.size()<=0)return new CommonResult(200,"没有这个标签哦！,数据库里有的一般是口味啊或者食材啊等的一些标签");
        List<Hrecipes> recipes=new ArrayList<Hrecipes>();
        int num=0;
        for(Map.Entry<String, String> entry : myrecipesids.entrySet()){
            String mapValue = entry.getValue();
            recipes.add(hBaseService.getrecipes(Zhiduan.hrecipes,entry.getValue()));
            if((num++)>10)break;
        }
        return new CommonResult(200,"成功！",recipes);
    }



    /*
     * 功能：对一个菜谱点赞
     *
     * 参数：userid（用户id），recipes（菜谱id）
     *
     * 步骤：
     * 1判断是否为空
     * 2得到用户的zan0和myzan，菜谱的userid和zan
     * 3用list改ation表格，
     *url:http://106.15.121.200:8848/recipse/zan?userid=1&&recipes=kaoyatui_9
     * */
    @RequestMapping("/zan")
    public CommonResult zan(int userid,String recipes){
        if(userid<=0||recipes==null||recipes.equals(""))return new CommonResult(416,"参数错误！");
        //用户数据ation中的zan0
        String userzan=hBaseService.get(AtionZhiduan.zan0,userid+"");
        //通过recipes得到整个菜谱
        Hrecipes myrecipes=hBaseService.getrecipes(Zhiduan.hrecipes,recipes);
        //通过recipes的userid得到发布菜谱用户的myzan
        String myzan=hBaseService.get(AtionZhiduan.myzan,myrecipes.getUserid());

        hBaseService.updateCell(AtionZhiduan.myzan,myrecipes.getUserid(),(Integer.parseInt(myzan)+1)+"");
        hBaseService.puttwe(AtionZhiduan.zan0,AtionZhiduan.zan,userid+"",(Integer.parseInt(userzan)+1)+"",recipes);

        hBaseService.updateCell(Zhiduan.dianzan,recipes,(myrecipes.getDianzan()+1)+"");


        return  new CommonResult(200,"成功！");
    }

    /*
    * 功能：收藏一个菜谱
    *
    * 参数userid（用户id），recipes（菜谱id）
    *
    * 步骤：
    * 1得到用户收藏数量collect0和菜谱收藏数shoucang
    * 2判断是否为空
    *url:http://106.15.121.200:8848/recipse/shoucang?userid=1&&recipes=kaoyatui_9
    * */
    @RequestMapping("/shoucang")
    public CommonResult shoucang(int userid,String recipes){
        if(userid<=0||recipes==null||recipes.equals(""))return new CommonResult(416,"参数错误！");

        //得到user的收藏数量，recipes的shouchang
        String userping=hBaseService.get(AtionZhiduan.collect0,userid+"");
        String recipesping0=hBaseService.get(Zhiduan.shoucang,recipes);
        //修改用户收藏列表
        hBaseService.puttwe(AtionZhiduan.collect0,AtionZhiduan.collect,userid+"",Integer.parseInt(userping)+1+"",recipes);
        //修改菜谱的收藏数量
        hBaseService.updateCell(Zhiduan.shoucang,recipes,Integer.parseInt(recipesping0)+1+"");
        return  new CommonResult(200,"成功！");
    }


    /*
    * 功能：评论一个菜谱
    *
    * 参数：
    * userid 用户id
    * recipes 菜谱id
    * value 评论内容
    * rate 菜谱分数
    * 1判断参数是否正常
    * 2从数据库里拿到userping和recipesping0
    * 3判断是否有userping和recipesping0
    * 4userping和recipesping0全部加一存回去
    * 5将userid和value存进hrecipes
    *
    * url:http://localhost:8848/recipse/ping?userid=1&&recipes=kaoyatui_9&&value=%E6%88%91%E7%9A%84%E8%AF%84%E8%AE%BA%E6%9D%A5%E4%BA%86&&rate=5.0
    *
    * */
    @RequestMapping("/ping")
    public CommonResult ping(int userid,String recipes,String value,float rate){

        System.out.println("{\\\"userId\\\":\\\""+userid+
                "\\\",\\\"recipesId\\\":\\\""+recipes+
                "\\\",\\\"score\\\":\\\""+rate+
                "\\\",\\\"actionTime\\\":\\\""+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())+
                "\\\",\\\"user_action\\\":\\\"comments\\\"}");

    value=rate+":"+value;
        if(userid<=0||recipes==null||value==null||recipes.equals("")||value.equals(""))return new CommonResult(416,"参数错误！");
        String userping=hBaseService.get(AtionZhiduan.ping0,userid+"");
        String recipesping0=hBaseService.get(Zhiduan.pinglun0,recipes);

        //存入user数据里边
        hBaseService.puttwe(AtionZhiduan.ping0,AtionZhiduan.ping,userid+"",(Integer.parseInt(userping)+1)+"",recipes);


        //将评论存入菜谱
        List<String[]> zhiduan=new ArrayList<String[]>();
        List<String> comvalue=new ArrayList<String>();

        //重写pinglunid和pinglun这两个字段
        String[] pinglunid=Zhiduan.pinglunid;
        String[] pinglun=Zhiduan.pinglun;
        pinglun[0]=pinglun[0]+(Integer.parseInt(recipesping0)+1+"");
        pinglunid[0]=pinglunid[0]+(Integer.parseInt(recipesping0)+1+"");

        //放入pinglunid，pinglun，pinglun0
        zhiduan.add(pinglunid);
        zhiduan.add(pinglun);
        zhiduan.add(Zhiduan.pinglun0);
        comvalue.add(userid+"");
        comvalue.add(value);
        comvalue.add(Integer.parseInt(recipesping0)+1+"");
        hBaseService.setlist(zhiduan,recipes,comvalue);
        return  new CommonResult(200,"成功！");
    }




    /*
    * 功能：用户发布一个菜谱
    *
    * 参数：userid（发布菜谱的用户id），recipes（菜谱类）
    *
    * 1.参数判断
    * 2.定义一个没有的菜谱id，存入菜谱
    * 3.增加user参数
    *
    * */
    @RequestMapping("/addrecipes")
    public CommonResult addrecipes(int userid,Hrecipes hrecipes){
        if(userid<=0||hrecipes==null)return new CommonResult(416,"参数错误！");

        hrecipes.setRecipesid(Pinyin.getAllPinyin(hrecipes.getCaiming())+new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
        hBaseService.put(hrecipes);
        String my=hBaseService.get(AtionZhiduan.my0,userid+"");
        hBaseService.puttwe(AtionZhiduan.my0,AtionZhiduan.my,userid+"",(Integer.parseInt(my)+1)+"",hrecipes.getRecipesid());
        return new CommonResult(200,"成功！");

    }

    /*
    * 功能：查找菜谱的一个接口
    * 参数：输入菜谱id
    * 1判断id是否为空
    * 2查找菜谱
    * 3返回200
    *
    * */

    //localhost:8080/recipse/findrecipsebyid?id=yangchunsanyuedemeishizhengyuqian
    @RequestMapping("/findrecipsebyid")

    public CommonResult findrecipsebuid(String id){
        if(id==null||id.equals("")){return new CommonResult(416,"菜谱的id错误");}
        Hrecipes hrecipes=hBaseService.getrecipes(Zhiduan.hrecipes,id+"");
        if(hrecipes==null||hrecipes.getRecipesid().equals(""))return new CommonResult(416,"菜谱的id错误");
        else return new CommonResult(200,"成功！",hrecipes);


    }

    //当用户浏览了一个菜谱后，增加菜谱的浏览量和用户数据
    /*
    * 传入参数用户id，菜谱id
    * 1判断菜谱id和用户id是否有空值
    * 2判断有没有这个用户或菜谱
    * 3存入hrecipes的liulan和ation中look0和look
    *url:localhost:8848/recipse/liulan?userid=1&&recipes=kaoyatui_9
    * */
    @RequestMapping("/liulan")
    public CommonResult look(int userid,String recipes){
        if(userid<0||recipes==null||recipes.equals(""))return new CommonResult(416,"用户id错误或者没有这个菜谱id");

        String userlooknum=hBaseService.get(AtionZhiduan.look0,userid+"");
        String recipesliulan=hBaseService.get(Zhiduan.liulan,recipes);
        hBaseService.updateCell(Zhiduan.liulan,recipes,(Integer.parseInt(recipesliulan)+1)+"");
        hBaseService.puttwe(AtionZhiduan.look0,AtionZhiduan.look,userid+"",(Integer.parseInt(userlooknum)+1)+"",recipes);
        return new CommonResult(200,"成功了");

    }

}
