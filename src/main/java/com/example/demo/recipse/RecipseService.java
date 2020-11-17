package com.example.demo.recipse;


import com.example.demo.bean.Hrecipes;
import com.example.demo.bean.Sqlrecipes;
import com.example.demo.bean.Zhiduan;
import com.example.demo.hbaseutil.HBaseService;
import com.example.demo.repository.SqlrecipesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipseService {
    //通过id找一个菜谱


    @Autowired
    HBaseService hBaseService;

    @Autowired
    SqlrecipesRepository sqlrecipesRepository;

    public Sqlrecipes getSqlRecipes(String id) {
        return sqlrecipesRepository.findByHbaseId(id);
    }


    //模糊查询返回rowkey
    public List<String> gethbaseidlikename(String name){return sqlrecipesRepository.findByRecipesName(name);}


    //模糊查询返回list-hrecipes
    public List<Hrecipes> gethrecipeslikename(String name){
        List<String> listid=gethbaseidlikename(name);
        List<Hrecipes> listrecipes=new ArrayList<Hrecipes>();
        int x=0;
        for(String rowkey:listid){
            Hrecipes hrecipes=hBaseService.getrecipes(Zhiduan.hrecipes,rowkey);
            if(hrecipes!=null)listrecipes.add(hrecipes);
            if((x++)>15)break;
        }
        return listrecipes;
    }


}