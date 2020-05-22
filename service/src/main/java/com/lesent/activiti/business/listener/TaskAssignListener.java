package com.lesent.activiti.business.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lesent.activiti.common.dto.res.listener.CallBackEntity;
import com.lesent.activiti.common.enums.ProcessControlEnum;
import com.lesent.activiti.common.enums.TaskCorrectorTypeEnum;
import com.lsdk.activiti.business.service.ProcessControlRegistry;
import com.lsdk.activiti.business.utils.SpringContextUtils;
import com.lesent.activiti.common.utils.HttpUtils;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;


//监听流程控制器指定审批人员
public class TaskAssignListener implements TaskListener {

    private static Logger logger = LoggerFactory.getLogger(TaskAssignListener.class);

    private ProcessControlRegistry processControlRegistry;

    private Executor executor;

    public TaskAssignListener(){
        executor = SpringContextUtils.getBean("activiti",Executor.class);
        processControlRegistry = SpringContextUtils.getBean("processControlRegistry",ProcessControlRegistry.class);
    }

    @Override
    public void notify(DelegateTask delegateTask) {

        CallBackEntity callBack = processControlRegistry.getCallBackUrl(delegateTask.getProcessDefinitionId(), ProcessControlEnum.ASSIGN_LISTENER);

        if(callBack == null){

            Map<String,String> param = new HashMap<>();

            param.put("instanceId",delegateTask.getProcessInstanceId());
            param.put("busiKey",delegateTask.getOwner());

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    String result = HttpUtils.sendPost(callBack.getCallBackUrl(),param);
                    JSONObject object = JSON.parseObject(result);
                    Integer code = object.getInteger("code");
                    if(code == 200){ //状态成功
                        JSONObject data = object.getJSONObject("data");
                        Integer correctorType = data.getInteger("correctorType");
                        String user = data.getString("user");
                        TaskCorrectorTypeEnum b = TaskCorrectorTypeEnum.values()[correctorType];
                        switch (b){
                            case ASSIGNE:
                                delegateTask.setAssignee(user);
                                break;
                            case CANDIDATEUSERS:
                                delegateTask.addCandidateUser(user);
                                break;
                            case CANDIDATEGROUP:
                                delegateTask.addCandidateGroup(user);
                                break;
                        }
                        logger.info(" >>> application:{} call url:{} success <<< ",callBack.getApplication(),callBack.getCallBackUrl());
                        return;
                    }
                    logger.info(" >>> application:{} call url:{} error:{} <<< ",callBack.getApplication(),callBack.getCallBackUrl(),result);
                }
            });
        }


    }
}
