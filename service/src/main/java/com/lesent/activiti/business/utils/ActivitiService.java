package com.lesent.activiti.business.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lesent.activiti.common.utils.HttpUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "activiti.server")
@Data
public class ActivitiService {

    private String startInstance;
    private String taskList;
    private String taskComplete;
    private String assignTask;
    private String nextTasks;
    private String setBusiKey;
    private String history;
    private String claimTask;

    //开启流程
    public JSONObject startInstance(JSONObject param){
        String result = HttpUtils.sendPostForJSON(startInstance,param);
        return JSON.parseObject(result);
    }

    //任务列表
    public JSONObject taskList(JSONObject param){
        String result = HttpUtils.sendPostForJSON(taskList,param);
        return JSON.parseObject(result);
    }

    //下一节点任务
    public JSONObject nextTasks(JSONObject param){
        String result = HttpUtils.sendPostForJSON(nextTasks,param);
        return JSON.parseObject(result);
    }

    //完成任务
    public JSONObject taskComplete(JSONObject param){
        String result = HttpUtils.sendPostForJSON(taskComplete,param);
        return JSON.parseObject(result);
    }

    //设置业务Key
    public JSONObject setBusiKey(JSONObject param){
        String result = HttpUtils.sendPostForJSON(setBusiKey,param);
        return JSON.parseObject(result);
    }

    //指定审批人
    public JSONObject assignTask(JSONObject param){
        String result = HttpUtils.sendPostForJSON(assignTask,param);
        return JSON.parseObject(result);
    }

    //选取任务
    public JSONObject claimTask(JSONObject param){
        String result = HttpUtils.sendPostForJSON(claimTask,param);
        return JSON.parseObject(result);
    }

}
