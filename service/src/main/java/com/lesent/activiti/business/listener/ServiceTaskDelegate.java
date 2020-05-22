package com.lesent.activiti.business.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lesent.activiti.business.exception.ActivitiBusiException;
import com.lesent.activiti.business.service.ProcessControlRegistry;
import com.lesent.activiti.business.utils.SpringContextUtils;
import com.lesent.activiti.common.dto.res.listener.CallBackEntity;
import com.lesent.activiti.common.enums.ProcessControlEnum;
import com.lesent.activiti.common.enums.RestStsEnum;
import com.lesent.activiti.common.utils.HttpUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

//ServiceTask 的任务执行通知
public class ServiceTaskDelegate implements JavaDelegate {

    private static Logger logger = LoggerFactory.getLogger(TaskEndListener.class);

    private ProcessControlRegistry processControlRegistry;

//    private RabbitTemplate rabbitTemplate;

    public ServiceTaskDelegate(){
        this.processControlRegistry = SpringContextUtils.getBean("processControlRegistry",ProcessControlRegistry.class);
//        this.rabbitTemplate = SpringContextUtils.getBean("rabbitTemplate",RabbitTemplate.class);
    }


    @Override
    public void execute(DelegateExecution execution) {
        CallBackEntity callBack = processControlRegistry.getCallBackUrl(execution.getProcessDefinitionId(), ProcessControlEnum.SERVICE_TASK);
        logger.info(" ================== service-task callback fired ===================== execution:{} ",execution);
        if(callBack != null){
            Map<String,String> param = new HashMap<>();
            param.put("instanceId",execution.getProcessInstanceId());
            param.put("taskId",execution.getId());
            param.put("taskSts",execution.getCurrentFlowElement().getName());
            logger.info(" >>> callBack :{} <<< ",callBack);
            //同步请求处理delate
            String result = HttpUtils.sendPost(callBack.getCallBackUrl(),param);
            JSONObject object = JSON.parseObject(result);
            Integer code = object.getInteger("code");
            if(code == 200){ //状态成功
                logger.info(" >>> application:{} call url:{} success <<< ",callBack.getApplication(),callBack.getCallBackUrl());
                //做响应的任务,需求再收集
                return;
            }
            logger.info(" >>> application:{} call url:{} error:{} <<< ",callBack.getApplication(),callBack.getCallBackUrl(),result);
        }else {
            logger.info(" >>> processInstanceId:{} callBack:{} is null <<< ",execution.getRootProcessInstanceId(),callBack);
            throw new ActivitiBusiException(RestStsEnum.NOT_VALID_APPLICATION);
        }
    }
}
