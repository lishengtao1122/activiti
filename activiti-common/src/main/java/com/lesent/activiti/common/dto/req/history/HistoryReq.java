package com.lesent.activiti.common.dto.req.history;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("流程审批历史记录查询")
@Data
public class HistoryReq implements Serializable {

    @ApiModelProperty(value = "instanceId 流程实例Id",required = true)
    private String instanceId;

    private String busiKey;

    @ApiModelProperty(value = "commentType 审批记录类型 0 审批记录",required = true)
    private Integer commentType;

}
