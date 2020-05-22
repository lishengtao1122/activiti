package com.lesent.activiti.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lesent.activiti.common.dto.ResEntity;
import com.lesent.activiti.common.dto.req.task.*;
import com.lesent.activiti.common.dto.res.task.TaskResVO;

import java.util.List;

public interface TaskBusiService {

    //任务查询:实例查询
    List<String> QueryCurrentTaskByInstance(String instanceId, String businessKey);

    //任务查询:实例查询 + 执行人
    ResEntity<Page<TaskResVO>> QueryCurrentTaskS(TaskListReq req);

    //任务领取
    ResEntity claimTaskByTask(ClaimTaskReq req);

    //任务执行 + 变量
    ResEntity completeTaskByTask(CompleteTaskReq req);

    //指定执行人
    ResEntity assignCandidate(AssignCandidateReq req);

    //获取下一执行任务节点
    ResEntity<List<TaskResVO>> findNextStepTasks(String instanceId);

    //为task设置busi_key
    ResEntity editTaskBusiKey(EditTaskBusiKeyReq req);

    //task的详情
    ResEntity<TaskResVO> taskDetail(String taskId);

    //删除实例
    ResEntity deleteInstance(String instanceId);
}
