package com.lesent.activiti.business.service;

import com.lesent.activiti.common.dto.ResEntity;
import com.lesent.activiti.common.dto.req.task.StartInstanceReq;

import java.util.Map;

public interface RuntimeBusiService {

    //开启流程

    ResEntity startInstance(StartInstanceReq req);

    String startInstance(String key, String businessKey, Map<String, Object> variable);

    //判断流程是否结束
    boolean instanceEnd(String instance);


}
