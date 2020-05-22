package com.lesent.activiti.common.dto.req.deploy;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeployAddReq extends BaseDeployReq implements Serializable {

    private byte[] file;

}
