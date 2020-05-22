package com.lesent.activiti.business.exception;

import com.lesent.activiti.common.enums.RestStsEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * ins 异常：业务异常
 */
@Data
public class ActivitiBusiException extends RuntimeException implements Serializable{

    private String code; //业务码

    private String msg; //说明

    private ActivitiBusiException(){}

    public ActivitiBusiException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public ActivitiBusiException(RestStsEnum stsEnum){
        super(stsEnum.msg);
        this.code = stsEnum.code;
        this.msg = stsEnum.msg;
    }

}
