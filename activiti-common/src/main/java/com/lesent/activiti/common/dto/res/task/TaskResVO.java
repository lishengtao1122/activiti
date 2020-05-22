package com.lesent.activiti.common.dto.res.task;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskResVO implements Serializable{

    private String instanceId;

    private String id;

    private boolean isClaim;

    private String name;

}
