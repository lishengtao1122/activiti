package com.lesent.activiti.business.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lesent.activiti.business.service.ProcessControlRegistry;
import com.lesent.activiti.business.utils.SpringContextUtils;
import com.lesent.activiti.common.dto.res.listener.CallBackEntity;
import com.lesent.activiti.common.enums.ProcessControlEnum;
import com.lsdk.activiti.business.service.ProcessControlRegistry;
import com.lsdk.activiti.business.utils.SpringContextUtils;
import com.lesent.activiti.common.utils.HttpUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;


//结束流程的监听（用来有业务需要监听结束任务）
public class TaskEndListener implements ExecutionListener {

    private static Logger logger = LoggerFactory.getLogger(TaskEndListener.class);

    private ProcessControlRegistry processControlRegistry;

    private Executor executor;

    public TaskEndListener(){
        executor = SpringContextUtils.getBean("activiti",Executor.class);
        processControlRegistry = SpringContextUtils.getBean("processControlRegistry",ProcessControlRegistry.class);
    }



    @Override
    public void notify(DelegateExecution delegateTask) {
        CallBackEntity callBack = processControlRegistry.getCallBackUrl(delegateTask.getProcessDefinitionId(), ProcessControlEnum.END_LISTENER);

        if(callBack != null){

            Map<String,String> param = new HashMap<>();

            param.put("instanceId",delegateTask.getProcessInstanceId());

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    String result = HttpUtils.sendPost(callBack.getCallBackUrl(),param);
                    JSONObject object = JSON.parseObject(result);
                    Integer code = object.getInteger("code");
                    if(code == 200){ //状态成功
                        logger.info(" >>> application:{} call url:{} success <<< ",callBack.getApplication(),callBack.getCallBackUrl());
                        return;
                    }
                    logger.info(" >>> application:{} call url:{} error:{} <<< ",callBack.getApplication(),callBack.getCallBackUrl(),result);
                }
            });
        }

    }
}
