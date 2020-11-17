package com.example.demo.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;


import javax.persistence.Column;
import javax.persistence.Entity;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "hbaseid")
public class Sqlrecipes {
    @Id
    @Column(name = "recipes_id")
    private int recipesId;
    @Column(name = "hbase_id")
    private String hbaseId;
    @Column(name = "recipes_name")
    private String  recipesName ;
    @Column(name = "recipes_taste")
    private String  recipesTaste ;
    @Column(name = "recipes_material")
    private String  recipesMaterial ;

}
