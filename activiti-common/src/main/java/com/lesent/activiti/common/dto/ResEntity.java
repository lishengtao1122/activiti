package com.lesent.activiti.common.dto;

import com.lesent.activiti.common.enums.RestStsEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 相应dto
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResEntity<T> implements Serializable{
    private String code; //业务码
    private String msg;  //业务码说明

    private T data;  //数据

    public boolean isSuccess(){
        return RestStsEnum.SUCCESS.code.equalsIgnoreCase(this.code);
    }

}
