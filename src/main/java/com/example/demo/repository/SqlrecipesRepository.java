package com.example.demo.repository;

import com.example.demo.bean.Sqlrecipes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SqlrecipesRepository extends JpaRepository<Sqlrecipes,Integer> {
    //通过用户id找到用户
    @Query("select s from hbaseid s where  s.hbaseId=?1")
    public Sqlrecipes findByHbaseId(String id);


    //通过名字模糊查找
    @Query("select s.hbaseId from hbaseid s where s.recipesName like %?1%")
    public List<String> findByRecipesName(String name);


}
