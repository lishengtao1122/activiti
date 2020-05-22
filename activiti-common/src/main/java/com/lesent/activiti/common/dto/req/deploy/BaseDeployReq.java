package com.lesent.activiti.common.dto.req.deploy;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseDeployReq implements Serializable {

    private String name;
    //唯一
    private String key;

    private String category;

    private String tenantId;

}
