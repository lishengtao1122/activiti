package com.lesent.activiti.common.dto.req.task.query;

import com.lesent.activiti.common.enums.QueryOPEnum;
import com.lesent.activiti.common.enums.TaskCorrectorTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalItem implements Serializable{

    private TaskCorrectorTypeEnum correctorTypeEnum;

    private QueryOPEnum op;

    private String userOrGroupId;

}
