//Json封装体CommentResult,传给前端，判断编码是否成功，成功才显示
package com.example.demo.bean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


//



@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {  //泛型：如果装的payment 返回payment,装的order 返回order

    //请求状态
    private Integer code;
    //请求内容
    private String message;
    //请求里放一个类
    private T data;

    public CommonResult(Integer code,String message){
        this(code,message,null);
    }
}