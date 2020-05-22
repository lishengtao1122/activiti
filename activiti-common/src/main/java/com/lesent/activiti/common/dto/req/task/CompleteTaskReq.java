package com.lesent.activiti.common.dto.req.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

@ApiModel("完成审批任务")
@Data
public class CompleteTaskReq implements Serializable {

    @ApiModelProperty(value = "任务Id",required = true)
    @NotNull(message = "任务Id不能为空")
    private String taskId;

    @ApiModelProperty(value = "流程变量")
    private Map<String,Object> variable;

    //审批备注内容
    @ApiModelProperty(value = "审批备注内容")
    private String comment;

}
