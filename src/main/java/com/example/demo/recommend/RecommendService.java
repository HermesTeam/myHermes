package com.example.demo.recommend;


import com.example.demo.bean.Zhiduan;
import com.example.demo.hbaseutil.HBaseService;
import com.example.demo.bean.Hrecipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendService {

    @Autowired
    HBaseService hBaseService;

    /*
    * 新的推荐方法
    * 菜谱来源于hbase的hrecipes，推荐列表来源于hbase的recommend。
    * 数据逻辑：
    * 根据用户id在recommen中查找推荐列表，返回List<Hrecipes>
    *1得到推荐列表
    *2遍历推荐列表的菜谱id，并查找菜谱数据（redis缓存后面实现）装进list中
    * */
    public List<Hrecipes> newtuijian( int userid){
        //1得到推荐列表
        System.out.println("用户id："+userid);
        Map<String,String> map=hBaseService.get(Zhiduan.recommend,userid+"");
        System.out.println(map);
        //2遍历推荐列表的菜谱id，并查找菜谱数据（redis缓存后面实现）装进list中，
        List<Hrecipes> mylist=new ArrayList<Hrecipes>();
        for(Map.Entry<String, String> entry : map.entrySet()) {
            String mapValue = entry.getValue();
            System.out.println(mapValue);
            mylist.add(hBaseService.getrecipes(Zhiduan.hrecipes,mapValue));
        }
        System.out.println(mylist);
        return mylist;
    }









//
//    @Autowired(required = true)
//    UserRepository userRepository;
//
//    @Autowired
//    RecipseRepository recipseRepository;
//
//
//    //菜谱推荐模块id是用户id，number是推荐几个菜谱，默认六个
//
//    public List<Recipes> tuijian(int id, int number) {
//
//        //1.得到用户全部标签
//        List<Userandlabel> relative = userRepository.findByUserId(id).getUserandlabelsByUserId();
//        for (Userandlabel aa : relative) {
//            System.out.println("标签："+aa.getAndId() + aa.getLabelByLabelId().getLabelName() + aa.getLabelValue());
//        }
//        System.out.println("============对标签的值进行排序=============");
//        //2.对标签的值进行排序，大->小
//        relative.sort(new Comparator<Userandlabel>() {
//            public int compare(Userandlabel a, Userandlabel b) {
//                if (a.getLabelValue() - b.getLabelValue() > 0) return 1;
//                else if (a.getLabelValue() - b.getLabelValue() == 0) return 0;
//                else return -1;
//            }
//        });
//
//        for (Userandlabel aa : relative) {
//            System.out.println("标签：" + aa.getLabelByLabelId().getLabelName() + aa.getLabelValue());
//        }
//
//
//        //3.以标签值得大小筛选10个以下的标签，组合出三个主题标签去数据库找
//        //  用户的标签最大的三个标签。会站1/2的位置
//        //  最后的全部随机选，站1/2
//        Random random = new Random();
//        //arr是数据库text——one
//        int []arr=new int[3];
//
//        //返回的数据集
//        List<Recipes> recipes = new ArrayList<Recipes>();
//        System.out.println("进入标签书==3？");
//        System.out.println(relative.size()==3);
//
//        if(relative.size()<3)return null;
//        else if(relative.size()==3){
//            System.out.println("进入标签书3");
//            arr[0]=relative.get(0).getLabelByLabelId().getLabelId();
//            arr[1]=relative.get(1).getLabelByLabelId().getLabelId();
//            arr[2]=relative.get(2).getLabelByLabelId().getLabelId();
//            recipes=selectrecipes(arr,number);
//        }
//        else if(relative.size()>3){
//            System.out.println("进入标签书》3");
//            arr[0]=relative.get(0).getLabelByLabelId().getLabelId();
//            arr[1]=relative.get(1).getLabelByLabelId().getLabelId();
//            arr[2]=relative.get(2).getLabelByLabelId().getLabelId();
//            recipes=selectrecipes(arr,3/number);
//
//            arr[0]=relative.get(random.nextInt(relative.size())).getLabelByLabelId().getLabelId();
//            arr[1]=relative.get(random.nextInt(relative.size())).getLabelByLabelId().getLabelId();
//            while(arr[1]==arr[0])
//                arr[1]=relative.get(random.nextInt(relative.size())).getLabelByLabelId().getLabelId();
//            arr[2]=relative.get(random.nextInt(relative.size())).getLabelByLabelId().getLabelId();
//            while(arr[2]==arr[0]||arr[2]==arr[1])
//                arr[1]=relative.get(random.nextInt(relative.size())).getLabelByLabelId().getLabelId();
//            recipes.addAll(selectrecipes(arr,3/number));
//        }
//            return recipes;
//    }
//
//
//    //这函数arr是菜谱的主题数，number是选几个
//    public List<Recipes> selectrecipes(int arr[], int number) {
//        List<Recipes> recipes = new ArrayList<Recipes>();
//        Arrays.sort(arr);
//        String str="";
//        for(int num:arr){
//            if(num<10)str=str+"0"+num;
//            else str=str+num;
//        }
//        System.out.println("找的主题数字"+str);
//        List<Recipes> res=recipseRepository.findByTextOne(str);
//        System.out.println("拿到的菜谱数"+res.size());
//        Random random = new Random();
//        if(res.size()!=0){
//            for(int x=0;x<number;x++) {
//                recipes.add(res.get(random.nextInt(res.size()) ));
//            }
//        }
//        return recipes;
//    }
}