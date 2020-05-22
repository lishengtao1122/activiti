package com.lesent.activiti.common.enums;

//流程审批备注类型
public enum CommentTypeEnum {

    CONMENT(0,"comment"),

    EVENT(1,"event"),

    ;

    private final Integer code;

    private String commentType;

    CommentTypeEnum(Integer code,String commentType){
        this.code = code;
        this.commentType = commentType;
    }

    //code获取 Enum Instance
    public static CommentTypeEnum instanceByCode(Integer code){
        if(code == null)
            return null;
        for (CommentTypeEnum var:values()){
            if(code == var.code)
                return var;
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getCommentType() {
        return commentType;
    }
}
