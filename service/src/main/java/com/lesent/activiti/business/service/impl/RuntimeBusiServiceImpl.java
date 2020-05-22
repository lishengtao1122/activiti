package com.lesent.activiti.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.lesent.activiti.business.dao.entity.TDeploy;
import com.lsdk.activiti.business.dao.entity.TDeploy;
import com.lsdk.activiti.business.dao.service.ITDeployService;
import com.lesent.activiti.common.enums.RestStsEnum;
import com.lsdk.activiti.business.exception.ActivitiBusiException;
import com.lsdk.activiti.business.service.RuntimeBusiService;
import com.lesent.activiti.common.dto.ResEntity;
import com.lesent.activiti.common.dto.ResEntityUtils;
import com.lesent.activiti.common.dto.req.CandidateReq;
import com.lesent.activiti.common.dto.req.task.StartInstanceReq;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RuntimeBusiServiceImpl extends BaseBusiServiceImpl implements RuntimeBusiService {


    @Autowired
    private ITDeployService deployService;


    @Override
    public ResEntity startInstance(StartInstanceReq req) {
        List<TDeploy> deploys = deployService.list(new QueryWrapper<TDeploy>().eq("busi_key", req.getKey()));
        if (CollectionUtils.isEmpty(deploys))
            throw new ActivitiBusiException(RestStsEnum.PROCESS_NOT_EXIST);
        ProcessInstance processInstance = null;
        try {
            processInstance = runtimeService.startProcessInstanceByKey(req.getKey(),req.getVariable());
        } catch (ActivitiObjectNotFoundException e) { //不存在
            throw new ActivitiBusiException("001", "不存在");
        }
        if (req.getNodeBusiKeyMap() != null && !req.getCandidateMap().isEmpty()) {
            List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).orderByTaskId().asc().list();
            taskList.stream()
                    .forEach(task -> {
                        if (!req.getCandidateMap().isEmpty()) {
                            CandidateReq candidate = req.getCandidateMap().get(task.getName());
                            if (candidate != null) {
                                super.securityAssignCandidate(task.getId(), candidate.getCorrectorTypeEnum(), candidate.getUserOrGroupId());
                            }
                        }
                        if (!req.getNodeBusiKeyMap().isEmpty()) {
                            String busiKey = req.getNodeBusiKeyMap().get(task.getName());
                            if (StringUtils.isNotBlank(busiKey)) {
                                super.securitySetBusiKey(task.getId(), busiKey);
                            }
                        }
                    });
        }

        return ResEntityUtils.success(processInstance.getId());
    }


    @Override
    public String startInstance(String key, String businessKey, Map<String, Object> variable) {
        ProcessInstance processInstance = null;
        try {
            processInstance = runtimeService.startProcessInstanceByKey(key, businessKey,variable);
        } catch (ActivitiObjectNotFoundException e) { //不存在
            throw new ActivitiBusiException("001", "不存在");
        }
        return processInstance.getProcessInstanceId();
    }


    @Override
    public boolean instanceEnd(String instanceId) {
        HistoricProcessInstance hpi = historyService   //与执行法的历史信息相关的Service
                .createHistoricProcessInstanceQuery()   //创建流程实例的历史信息查询对象
                .processInstanceId(instanceId)   //查询条件 -- 通过流程实例查询历史信息
                .singleResult();    //返回查询结果
        if (hpi == null) {
            throw new ActivitiBusiException("001", "不存在");
        }
        return hpi.getEndTime() != null;
    }
}
