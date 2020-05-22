package com.lesent.activiti.common.dto.req.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel("领取任务")
@Data
public class ClaimTaskReq implements Serializable {

    @ApiModelProperty(value = "任务Id",required = true)
    @NotNull(message = "任务Id不能为空")
    private String taskId;

    @NotNull(message = "指定审批人Id不能为空")
    @ApiModelProperty(value = "指定审批人Id",required = true)
    private String userId;

}
