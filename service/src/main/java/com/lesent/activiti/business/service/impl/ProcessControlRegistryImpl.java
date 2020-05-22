package com.lesent.activiti.business.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.lesent.activiti.business.dao.entity.TDeploy;
import com.lesent.activiti.business.exception.ActivitiBusiException;
import com.lsdk.activiti.business.dao.entity.TDeploy;
import com.lsdk.activiti.business.dao.service.ITDeployService;
import com.lesent.activiti.common.enums.ProcessControlEnum;
import com.lesent.activiti.common.enums.RestStsEnum;
import com.lsdk.activiti.business.exception.ActivitiBusiException;
import com.lsdk.activiti.business.service.ProcessControlRegistry;
import com.lesent.activiti.common.dto.res.listener.CallBackEntity;
import com.lesent.activiti.common.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service("processControlRegistry")
public class ProcessControlRegistryImpl implements ProcessControlRegistry {

    private Map<String,Map<ProcessControlEnum,String>> processRegisty = new ConcurrentHashMap<>();

    private Map<String,AtomicInteger> failureMap = new ConcurrentHashMap<>();

    @Autowired
    private ITDeployService deployService;

    public boolean registry(String application,String type, String urlV){
        Integer control = Integer.valueOf(type);
        ProcessControlEnum controlEnum = ProcessControlEnum.values()[control];
        if(controlEnum == null || StringUtils.isBlank(urlV)){
            throw new ActivitiBusiException(RestStsEnum.PARAM_VALID);
        }
        //过滤参数
        /*Map<ProcessControlEnum,String> processControlEnumStringMap = new HashMap<>();
        for (Map.Entry<String,String> process:processValue.entrySet()){
            Integer key = Integer.valueOf(process.getKey());
            ProcessControlEnum processControlEnum = ProcessControlEnum.values()[key];
            if(StringUtils.isNotBlank(process.getValue())){
                processControlEnumStringMap.put(processControlEnum,process.getValue());
            }
        }*/



        if(processRegisty.containsKey(application)){
            Map<ProcessControlEnum,String> controlEnumStringMap = processRegisty.get(application);
            controlEnumStringMap.put(controlEnum,urlV);
        }else {
            Map<ProcessControlEnum,String> controlEnumStringMap = new HashMap<>();
            controlEnumStringMap.put(controlEnum,urlV);
            processRegisty.put(application,controlEnumStringMap);
        }
        return Boolean.TRUE;
    }

    public Map<ProcessControlEnum,String> getProcessFromRegistry(String application){
        return processRegisty.get(application);
    }

    public boolean containKey(String application){
        return processRegisty.containsKey(application);
    }

    @Override
    public CallBackEntity getCallBackUrl(String processDefineId, ProcessControlEnum controlEnum) {

        List<TDeploy> deployS = deployService.list(new QueryWrapper<TDeploy>().eq("process_define_id",processDefineId));

        if(CollectionUtils.isEmpty(deployS))
            return null;

        String application = deployS.get(0).getApplication();

        Map<ProcessControlEnum,String> urlMap = processRegisty.get(application);

        if(urlMap == null || urlMap.isEmpty())
            return null;

        if(StringUtils.isBlank(urlMap.get(controlEnum)))
            return null;

        return new CallBackEntity(application,urlMap.get(controlEnum));
    }

    @Scheduled(cron = "0 * * * * *")
    public void heartBeat(){
        new HeartBeatProcessControl().run();
    }

    @Scheduled(cron = "0 * * * * *")
    public void initFailure(){
        failureMap.clear();
        for (Map.Entry<String,Map<ProcessControlEnum,String>> process:processRegisty.entrySet()){
            failureMap.put(process.getKey(),new AtomicInteger(0));
        }
    }



    //heartBeat
    class HeartBeatProcessControl extends Thread {

        private final Logger logger = LoggerFactory.getLogger(HeartBeatProcessControl.class);

        @Override
        public void run() {
            logger.info(" >>> start exec heartbeat now:{} ",new Date());
            if(processRegisty.size() > 0){
                //不为空,筛选出来heartbeat
                Map<String,String> heartMap = new HashMap<>();
                for (Map.Entry<String,Map<ProcessControlEnum,String>> process:processRegisty.entrySet()){
                    String application = process.getKey();
                    Map<ProcessControlEnum,String> urlMap = process.getValue();
                    String heartUrl = urlMap.get(ProcessControlEnum.HEART_BEAT);
                    heartMap.put(application,heartUrl);
                }
                if(heartMap.size() > 0){
                    for (Map.Entry<String,String> heartEntry:heartMap.entrySet()){
                        Map<String,String> param = new HashMap<>();
                        param.put("status","0"); //0：状态良好
                        String result = HttpUtils.sendGet(heartEntry.getValue(),param);
                        if(StringUtils.isNotBlank(result)){
                            JSONObject object = JSON.parseObject(result);
                            Integer code = object.getInteger("code");
//                            if(code != 200){ //状态不良处理
//                                if(failureMap.get(heartEntry.getKey()).addAndGet(1) > 10){
//                                    processRegisty.remove(heartEntry.getKey());
//                                }
//                            }
                        }
                    }
                }
            }
            logger.info(" >>> end exec heartbeat now:{} ",new Date());
        }
    }




}
