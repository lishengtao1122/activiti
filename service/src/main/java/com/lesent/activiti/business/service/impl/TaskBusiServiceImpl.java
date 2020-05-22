package com.lesent.activiti.business.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lesent.activiti.common.dto.ResEntityUtils;
import com.lesent.activiti.common.dto.req.task.*;
import com.lesent.activiti.common.dto.req.task.query.ApprovalItem;
import com.lesent.activiti.common.dto.req.task.query.ApprovalItemsOr;
import com.lesent.activiti.common.dto.req.task.query.QueryVariable;
import com.lesent.activiti.common.dto.req.task.query.QueryVariableItem;
import com.lesent.activiti.common.enums.AndOrEnum;
import com.lesent.activiti.common.enums.RestStsEnum;
import com.lsdk.activiti.business.exception.ActivitiBusiException;
import com.lsdk.activiti.business.service.TaskBusiService;
import com.lesent.activiti.common.dto.ResEntity;
import com.lesent.activiti.common.dto.res.task.TaskResVO;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class TaskBusiServiceImpl extends BaseBusiServiceImpl implements TaskBusiService {

    private static final Logger logger = LoggerFactory.getLogger(TaskBusiServiceImpl.class);

    @Override
    public List<String> QueryCurrentTaskByInstance(String instanceId, String businessKey) {

        List<Task> taskList = taskService.createTaskQuery().processInstanceId(instanceId)
                .processInstanceBusinessKey(businessKey).orderByTaskId().asc().list();

        if (CollectionUtils.isNotEmpty(taskList)) { //多个任务
            return taskList.stream()
                    .map(Task::getId)
                    .collect(toList());
        }
        return null;
    }

    @Override
    public ResEntity<Page<TaskResVO>> QueryCurrentTaskS(TaskListReq req) {
        List<Task> taskList = null;
        TaskQuery taskQuery = taskService.createTaskQuery();
        if (StringUtils.isNotBlank(req.getKey()))
            taskQuery.processDefinitionKey(req.getKey());
        if (StringUtils.isNotBlank(req.getBusiKey()))
            taskQuery.processInstanceBusinessKeyLike(req.getKey());
        if (StringUtils.isNotBlank(req.getInstanceId()))
            taskQuery.processInstanceId(req.getInstanceId());
        if (CollectionUtils.isNotEmpty(req.getTaskBusis()))
            taskQuery.taskNameIn(req.getTaskBusis());
        if(req.getQueryVariable() != null){
            QueryVariable queryVariable = req.getQueryVariable();
            if(queryVariable.getAndOrEnum() == AndOrEnum.OR){
                taskQuery.or();
            }
            for (QueryVariableItem item:queryVariable.getQueryVariableItems()){
                switch (item.getOp()){
                    case EQ:
                        taskQuery.processVariableValueEquals(item.getKey(),item.getValue());
                        break;
                    case NO_EQ:
                        taskQuery.processVariableValueNotEquals(item.getKey(),item.getValue());
                        break;
                    case LESS_THAN:
                        taskQuery.processVariableValueLessThan(item.getKey(),item.getValue());
                        break;
                    case LESS_THAN_OR_EQUAL:
                        taskQuery.processVariableValueLessThanOrEqual(item.getKey(),item.getValue());
                        break;
                    case GREATER_THAN:
                        taskQuery.processVariableValueGreaterThan(item.getKey(),item.getValue());
                        break;
                    case GREATER_THAN_OR_EQUAL:
                        taskQuery.processVariableValueGreaterThanOrEqual(item.getKey(),item.getValue());
                        break;
                    case LIKE:
                        taskQuery.processVariableValueLike(item.getKey(),String.valueOf(item.getValue()));
                        break;
                }
            }
            if(queryVariable.getAndOrEnum() == AndOrEnum.OR){
                taskQuery.endOr();
            }
        }

        /*Map<String,String> variable = req.getVariable();
        if(variable != null && !variable.isEmpty()){
            for (Map.Entry<String,String> entry:variable.entrySet()){
                taskQuery.processVariableValueLike(entry.getKey(),entry.getValue());
            }
        }*/
        ApprovalItemsOr approvalItemsOr = req.getApprovalItemsOr();
        if(approvalItemsOr != null && CollectionUtils.isNotEmpty(approvalItemsOr.getApprovalItems())){
            List<ApprovalItem> approvalItemList = approvalItemsOr.getApprovalItems();
            taskQuery.or();
            for (ApprovalItem item:approvalItemList){
                if (item != null) {
                    switch (item.getCorrectorTypeEnum()) {
                        case ASSIGNE:
                            taskQuery.taskAssignee(item.getUserOrGroupId());
                            break;
                        case CANDIDATEGROUP:
                            taskQuery.taskCandidateGroup(item.getUserOrGroupId());
                            break;
                        case CANDIDATEUSERS:
                            taskQuery.taskCandidateUser(item.getUserOrGroupId());
                            break;
                    }
                }
            }
            taskQuery.endOr();
        }

        int firstResult = req.getPageNum() * req.getPageSize();

        long total = taskQuery.count();
        taskList = taskQuery.orderByTaskCreateTime().orderByTaskId().asc().listPage(firstResult,req.getPageSize());
        Page<TaskResVO> page = new Page<>(req.getPageNum(), req.getPageSize(), total, true);
        if (CollectionUtils.isNotEmpty(taskList)) { //多个任务
            page.setRecords(
                    taskList.stream()
                            .map(task -> {
                                TaskResVO vo = new TaskResVO();
                                BeanUtils.copyProperties(task, vo);
                                vo.setClaim(StringUtils.isBlank(task.getAssignee()));
                                vo.setInstanceId(task.getProcessInstanceId());
                                return vo;
                            }).collect(toList()));
        }
        return ResEntityUtils.success(page);
    }

    @Override
    public ResEntity claimTaskByTask(ClaimTaskReq req) {
        try {
            taskService.claim(req.getTaskId(), req.getUserId());
        } catch (ActivitiObjectNotFoundException e) { //不存在
            throw new ActivitiBusiException(RestStsEnum.TASK_NOT_EXIST);
        }
        return ResEntityUtils.success();
    }

//    @Transactional
    @Override
    public ResEntity completeTaskByTask(CompleteTaskReq req) {
        try {
            if(StringUtils.isNotBlank(req.getComment())){
                Comment comment = taskService.addComment(req.getTaskId(),null,req.getComment());
            }
            taskService.complete(req.getTaskId(), req.getVariable());
        } catch (ActivitiObjectNotFoundException e) { //不存在
            throw new ActivitiBusiException(RestStsEnum.TASK_NOT_EXIST);
        }
        return ResEntityUtils.success();
    }

    @Override
    public ResEntity assignCandidate(AssignCandidateReq req) {
        super.securityAssignCandidate(req.getTaskId(), req.getCorrectorTypeEnum(), req.getUserOrGroupId());
        return ResEntityUtils.success();
    }

    @Override
    public ResEntity<List<TaskResVO>> findNextStepTasks(String instanceId) {
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(instanceId).orderByTaskId().asc().list();
        if (CollectionUtils.isEmpty(taskList))
            return null;
        return ResEntityUtils.success(taskList.stream()
                .map(task -> {
                    TaskResVO vo = new TaskResVO();
                    vo.setId(task.getId());
                    vo.setName(task.getName());
                    return vo;
                }).collect(toList()));
    }

    //借用owner带busiKey
    @Override
    public ResEntity editTaskBusiKey(EditTaskBusiKeyReq req) {
        super.securitySetBusiKey(req.getTaskId(), req.getBusiKey());
        return ResEntityUtils.success();
    }

    @Override
    public ResEntity<TaskResVO> taskDetail(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(task == null)
            return ResEntityUtils.error(RestStsEnum.TASK_NOT_EXIST);
        TaskResVO vo = new TaskResVO();
        BeanUtils.copyProperties(task, vo);
        vo.setClaim(StringUtils.isBlank(task.getAssignee()));
        vo.setInstanceId(task.getProcessInstanceId());
        return ResEntityUtils.success(vo);
    }

    @Override
    public ResEntity deleteInstance(String instanceId) {
        try {
            runtimeService.deleteProcessInstance(instanceId,"用户主动删除");
        }catch (Exception e){
            logger.error(" >>> delete process by instanceId:{} error ! <<< ",instanceId,e);
            return ResEntityUtils.error(RestStsEnum.DELETE_PROCESS_ERROR.code,e.getMessage());
        }

        return ResEntityUtils.success();
    }


}
