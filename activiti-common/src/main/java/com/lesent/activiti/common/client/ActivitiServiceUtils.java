package com.lesent.activiti.common.client;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.lesent.activiti.common.constan.Constant;
import com.lesent.activiti.common.dto.ResEntity;
import com.lesent.activiti.common.dto.req.history.HistoryReq;
import com.lesent.activiti.common.dto.req.task.*;
import com.lesent.activiti.common.dto.res.history.HistoryTaskResVO;
import com.lesent.activiti.common.dto.res.task.TaskResVO;
import com.lesent.activiti.common.utils.PageUtils;
import com.lsdk.activiti.common.dto.req.task.*;
import com.lesent.activiti.common.utils.HttpUtils;
import lombok.Data;
import com.lsdk.activiti.common.constan.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "activiti.server")
@Data
public class ActivitiServiceUtils {

    private String startInstance;
    private String taskList;
    private String taskComplete;
    private String assignTask;
    private String nextTasks;
    private String setBusiKey;
    private String history;
    private String claimTask;
    private String userTasks;
    private String taskDetail;
    private String deleteInstance;

    //开启流程
    public ResEntity<String> startInstance(StartInstanceReq req){
        String json = JSONObject.toJSONString(req);
        String result = HttpUtils.sendPostForJSONString(startInstance,json);
        ResEntity<String> resEntity = JSONObject.parseObject(result,ResEntity.class);
        String data = JSONObject.parseObject(result).getObject("data",String.class);
        resEntity.setData(data);
        return resEntity;
    }

    //任务列表
    public ResEntity<PageUtils> taskList(TaskListReq req){
        String json = JSONObject.toJSONString(req);
        String result = HttpUtils.sendPostForJSONString(taskList,json);
        ResEntity<PageUtils> resEntity = JSONObject.parseObject(result,new TypeReference<ResEntity<PageUtils>>(){});

        JSONObject data = JSONObject.parseObject(result).getJSONObject("data");
        String records = data.getString("records");
        List<TaskResVO> taskResVOS = JSONObject.parseArray(records,  TaskResVO.class);
//        List<TaskResVO> taskResVOS = data.getJSONArray("records").toJavaList(TaskResVO.class);
        long totalCount = data.getLong("total");
        int pageSize = data.getInteger("size");
        int currentPage = data.getInteger("current");
        PageUtils pageUtils = new PageUtils(taskResVOS,(int)totalCount,pageSize,currentPage);
        resEntity.setData(pageUtils);
        return resEntity;
    }

    //下一节点任务
    public ResEntity<List<TaskResVO>> nextTasks(String instanceId){
        String result = HttpUtils.sendGet(nextTasks+ Constant.SYMBOL_SLASH + instanceId,null);
        ResEntity<List<TaskResVO>> resEntity = JSONObject.parseObject(result,ResEntity.class);
        List<TaskResVO> data = JSONObject.parseObject(result).getJSONArray("data").toJavaList(TaskResVO.class);
        resEntity.setData(data);
        return resEntity;
    }

    //完成任务
    public ResEntity taskComplete(CompleteTaskReq req){
        String json = JSONObject.toJSONString(req);
        String result = HttpUtils.sendPostForJSONString(taskComplete,json);
        ResEntity resEntity = JSONObject.parseObject(result,ResEntity.class);
        return resEntity;
    }

    //设置业务Key
    public ResEntity setBusiKey(EditTaskBusiKeyReq req){
        String json = JSONObject.toJSONString(req);
        String result = HttpUtils.sendPostForJSONString(setBusiKey,json);
        ResEntity resEntity = JSONObject.parseObject(result,ResEntity.class);
        return resEntity;
    }

    //指定审批人
    public ResEntity assignTask( AssignCandidateReq req){
        String json = JSONObject.toJSONString(req);
        String result = HttpUtils.sendPostForJSONString(assignTask,json);
        ResEntity resEntity = JSONObject.parseObject(result,ResEntity.class);
        return resEntity;
    }

    //选取任务
    public ResEntity claimTask(ClaimTaskReq req){
        String json = JSONObject.toJSONString(req);
        String result = HttpUtils.sendPostForJSONString(claimTask,json);
        ResEntity resEntity = JSONObject.parseObject(result,ResEntity.class);
        return resEntity;
    }

