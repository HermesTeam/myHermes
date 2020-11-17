package com.example.demo.hbaseutil;

import com.example.demo.bean.Hrecipes;
import com.example.demo.bean.Zhiduan;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* HBaseService重要方法execute 参考资料https://github.com/Al-assad/spring-data-hbase
*HBaseService重要方法execute中的方法基本都是包装了execute（）的方法
*笔记：HbaseTemplate本身就有很多方法基本满足够用了。等以后写好了工具类在有针对性的重写这些方法
*官方api重点看HbaseTemplate这个类
*https://docs.spring.io/spring-hadoop/docs/2.5.0.RELEASE/api/
*spring的官方例子要是早看到就不用走这么多弯路了
*https://github.com/spring-projects/spring-hadoop-samples
*
* */
@Service
public class HBaseService   {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    private String recipesname="hrecipes";


    /**
     * 通过表名  key 和 列族 和列 获取一个数据
     * @return
     */
    public String get(String[] cell ,String rowName) {
        return hbaseTemplate.get(cell[2], rowName,cell[1],cell[0] ,new RowMapper<String>(){
            public String mapRow(Result result, int rowNum) throws Exception {
                List<Cell> ceList =   result.listCells();
                String res = "";
                if(ceList!=null&&ceList.size()>0){
                    for(Cell cell:ceList){
                        res = Bytes.toString( cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                    }
                }
                return res;
            }
        });
    }

    /**
     * 通过表名  key 和 列族 和列 获取一个数据
     * @return
     */
    public String getonex(String[] cell ,String rowName,String x) {
        return hbaseTemplate.get(cell[2], rowName,cell[1],cell[0]+x ,new RowMapper<String>(){
            public String mapRow(Result result, int rowNum) throws Exception {
                List<Cell> ceList =   result.listCells();
                String res = "";
                if(ceList!=null&&ceList.size()>0){
                    for(Cell cell:ceList){
                        res = Bytes.toString( cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                    }
                }
                return res;
            }
        });
    }

    //得到一个列族
    public Map<String,String> getfamily(String tableName, String rowName, String familyName){
        return hbaseTemplate.get(tableName, rowName,familyName,new RowMapper<Map<String,String>>(){
            public Map<String,String> mapRow(Result result, int rowNum) throws Exception {
                List<Cell> ceList =   result.listCells();
                Map<String,String> map = new HashMap<String, String>();
                if(ceList!=null&&ceList.size()>0){
                    for(Cell cell:ceList){
                        map.put(Bytes.toString(cell.getFamilyArray(),cell.getFamilyOffset(),cell.getFamilyLength())+
                                        "_"+Bytes.toString( cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength()),
                                Bytes.toString( cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                    }
                }
                return  map;
            }
        });


    }






    /**
     * http://localhost:8080/recipse/findrecipsebyid?id=wuxiangcandou_16
     * 通过表名和key获取Hrecipes
     * @param tableName
     * @param rowName
     * @return
     */
    public Hrecipes getrecipes(String tableName, String rowName) {
        Map<String, String> aa= get(tableName, rowName);
        Hrecipes recipes=new Hrecipes();
//        private String recipesid;//菜谱id
        if(aa!=null&&aa.size()<1)return null;
        recipes.setRecipesid(rowName);
//        private String caiming;//菜名
        if(aa.get(Zhiduan.prief+"_"+Zhiduan.recipsename[0])!=null)
        recipes.setCaiming(aa.get(Zhiduan.prief+"_"+Zhiduan.recipsename[0]));
//        private String gongyi;//工艺
        if(aa.get(Zhiduan.prief+"_"+Zhiduan.gongyi[0])!=null)
        recipes.setGongyi(aa.get(Zhiduan.prief+"_"+Zhiduan.gongyi[0]));
//        private String userid;//发菜谱的用户id
        if(aa.get(Zhiduan.prief+"_"+Zhiduan.userid[0])!=null)
        recipes.setUserid(aa.get(Zhiduan.prief+"_"+Zhiduan.userid[0]));
//        private String kouwei;//口味
        if(aa.get(Zhiduan.prief+"_"+Zhiduan.kouwei[0])!=null)
        recipes.setKouwei(aa.get(Zhiduan.prief+"_"+Zhiduan.kouwei[0]));
//        private String kaluli;//卡路里
        if(aa.get(Zhiduan.prief+"_"+Zhiduan.kaluli[0])!=null)
        recipes.setKaluli(aa.get(Zhiduan.prief+"_"+Zhiduan.kaluli[0]));
//        private String tanshui;//碳水化合物
        if(aa.get(Zhiduan.prief+"_"+Zhiduan.tanshui[0])!=null)
        recipes.setTanshui(aa.get(Zhiduan.prief+"_"+Zhiduan.tanshui[0]));
//        private String danbai;//蛋白质
        if(aa.get(Zhiduan.prief+"_"+Zhiduan.danbai[0])!=null)
        recipes.setDanbai(aa.get(Zhiduan.prief+"_"+Zhiduan.danbai[0]));
//        private ArrayList<String> biaoqian;//标签
        recipes.setBiaoqian(this.getList(Zhiduan.prief+"_"+Zhiduan.biaoqian[0],aa));
//        private ArrayList<String> fuliao;//辅料
        recipes.setFuliao(this.getList(Zhiduan.prief+"_"+Zhiduan.fuliao[0],aa));
//        private String pinglun0;//评论数量
        if(aa.get(Zhiduan.prief+"_"+Zhiduan.pinglun0[0])!=null)
        recipes.setPinglun0(aa.get(Zhiduan.prief+"_"+Zhiduan.pinglun0[0]));
//        private int liulan=0;//浏览量
        if(aa.get(Zhiduan.prief+"_"+Zhiduan.liulan[0])!=null)
        recipes.setLiulan(Integer.parseInt(aa.get(Zhiduan.prief+"_"+Zhiduan.liulan[0])));
//        private int shoucang=0;//收藏数量
        if(aa.get(Zhiduan.prief+"_"+Zhiduan.shoucang[0])!=null)
        recipes.setShoucang(Integer.parseInt(aa.get(Zhiduan.prief+"_"+Zhiduan.shoucang[0])));
//        private int dianzan=0;//点赞数量
        if(aa.get(Zhiduan.prief+"_"+Zhiduan.dianzan[0])!=null)
         recipes.setDianzan(Integer.parseInt(aa.get(Zhiduan.prief+"_"+Zhiduan.dianzan[0])));
//        private String fengxiang;//分享
        if(aa.get(Zhiduan.prief+"_"+Zhiduan.fenxiang[0])!=null)
         recipes.setFengxiang(aa.get(Zhiduan.prief+"_"+Zhiduan.fenxiang[0]));
//        private float pingfen;//评分
        if(aa.get(Zhiduan.prief+"_"+Zhiduan.pingfen[0])!=null)
         recipes.setPingfen(Float.parseFloat(aa.get(Zhiduan.prief+"_"+Zhiduan.pingfen[0])));
//        private String chuju;//厨具
        if(aa.get(Zhiduan.prief+"_"+Zhiduan.chuju[0])!=null)
         recipes.setChuju(aa.get(Zhiduan.prief+"_"+Zhiduan.chuju[0]));
//        private String jieshao;//介绍
        if(aa.get(Zhiduan.detail+"_"+Zhiduan.jieshao[0])!=null)
        recipes.setJieshao(aa.get(Zhiduan.detail+"_"+Zhiduan.jieshao[0]));
//        private ArrayList<String> buzoutext;//步骤
        recipes.setBuzoutext(this.getList(Zhiduan.detail+"_"+Zhiduan.buzoutext[0],aa));
//        private ArrayList<String> buzhouimg;//步骤图片
        recipes.setBuzhouimg(this.getList(Zhiduan.detail+"_"+Zhiduan.buzouimg[0],aa));
//        private ArrayList<String> zhutitu;//主题图
        recipes.setZhutitu(this.getList(Zhiduan.detail+"_"+Zhiduan.zhutitu[0],aa));
//        private String nandu;//难度
        if(aa.get(Zhiduan.detail+"_"+Zhiduan.nandu[0])!=null)
        recipes.setNandu(aa.get(Zhiduan.detail+"_"+Zhiduan.nandu[0]));
//        private String renshu;//人数
        if(aa.get(Zhiduan.detail+"_"+Zhiduan.renshu[0])!=null)
        recipes.setRenshu(aa.get(Zhiduan.detail+"_"+Zhiduan.renshu[0]));
//        private String zhunbeitime;//准备时间
        if(aa.get(Zhiduan.pshijian[1]+"_"+Zhiduan.pshijian[0])!=null)
        recipes.setZhunbeitime(aa.get(Zhiduan.pshijian[1]+"_"+Zhiduan.pshijian[0]));
//        private String zhizuotime;//制作时间
        if(aa.get(Zhiduan.zshijian[1]+"_"+Zhiduan.zshijian[0])!=null)
        recipes.setZhizuotime(aa.get(Zhiduan.zshijian[1]+"_"+Zhiduan.zshijian[0]));

        Map<String,Map<String,String>> dtail=new HashMap<String, Map<String,String>>();
        Map<String,String>zhuliao=new HashMap<String, String>();
        int x=1;
        while(aa.get(Zhiduan.zhuliao[1]+"_"+Zhiduan.zhuliao[0]+x)!=null&&aa.get(Zhiduan.zhuliaozhong[1]+"_"+Zhiduan.zhuliaozhong[0]+x)!=null) {
            zhuliao.put(aa.get(Zhiduan.zhuliao[1] + "_" + Zhiduan.zhuliao[0] + x), aa.get(Zhiduan.zhuliaozhong[1] + "_" + Zhiduan.zhuliaozhong[0]+x));
            x++;
        }
        Map<String,String> pinglun=new HashMap<String, String>();
        x=1;
        while(aa.get(Zhiduan.pinglun[1]+"_"+Zhiduan.pinglun[0]+x)!=null&&aa.get(Zhiduan.pinglunid[1]+"_"+Zhiduan.pinglunid[0]+x)!=null) {
            pinglun.put(aa.get(Zhiduan.pinglunid[1] + "_" + Zhiduan.pinglunid[0] + x), aa.get(Zhiduan.pinglun[1] + "_" + Zhiduan.pinglun[0] + x));
            x++;
        }
        dtail.put("zhuliao",zhuliao);
        dtail.put("pinglun",pinglun);
        recipes.setDtail(dtail);
        return recipes;

    }

    /**
     * 通过表名和key获取一行数据
     * @param tableName
     * @param rowName
     * @return
     */
    public Map<String, String> get(String tableName, String rowName) {
        return hbaseTemplate.get(tableName, rowName,new RowMapper<Map<String,String>>(){
            public Map<String,String> mapRow(Result result, int rowNum) throws Exception {
                List<Cell> ceList =   result.listCells();
                Map<String,String> map = new HashMap<String, String>();
                if(ceList!=null&&ceList.size()>0){
                    for(Cell cell:ceList){
                        map.put(Bytes.toString(cell.getFamilyArray(),cell.getFamilyOffset(),cell.getFamilyLength())+
                                        "_"+Bytes.toString( cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength()),
                                Bytes.toString( cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                    }
                }
                return  map;
            }
        });
    }



    //实例化recipes的一些list用的
    public ArrayList<String> getList(String filme,Map<String,String> aa){
        int x=0;
        ArrayList<String> bb=new ArrayList<String>();
        while(aa.get(filme+x)!=null)
            bb.add(aa.get(filme+x++));
        return  bb;
    }

    /*
    * 功能：放入或者跟新n个数据
    *
    * 参数：
    *
    * 步骤：
    *
    * */
    public void addlist(List<String[]> zhiduan,List<String> value ,String rowkey){
        Assert.hasLength(rowkey);
        Assert.hasLength(zhiduan.get(1)[1]);
        Assert.hasLength(zhiduan.get(1)[0]);
        Assert.notNull(value.get(1));
        hbaseTemplate.execute(zhiduan.get(1)[2], new TableCallback<Object>() {
            public Object doInTable(HTableInterface htable) throws Throwable {
                Put put = (new Put(rowkey.getBytes(hbaseTemplate.getCharset())));
                for(int x=0;x<zhiduan.size();x++)
                    put.addColumn(zhiduan.get(x)[1].getBytes(hbaseTemplate.getCharset()),zhiduan.get(x)[0].getBytes(hbaseTemplate.getCharset()),value.get(x).getBytes(hbaseTemplate.getCharset()));
                htable.put(put);
                return null;
            }
        });
    }





    /**
     * 增加或更新某一个数据(cell)的
     * table表名
     * rowkey行键
     * family列族
     * column列
     *
     */
    public boolean updateCell(String []cell,String rowkey,String value){
        try {
            hbaseTemplate.execute(cell[2], new TableCallback<Object>() {

                public Object doInTable(HTableInterface htable) throws Throwable {
                    Put put = new Put(Bytes.toBytes(rowkey));

                    put.addColumn(cell[1].getBytes(hbaseTemplate.getCharset()), cell[0].getBytes(hbaseTemplate.getCharset()), value.getBytes(hbaseTemplate.getCharset()));
                    htable.put(put);
                    return rowkey;
                }
                });
        }
        catch (Exception e){
            System.out.println(e);
            System.out.println("HBaseServer的updateCell出错了");
            return false;
        }
        return true;
    }
    /*
     * 通过一些list来存数据(同一个表格)
     *
     *
     * */

    public void setlist(List<String[]> zhi,String rowkey,List<String> value){
        if(zhi.size()==value.size()&&zhi.size()>0){
            Assert.hasLength(rowkey);
            Assert.hasLength(zhi.get(0)[1]);
            Assert.hasLength(zhi.get(0)[0]);
            hbaseTemplate.execute(zhi.get(0)[2], new TableCallback<Object>() {
                public Object doInTable(HTableInterface htable) throws Throwable {
                    Put put = new Put(rowkey.getBytes(hbaseTemplate.getCharset()));
                    for(int x=0;x<zhi.size();x++)
                        put.addColumn(zhi.get(x)[1].getBytes(hbaseTemplate.getCharset()),zhi.get(x)[0].getBytes(hbaseTemplate.getCharset()),value.get(x).getBytes(hbaseTemplate.getCharset()));
                    htable.put(put);
                    return null;
                }
            });
        }
    }



//puttwe(Zhiduan.recipsename,Zhiduan.kouwei,"123","更改后名字","更改后的口味");

    //存一些需要改两个参数的数据用的,这个函数使用条件比较苛刻
    public void puttwe(String []cellone,String []celltwe,String rowkey,String valueone,String valuetwe) {

        Assert.hasLength(rowkey);
        Assert.hasLength(cellone[1]);
        Assert.hasLength(cellone[0]);
        Assert.notNull(valueone);
        hbaseTemplate.execute(cellone[2], new TableCallback<Object>() {
            public Object doInTable(HTableInterface htable) throws Throwable {
                Put put = (new Put(rowkey.getBytes(hbaseTemplate.getCharset())));
                put.addColumn(cellone[1].getBytes(hbaseTemplate.getCharset()),cellone[0].getBytes(hbaseTemplate.getCharset()),valueone.getBytes(hbaseTemplate.getCharset()));
                put.addColumn(celltwe[1].getBytes(hbaseTemplate.getCharset()),(celltwe[0]+valueone).getBytes(hbaseTemplate.getCharset()),valuetwe.getBytes(hbaseTemplate.getCharset()));

                htable.put(put);
                return null;
            }
        });
    }

    //辅助存菜谱的函数
    public Put putPut(Put put,List<String[]> zhiduan,List<String> values){
        if(put==null||zhiduan.size()!=values.size())return null;
        else {
            for(int x=0;x<zhiduan.size();x++){
                put.addColumn(zhiduan.get(x)[1].getBytes(hbaseTemplate.getCharset()),zhiduan.get(x)[0].getBytes(hbaseTemplate.getCharset()),(values.get(x)).getBytes(hbaseTemplate.getCharset()));
            }
        }
        return  put;
    }


    //存进去一个菜谱
    public void put(Hrecipes hrecipes) {
        this.hbaseTemplate.execute(Zhiduan.hrecipes, new TableCallback<Object>() {
            public Object doInTable(HTableInterface htable) throws Throwable {
                Put put = new Put(Bytes.toBytes(hrecipes.getRecipesid()));
                List<String[]> zhiduan=new ArrayList<String[]>();
                List<String> value=new ArrayList<String>();
//                private String caiming;//菜名
                zhiduan.add(Zhiduan.recipsename);
                value.add(hrecipes.getCaiming());
//                private String gongyi;//工艺
                zhiduan.add(Zhiduan.gongyi);
                value.add(hrecipes.getGongyi());
//                private String userid;//发菜谱的用户id
                zhiduan.add(Zhiduan.userid);
                value.add(hrecipes.getUserid());
//                private String kouwei;//口味
                zhiduan.add(Zhiduan.kouwei);
                value.add(hrecipes.getKouwei());
//                private String kaluli;//卡路里
                zhiduan.add(Zhiduan.kaluli);
                value.add(hrecipes.getKaluli());
//                private String tanshui;//碳水化合物
                zhiduan.add(Zhiduan.tanshui);
                value.add(hrecipes.getTanshui());
//                private String danbai;//蛋白质
                zhiduan.add(Zhiduan.danbai);
                value.add(hrecipes.getDanbai());
//                private ArrayList<String> biaoqian;//标签
                put=putList(put,hrecipes.getBiaoqian(),Zhiduan.prief,Zhiduan.biaoqian[0]);
//                private ArrayList<String> fuliao;//辅料
                put=putList(put,hrecipes.getFuliao(),Zhiduan.prief,Zhiduan.fuliao[0]);
//                private String pinglun0;//评论数量
                zhiduan.add(Zhiduan.pinglun0);
                value.add(hrecipes.getPinglun0());
//                private int liulan=0;//浏览量
                zhiduan.add(Zhiduan.pinglun0);
                value.add(hrecipes.getPinglun0());
//                private int shoucang=0;//收藏数量
                zhiduan.add(Zhiduan.shoucang);
                value.add(hrecipes.getShoucang()+"");
//                private int dianzan=0;//点赞数量
                zhiduan.add(Zhiduan.dianzan);
                value.add(hrecipes.getDianzan()+"");
//                private String fengxiang;//分享
                zhiduan.add(Zhiduan.fenxiang);
                value.add(hrecipes.getFengxiang()+"");
//                private float pingfen;//评分
                zhiduan.add(Zhiduan.pingfen);
                value.add(hrecipes.getPingfen()+"");
//                private String chuju;//厨具
                zhiduan.add(Zhiduan.chuju);
                value.add(hrecipes.getChuju()+"");
//                private String jieshao;//介绍
                zhiduan.add(Zhiduan.chuju);
                value.add(hrecipes.getJieshao()+"");
//                private ArrayList<String> buzoutext;//步骤
                put=putList(put,hrecipes.getBuzoutext(),Zhiduan.detail,Zhiduan.buzoutext[0]);
//                private ArrayList<String> buzhouimg;//步骤图片
                put=putList(put,hrecipes.getBuzhouimg(),Zhiduan.detail,Zhiduan.buzouimg[0]);
//                private ArrayList<String> zhutitu;//主题图
                put=putList(put,hrecipes.getZhutitu(),Zhiduan.detail,Zhiduan.zhutitu[0]);
//                private String nandu;//难度
                zhiduan.add(Zhiduan.nandu);
                value.add(hrecipes.getNandu()+"");
//                private String renshu;//人数
                zhiduan.add(Zhiduan.renshu);
                value.add(hrecipes.getRenshu()+"");
//                private String zhunbeitime;//准备时间
                zhiduan.add(Zhiduan.zshijian);
                value.add(hrecipes.getZhunbeitime());
//                private String zhizuotime;//制作时间
                zhiduan.add(Zhiduan.pshijian);
                value.add(hrecipes.getZhizuotime());
                for(int x=0;x<zhiduan.size();x++)
                    put.addColumn(zhiduan.get(x)[1].getBytes(hbaseTemplate.getCharset()),zhiduan.get(x)[0].getBytes(hbaseTemplate.getCharset()),value.get(x).getBytes(hbaseTemplate.getCharset()));
                Map<String,Map<String,String>> bb=hrecipes.getDtail();
                Map<String,String> zhuliao=bb.get("zhuliao");
                Map<String,String> pinglun=bb.get("pinglun");
                for(Map.Entry<String, String> entry : zhuliao.entrySet()){
                    String mapKey = entry.getKey();
                    String mapValue = entry.getValue();
                    put.addColumn(Zhiduan.detail.getBytes(hbaseTemplate.getCharset()),Zhiduan.zhuliao[0].getBytes(hbaseTemplate.getCharset()),mapKey.getBytes(hbaseTemplate.getCharset()));
                    put.addColumn(Zhiduan.detail.getBytes(hbaseTemplate.getCharset()),Zhiduan.zhuliaozhong[0].getBytes(hbaseTemplate.getCharset()),mapValue.getBytes(hbaseTemplate.getCharset()));
                }
                for(Map.Entry<String, String> entry : pinglun.entrySet()){
                    String mapKey = entry.getKey();
                    String mapValue = entry.getValue();
                    put.addColumn(Zhiduan.detail.getBytes(hbaseTemplate.getCharset()),Zhiduan.pinglunid[0].getBytes(hbaseTemplate.getCharset()),mapKey.getBytes(hbaseTemplate.getCharset()));
                    put.addColumn(Zhiduan.detail.getBytes(hbaseTemplate.getCharset()),Zhiduan.pinglun[0].getBytes(hbaseTemplate.getCharset()),mapValue.getBytes(hbaseTemplate.getCharset()));
                }
                htable.put(put);
                return null;
            }
        });
    }

    //put recipes的一些list用的
    public Put putList(Put put,ArrayList<String> aa,String filme,String quoo){
        for(int i = 0;i<aa.size();i++)put.addColumn(filme.getBytes(hbaseTemplate.getCharset()),(quoo+i).getBytes(hbaseTemplate.getCharset()),aa.get(i).getBytes(hbaseTemplate.getCharset()));
        return  put;
    }












    public  List<Map<String,Map<String, String>>> query(String tablename,String id){
        List<Map<String,Map<String, String>>> md = hbaseTemplate.find(tablename, id, (result, rowNum)->{
            Cell[] cells = result.rawCells();
            Map<String,Map<String, String>> data = new HashMap<>(16);
            for(Cell c : cells){
                String columnFamily = new String(CellUtil.cloneFamily(c));
                String rowName = new String(CellUtil.cloneQualifier(c));
                String value = new String(CellUtil.cloneValue(c));

                Map<String, String> obj = data.get(columnFamily);
                if(null == obj){
                    obj = new HashMap<>(16);
                }
                obj.put(rowName, value);
            }
            return data;
        });

        return md;
    }






    /**
     * 通过表名，开始行键和结束行键获取数据
     * @param tableName
     * @param startRow
     * @param stopRow
     * @return
     */
    public List<Map<String,Object>> find(String tableName , String startRow,String stopRow) {
        Scan scan = new Scan();
        if(startRow==null){
            startRow="";
        }
        if(stopRow==null){
            stopRow="";
        }
        scan.setStartRow(Bytes.toBytes(startRow));
        scan.setStopRow(Bytes.toBytes(stopRow));
        /* PageFilter filter = new PageFilter(5);
         scan.setFilter(filter);*/
        return  hbaseTemplate.find(tableName, scan,new RowMapper<Map<String,Object>>(){
            @Override
            public Map<String,Object> mapRow(Result result, int rowNum) throws Exception {
                List<Cell> ceList =   result.listCells();
                Map<String,Object> map = new HashMap<String,Object>();
                Map<String,Map<String,Object>> returnMap = new HashMap<String,Map<String,Object>>();
                String  row = "";
                if(ceList!=null&&ceList.size()>0){
                    for(Cell cell:ceList){
                        row =Bytes.toString( cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
                        String value =Bytes.toString( cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                        String family =  Bytes.toString(cell.getFamilyArray(),cell.getFamilyOffset(),cell.getFamilyLength());
                        String quali = Bytes.toString( cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength());
                        map.put(family+"_"+quali, value);
                    }
                    map.put("row",row );
                }
                return  map;
            }
        });
    }



}