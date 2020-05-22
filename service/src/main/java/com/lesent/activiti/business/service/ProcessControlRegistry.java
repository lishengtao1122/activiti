package com.lesent.activiti.business.service;

import com.lesent.activiti.common.enums.ProcessControlEnum;
import com.lesent.activiti.common.dto.res.listener.CallBackEntity;

import java.util.Map;

//流程控制器 注册
public interface ProcessControlRegistry {

    boolean registry(String application, String type, String urlV);

    Map<ProcessControlEnum,String> getProcessFromRegistry(String application);

    boolean containKey(String application);

    //根据 processDefineId 和 ProcessControlEnum 获取回调url
    CallBackEntity getCallBackUrl(String processDefineId, ProcessControlEnum controlEnum);
}
