package com.lesent.activiti.business.controller;

import com.lesent.activiti.business.service.RuntimeBusiService;
import com.lesent.activiti.business.service.TaskBusiService;
import com.lesent.activiti.common.dto.ResEntity;
import com.lesent.activiti.common.dto.req.task.*;
import com.lsdk.activiti.business.service.RuntimeBusiService;
import com.lsdk.activiti.business.service.TaskBusiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@Api(description = "task_controller 流程任务")
@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    @Autowired
    private TaskBusiService taskBusiService;

    @Autowired
    private RuntimeBusiService runtimeBusiService;

    @ApiOperation(value = "流程任务列表", httpMethod = "POST")
    @RequestMapping("/list")
    public ResEntity listTask(@RequestBody @Valid TaskListReq req){
        return taskBusiService.QueryCurrentTaskS(req);
    }

    @ApiOperation(value = "开启流程示例", httpMethod = "POST")
    @RequestMapping("/start")
    public ResEntity startInstance(@RequestBody @Valid StartInstanceReq req){
        return runtimeBusiService.startInstance(req);
    }

    @ApiOperation(value = "完成任务", httpMethod = "POST")
    @RequestMapping("/complete")
    public ResEntity complateTask(@RequestBody @Valid CompleteTaskReq req){
        return taskBusiService.completeTaskByTask(req);
    }

    @ApiOperation(value = "领取任务", httpMethod = "POST")
    @RequestMapping("/claim")
    public ResEntity claimTask(@RequestBody @Valid ClaimTaskReq req){
        return taskBusiService.claimTaskByTask(req);
    }

    @ApiOperation(value = "获取流程的当前审批任务", httpMethod = "GET")
    @ApiModelProperty(value = "instanceId 流程实例Id")
    @RequestMapping("/next/{instanceId}")
    public ResEntity findNextTasks(@PathVariable("instanceId") String instanceId){
        return taskBusiService.findNextStepTasks(instanceId);
    }

    @ApiOperation(value = "指定审批人", httpMethod = "POST")
    @RequestMapping("/assign")
    public ResEntity assignCandidate(@RequestBody @Valid AssignCandidateReq req){
        return taskBusiService.assignCandidate(req);
    }

    @ApiOperation(value = "指定任务的业务Key", httpMethod = "POST")
    @RequestMapping("/busiKey")
    public ResEntity editTaskBusiKey(@RequestBody @Valid EditTaskBusiKeyReq req){
        return taskBusiService.editTaskBusiKey(req);
    }

    @ApiOperation(value = "获取当前任务详情", httpMethod = "GET")
    @ApiModelProperty(value = "taskId 任务Id")
    @RequestMapping("/detail/{taskId}")
    public ResEntity listTask(@PathVariable("taskId") String taskId){
        return taskBusiService.taskDetail(taskId);
    }

    /**
     * 删除流程实例
     * @param instanceId
     * @return
     */
    @ApiOperation(value = "删除流程实例", httpMethod = "GET")
    @ApiModelProperty(value = "instanceId 任务实例Id")
    @RequestMapping("/delete/{instanceId}")
    public ResEntity deleteInstance(@PathVariable("instanceId") String instanceId){
        return taskBusiService.deleteInstance(instanceId);
    }

}
