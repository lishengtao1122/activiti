package com.lesent.activiti.common.dto.req.task;

import com.lesent.activiti.common.dto.req.PageReq;
import com.lesent.activiti.common.dto.req.task.query.QueryVariable;
import com.lesent.activiti.common.dto.req.task.query.ApprovalItemsOr;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("任务列表")
@Data
public class TaskListReq extends PageReq {

    @ApiModelProperty(value = "流程的Key")
    private String key;
    @ApiModelProperty(value = "流程实例的Id")
    private String instanceId;
    @ApiModelProperty(value = "流程业务Key")
    private String busiKey;
    @ApiModelProperty(value = "流程节点的名称集合")
    private List<String> taskBusis;
    @ApiModelProperty(value = "流程变量查询")
    private QueryVariable queryVariable;
    @ApiModelProperty(value = "流程审批人员查询")
    private ApprovalItemsOr approvalItemsOr;

}
