package com.lesent.activiti.common.dto.res.history;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TaskAssign implements Serializable {

    private String taskId;

    private String assignee;

    private Date endTime;

}
