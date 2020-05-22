package com.lesent.activiti.common.enums;

//响应码
public enum RestStsEnum {

    SUCCESS("200","SUCCESS") , //成功
    FAILURE("500","FAILURE"), //系统错误

    PARAM_VALID("100","param valid"), //参数错误


    FILE_VALID("0000","process file error"), //流程文件错误

    PROCESS_NOT_EXIST("0001","process not exist"), //流程文件不存在

    PUBLISH_PROCESS_ERROR("0002","publish process err"), //发布流程文件失败

    PROCESS_HAS_PUBLISHED("0003","process has published"), //流程文件已发布

    PROCESS_NOT_PUBLISHED("0004","process not published"), //流程文件未发布

    UPDATE_PROCESS_STS_ERROR("0005","update process state error"), //更新流程状态失败

    TASK_NOT_EXIST("0006","task not exist"), //任务不存在

    DELETE_PROCESS_ERROR("0007","delete task error"), //删除流程示例错误

    NOT_VALID_APPLICATION("0008","not valid application service-task callback url"),


    ;

    public final String code; //业务码

    public final String msg;  //业务码说明

    RestStsEnum(String code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
