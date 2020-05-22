package com.lesent.activiti.common.dto.req.deploy;

import com.lesent.activiti.common.dto.req.PageReq;
import com.lesent.activiti.common.enums.PubStsEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("流程列表查询")
@Data
public class DeployListReq extends PageReq implements Serializable {

    //流程发布状态
    @ApiModelProperty(value = "流程发布状态 0:未发布 1:已发布 2:暂停使用")
    private PubStsEnum stsEnum;

    @ApiModelProperty(value = "流程名称")
    private String name;

    @ApiModelProperty(value = "节点名称")
    private String busiKey;

    @ApiModelProperty(value = "流程分类")
    private String category;



}
