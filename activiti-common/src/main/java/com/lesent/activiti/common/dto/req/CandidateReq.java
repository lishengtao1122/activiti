package com.lesent.activiti.common.dto.req;

import com.lesent.activiti.common.enums.TaskCorrectorTypeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class CandidateReq implements Serializable{

    private TaskCorrectorTypeEnum correctorTypeEnum;
    private String userOrGroupId;

}
