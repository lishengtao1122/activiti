package com.lesent.activiti.common.dto.req.task;

import com.lesent.activiti.common.enums.TaskCorrectorTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("指定审批人")
@Data
public class AssignCandidateReq implements Serializable {

    @ApiModelProperty(value = "任务Id",required = true)
    private String taskId;
    @ApiModelProperty(value = "审批人指定类型: 0 ASSIGNE 1 CANDIDATEUSERS 2 CANDIDATEGROUP ",required = true)
    private TaskCorrectorTypeEnum correctorTypeEnum;
    @ApiModelProperty(value = "审批人员/组",required = true)
    private String userOrGroupId;

}
