package com.lesent.activiti.business.controller;

import com.lesent.activiti.business.service.HistoryBusiService;
import com.lesent.activiti.common.dto.ResEntity;
import com.lesent.activiti.common.dto.req.history.HistoryReq;
import com.lsdk.activiti.business.service.HistoryBusiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "history_controller 流程历史")
@RestController
@RequestMapping("/api/v1/history")
public class HistoryController {

    @Autowired
    private HistoryBusiService historyBusiService;

    @ApiOperation(value = "流程审批历史记录",httpMethod = "POST")
    @RequestMapping("/query")
    public ResEntity historicActiviti(@RequestBody HistoryReq req){
        return historyBusiService.historicActiviti(req);
    }

}
