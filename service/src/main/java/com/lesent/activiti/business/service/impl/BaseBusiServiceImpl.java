package com.lesent.activiti.business.service.impl;

import com.lesent.activiti.business.exception.ActivitiBusiException;
import com.lesent.activiti.common.enums.RestStsEnum;
import com.lesent.activiti.common.enums.TaskCorrectorTypeEnum;
import com.lsdk.activiti.business.exception.ActivitiBusiException;
import org.activiti.engine.*;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseBusiServiceImpl {

    @Autowired
    protected RepositoryService repositoryService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected HistoryService historyService;
    @Autowired
    protected RuntimeService runtimeService;

    protected void securityAssignCandidate(String taskId, TaskCorrectorTypeEnum correctorTypeEnum, String userOrGroupId) {
        try{
            switch (correctorTypeEnum){
                case ASSIGNE:
                    taskService.setAssignee(taskId,userOrGroupId);
                case CANDIDATEUSERS:
                    taskService.addCandidateUser(taskId,userOrGroupId);
                case CANDIDATEGROUP:
                    taskService.addCandidateGroup(taskId,userOrGroupId);
            }
        }catch (ActivitiObjectNotFoundException e){ //不存在
            throw new ActivitiBusiException("001","任务不存在");
        }
    }

    protected void securitySetBusiKey(String taskId, String busiKey) {
        TaskEntity task = (TaskEntity)taskService.createTaskQuery().taskId(taskId).singleResult();
        if(task == null)
            throw new ActivitiBusiException(RestStsEnum.TASK_NOT_EXIST);
        //设置task_busi
        try {
            taskService.setOwner(taskId,busiKey);
        }catch (ActivitiObjectNotFoundException  e){ //不存在
            throw new ActivitiBusiException("001","任务不存在");
        }
    }

}