    //获取流程定义的UserTasks
    public ResEntity<List<TaskResVO>> userTaskS(String processDefineId){
        String result = HttpUtils.sendGet(userTasks+Constant.SYMBOL_SLASH+processDefineId,null);
        ResEntity<List<TaskResVO>> resEntity = JSONObject.parseObject(result,ResEntity.class);
        String dataStr = JSONObject.parseObject(result).getString("data");
        if(!StringUtils.isEmpty(dataStr)){
            List<TaskResVO> list = JSONObject.parseArray(dataStr,  TaskResVO.class);
            resEntity.setData(list);
        }
        return resEntity;
    }

    //查询审批记录
    public ResEntity<List<HistoryTaskResVO>> historicActiviti(HistoryReq req){
        String json = JSONObject.toJSONString(req);
        String result = HttpUtils.sendPostForJSONString(history,json);
        ResEntity<List<HistoryTaskResVO>> resEntity = JSONObject.parseObject(result,ResEntity.class);
        String dataStr = JSONObject.parseObject(result).getString("data");
        if(!StringUtils.isEmpty(dataStr)){
            List<HistoryTaskResVO> list = JSONObject.parseArray(dataStr,  HistoryTaskResVO.class);
            resEntity.setData(list);
        }
        return resEntity;
    }


    //根据任务Id获取任务信息
    public ResEntity<TaskResVO> taskDetail(String taskId){
        String result = HttpUtils.sendGet(taskDetail+ Constant.SYMBOL_SLASH + taskId,null);
        ResEntity<TaskResVO> resEntity = JSONObject.parseObject(result,ResEntity.class);
        String data = JSONObject.parseObject(result).getString("data");
        if(!StringUtils.isEmpty(data)){
            TaskResVO vo = JSONObject.parseObject(data,TaskResVO.class);
            resEntity.setData(vo);
        }
        return resEntity;
    }

    //根据任务Id获取任务信息
    public ResEntity<TaskResVO> deleteInstance(String instanceId){
        String result = HttpUtils.sendGet(deleteInstance+ Constant.SYMBOL_SLASH + instanceId,null);
        return JSONObject.parseObject(result,ResEntity.class);
    }

    public static void main(String[] args){
        String result = "{\"code\":\"200\",\"msg\":\"成功\",\"data\":[{\"taskId\":\"682826\",\"approver\":\"190\",\"busiKey\":\"举证\",\"commonts\":[\"举证\"],\"dueDate\":\"2020-05-15 18:32:59\"},{\"taskId\":\"682831\",\"approver\":\"66\",\"busiKey\":\"分案\",\"commonts\":[\"分案\"],\"dueDate\":\"2020-05-15 18:34:27\"},{\"taskId\":\"682840\",\"approver\":\"190\",\"busiKey\":\"待审核\",\"commonts\":[\"审核通过\"],\"dueDate\":\"2020-05-15 18:35:33\"},{\"taskId\":\"682846\",\"approver\":\"190\",\"busiKey\":\"待报案\",\"commonts\":null,\"dueDate\":\"2020-05-15 18:38:32\"},{\"taskId\":\"682852\",\"approver\":\"190\",\"busiKey\":\"待开庭\",\"commonts\":null,\"dueDate\":\"2020-05-15 18:44:17\"},{\"taskId\":\"682858\",\"approver\":\"190\",\"busiKey\":\"撤诉\",\"commonts\":[\"撤诉失败\"],\"dueDate\":\"2020-05-19 14:17:17\"},{\"taskId\":\"683017\",\"approver\":\"190\",\"busiKey\":\"待执行\",\"commonts\":null,\"dueDate\":\"2020-05-19 14:19:32\"}],\"success\":true}";
        ResEntity<List<HistoryTaskResVO>> resEntity = JSONObject.parseObject(result,ResEntity.class);
        String dataStr = JSONObject.parseObject(result).getString("data");
        if(!StringUtils.isEmpty(dataStr)){
            List<HistoryTaskResVO> list = JSONObject.parseArray(dataStr,  HistoryTaskResVO.class);
            resEntity.setData(list);
        }
    }

}
