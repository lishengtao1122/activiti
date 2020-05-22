package com.lesent.activiti.common.dto.req.task;

import com.lesent.activiti.common.dto.req.CandidateReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@ApiModel("开启流程")
@Data
public class StartInstanceReq implements Serializable {

    @ApiModelProperty(value = "流程Key",required = true)
    @NotNull(message = "流程Key不能为空")
    private String key;

    //指定第节点的审批人，无则不需审核
    @ApiModelProperty("指定第节点的审批人，无则不需审核")
    private HashMap<String,CandidateReq> candidateMap;

    //指定流程的总busiKey
    @ApiModelProperty("指定流程的总busiKey")
    private String businessKey;

    //指定节点的business Key
    @ApiModelProperty(value = "指定节点的business Key")
    private HashMap<String,String> nodeBusiKeyMap;

    //流程变量
    @ApiModelProperty(value = "流程变量")
    private Map<String,Object> variable;


}
