package com.lesent.activiti.common.dto;


import com.lesent.activiti.common.enums.RestStsEnum;

public class ResEntityUtils {

    //成功：有返回值
    public static <T> ResEntity<T> success(T data){
        return new ResEntity<T>("200","成功",data);
    }

    //成功：无返回值
    public static <T> ResEntity<T> success(){
        return success(null);
    }

    //失败：无返回值
    public static <T> ResEntity<T> error(String code, String msg){
        return error(code,msg,null);
    }

    //失败：无返回值
    public static <T> ResEntity<T> error(RestStsEnum stsEnum){
        return error(stsEnum.code,stsEnum.msg,null);
    }

    //失败：有返回值
    public static <T> ResEntity<T> error(String code,String msg,T data){
        return new ResEntity<T>(code,msg,data);
    }

    private ResEntityUtils(){}

}
