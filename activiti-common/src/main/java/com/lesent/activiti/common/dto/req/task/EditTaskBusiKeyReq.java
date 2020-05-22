package com.lesent.activiti.common.dto.req.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel("指定任务的业务Key")
@Data
public class EditTaskBusiKeyReq implements Serializable {

    @ApiModelProperty(value = "任务Id",required = true)
    @NotNull(message = "任务Id不能为空")
    private String taskId;
    @ApiModelProperty(value = "任务BusiKey",required = true)
    @NotNull(message = "任务BusiKey不能为空")
    private String busiKey;

}
