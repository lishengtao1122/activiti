package com.lesent.activiti.business.controller;

import com.lesent.activiti.common.dto.ResEntity;
import com.lesent.activiti.common.dto.ResEntityUtils;
import com.lesent.activiti.common.enums.RestStsEnum;
import com.lsdk.activiti.business.service.ProcessControlRegistry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("业务应用注册流程引擎")
@RestController
@RequestMapping("/api/v1/registry")
public class ProcessRegistryController {

    @Autowired
    private ProcessControlRegistry registry;

    @ApiOperation(value = "业务应用注册流程引擎回调url",httpMethod = "POST")
    @RequestMapping
    public ResEntity registry(@ApiParam(value = "业务应用名称(和发布流程时填入的应用名称相同)",required = true) String application
            ,@ApiParam(value = "回调url类型 0:心跳 1:结束时间的监听 3:指定审批人的监听 4:service-task的监听" ,required = true) String type
            ,@ApiParam(value = "回调的url" ,required = true) String urlV){
        if(StringUtils.isBlank(application) || StringUtils.isBlank(type) || StringUtils.isBlank(urlV))
            return ResEntityUtils.error(RestStsEnum.PARAM_VALID);
        if(!registry.registry(application,type,urlV))
            return ResEntityUtils.error(RestStsEnum.FAILURE);
        return ResEntityUtils.success();
    }



}
