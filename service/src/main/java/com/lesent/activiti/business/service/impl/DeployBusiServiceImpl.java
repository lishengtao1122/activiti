package com.lesent.activiti.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lesent.activiti.common.constan.Constant;
import com.lsdk.activiti.business.dao.entity.TDeploy;
import com.lsdk.activiti.business.dao.service.ITDeployService;
import com.lesent.activiti.common.enums.PubStsEnum;
import com.lesent.activiti.common.enums.RestStsEnum;
import com.lsdk.activiti.business.exception.ActivitiBusiException;
import com.lsdk.activiti.business.service.DeployBusiService;
import com.lsdk.activiti.business.utils.OSSClientUtilLsdk;
import com.lesent.activiti.common.dto.ResEntity;
import com.lesent.activiti.common.dto.ResEntityUtils;
import com.lesent.activiti.common.dto.req.deploy.DeployListReq;
import com.lesent.activiti.common.dto.res.deploy.DeployResVO;
import com.lesent.activiti.common.dto.res.task.TaskResVO;
import org.activiti.bpmn.model.*;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class DeployBusiServiceImpl extends BaseBusiServiceImpl implements DeployBusiService {

    private static Logger logger = LoggerFactory.getLogger(DeployBusiServiceImpl.class);

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private ITDeployService deployService;

    @Autowired
    private OSSClientUtilLsdk ossClientUtilLsdk;

    @Value("${activiti.file.path}")
    private String filePath;

    @Override
    public ResEntity saveProcess(MultipartFile file, String busiKey, String name, String category,String application) {
        TDeploy deploy = new TDeploy();
        deploy.setBusiKey(busiKey);
        deploy.setName(name);
        deploy.setCategory(category);
        deploy.setApplication(application);
        //文件处理,ossClient
        String fileUrl;
        try {
            fileUrl = ossClientUtilLsdk.uploadByByteToOSS(file.getBytes(),file.getOriginalFilename(),filePath);
        } catch (IOException e) {
            return ResEntityUtils.error(RestStsEnum.FILE_VALID);
        }
        deploy.setResourcePath(fileUrl);

        Date nowTime = new Date();

        deploy.setCreateDate(nowTime);
        deploy.setUpdateDate(nowTime);

        deploy.setPubSts(PubStsEnum.UN_PUBLISH.ordinal());

        deployService.save(deploy);

        return ResEntityUtils.success(deploy.getId());
    }

    @Override
    public ResEntity publishProcess(Integer processId) {
        TDeploy deploy = deployService.getById(processId);
        String deployMentId = "";
        if(deploy == null)
            throw new ActivitiBusiException(RestStsEnum.PROCESS_NOT_EXIST);
        if(deploy.getPubSts() == PubStsEnum.UN_PUBLISH.ordinal()){
            InputStream in = null;
            try{
                //发布 oss获取资源文件
                in = ossClientUtilLsdk.downloadFile(deploy.getResourcePath());
                Deployment deployment = repositoryService.createDeployment()
                        .name(deploy.getName())
                        .key(deploy.getBusiKey())
                        .addInputStream(deploy.getBusiKey()+ Constant.SYMBOL_POINT+Constant.PROCESS_SUFFIX,in)
                        .deploy();
                deployMentId = deployment.getId();
            }catch (Exception e){
                logger.error(" >>> get file from oss error <<< ",e);
                throw new ActivitiBusiException(RestStsEnum.PUBLISH_PROCESS_ERROR);
            }finally {
                if (in != null){
                    try{
                        in.close();
                    }catch (IOException e){
                        logger.error(" close input stream error ",e);
                    }
                }
            }
        } else if (deploy.getPubSts() == PubStsEnum.SUSPOND.ordinal()){
            try{
                repositoryService.activateProcessDefinitionById(deploy.getProcessDefineId());
            }catch (ActivitiObjectNotFoundException e){ //不存在
                throw new ActivitiBusiException(RestStsEnum.PROCESS_NOT_EXIST);
            }
        }else {
            throw new ActivitiBusiException(RestStsEnum.PROCESS_HAS_PUBLISHED);
        }

        TDeploy upo = new TDeploy();
        upo.setId(deploy.getId());
        upo.setPubSts(PubStsEnum.PUBLISHED.ordinal());
        upo.setVersion(deploy.getVersion() + 1);

        if(deploy.getPubSts() == PubStsEnum.UN_PUBLISH.ordinal()){
            //查询流程定义的Key
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployMentId)
                    .latestVersion().latestVersion().singleResult();
            upo.setProcessDefineId(processDefinition.getId());
        }

        if(!deployService.updateById(upo)){
            logger.error(" >>> update deploy:{} sts by Id err <<< ",upo);
            //更新流程状态失败
            throw new ActivitiBusiException(RestStsEnum.UPDATE_PROCESS_STS_ERROR);
        }
        return ResEntityUtils.success(upo.getId());
    }

    @Override
    public ResEntity unPublishProcess(Integer processId) {
        TDeploy deploy = deployService.getById(processId);
        if(deploy == null)
            throw new ActivitiBusiException(RestStsEnum.PROCESS_NOT_EXIST);
        if(deploy.getPubSts() == PubStsEnum.UN_PUBLISH.ordinal())
            throw new ActivitiBusiException(RestStsEnum.PROCESS_NOT_PUBLISHED);

        try{
            repositoryService.suspendProcessDefinitionById(deploy.getProcessDefineId());
        }catch (ActivitiObjectNotFoundException e){ //不存在
            throw new ActivitiBusiException(RestStsEnum.PROCESS_NOT_EXIST);
        }

        //更改流程状态
        TDeploy upo = new TDeploy();
        upo.setId(processId);
        upo.setPubSts(PubStsEnum.SUSPOND.ordinal());
        upo.setVersion(deploy.getVersion() + 1);
        if(!deployService.updateById(upo)){
            logger.error(" >>> update deploy:{} sts by Id err <<< ",upo);
            //更新流程状态失败
            throw new ActivitiBusiException(RestStsEnum.UPDATE_PROCESS_STS_ERROR);
        }
        return ResEntityUtils.success();
    }

    @Override
    public ResEntity deleteProcess(Integer processId) {
        TDeploy deploy = deployService.getById(processId);
        if(deploy == null)
            throw new ActivitiBusiException(RestStsEnum.PROCESS_NOT_EXIST);
        repositoryService.deleteDeployment(deploy.getProcessDefineId());
        return ResEntityUtils.success();
    }

    @Override
    public ResEntity<Page<DeployResVO>> listProcess(DeployListReq req) {
        //分页查询
        Page<TDeploy> page = new Page<>(req.getPageNum(),req.getPageSize());
        Wrapper<TDeploy> queryWrapper = constructQueryWrapper(req);
        page = deployService.page(page,queryWrapper);
        Page<DeployResVO> resPage = new Page<>();
        BeanUtils.copyProperties(page, resPage);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            resPage.setRecords(
                    page.getRecords()
                            .stream()
                            .map(deploy -> {
                                DeployResVO res = new DeployResVO();
                                BeanUtils.copyProperties(deploy, res);
                                return res;
                            }).collect(toList())
            );
        }
        return ResEntityUtils.success(resPage);
    }

    @Override
    public ResEntity listUserTaskS(String processDefineId) {
        BpmnModel model = repositoryService.getBpmnModel(processDefineId);
        if(model == null)
            throw new ActivitiBusiException(RestStsEnum.PROCESS_NOT_EXIST);
        List<FlowElement> flowElementList = (List<FlowElement>) model.getMainProcess().getFlowElements();
        if(CollectionUtils.isEmpty(flowElementList))
            return ResEntityUtils.success();
        List<FlowElement> userTaskList = flowElementList.stream()
                .filter(flowElement -> {
                    if(flowElement instanceof UserTask)
                        return true;
                    else return false;
                }).collect(toList());
        if(CollectionUtils.isEmpty(userTaskList))
            return ResEntityUtils.success();
        List<TaskResVO> taskResVOS = userTaskList.stream()
                .map(flowElement -> {
                    UserTask userTask = (UserTask)flowElement;
                    TaskResVO resVO = new TaskResVO();
                    BeanUtils.copyProperties(userTask,resVO);
                    return resVO;
                }).collect(toList());
        return ResEntityUtils.success(taskResVOS);
    }

    private Wrapper<TDeploy> constructQueryWrapper(DeployListReq req) {
        QueryWrapper<TDeploy> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(req.getName()))
            queryWrapper.like("name",req.getName()+Constant.SYMBOL_PERCENT);
        if(StringUtils.isNotBlank(req.getBusiKey()))
            queryWrapper.eq("busi_key",req.getBusiKey());
        if(StringUtils.isNotBlank(req.getCategory()))
            queryWrapper.eq("category",req.getCategory());
        if(req.getStsEnum() != null)
            queryWrapper.eq("pub_sts",req.getStsEnum().ordinal());
        return queryWrapper;
    }

    @Override
    public InputStream generateProcessDiagram(String instanceId) {
        //  获取历史流程实例
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(instanceId).singleResult();

        if (historicProcessInstance == null) {
            throw new RuntimeException("获取流程实例ID[" + instanceId + "]对应的历史流程实例失败！");
        } else {
            // 获取流程定义
            ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                    .getDeployedProcessDefinition(historicProcessInstance.getProcessDefinitionId());

            // 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
            List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(instanceId).orderByHistoricActivityInstanceId().asc().list();

            // 已执行的节点ID集合
            List<String> executedActivityIdList = new ArrayList<String>();
            int index = 1;
            //logger.info("获取已经执行的节点ID");
            for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
                executedActivityIdList.add(activityInstance.getActivityId());

                logger.info("第[" + index + "]个已执行节点=" + activityInstance.getActivityId() + " : " + activityInstance.getActivityName());
                index++;
            }

            BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());

            // 已执行的线集合
            List<String> flowIds = new ArrayList<String>();
            // 获取流程走过的线 (getHighLightedFlows是下面的方法)
            flowIds = getHighLightedFlows(bpmnModel, processDefinition, historicActivityInstanceList);

            // 获取流程图图像字符流
            ProcessDiagramGenerator pec = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
            //配置字体
            InputStream imageStream = pec.generateDiagram(bpmnModel, "png", executedActivityIdList, flowIds, "宋体", "微软雅黑", "黑体", null, 2.0);
            return imageStream;
        }
    }

    private List<String> getHighLightedFlows(BpmnModel bpmnModel, ProcessDefinitionEntity processDefinitionEntity, List<HistoricActivityInstance> historicActivityInstances) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //24小时制
        List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId

        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {
            // 对历史流程节点进行遍历
            // 得到节点定义的详细信息
            FlowNode activityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(i).getActivityId());

            List<FlowNode> sameStartTimeNodes = new ArrayList<FlowNode>();// 用以保存后续开始时间相同的节点
            FlowNode sameActivityImpl1 = null;

            HistoricActivityInstance activityImpl_ = historicActivityInstances.get(i);// 第一个节点
            HistoricActivityInstance activityImp2_;

            for (int k = i + 1; k <= historicActivityInstances.size() - 1; k++) {
                activityImp2_ = historicActivityInstances.get(k);// 后续第1个节点

                if (activityImpl_.getActivityType().equals("userTask") && activityImp2_.getActivityType().equals("userTask") &&
                        df.format(activityImpl_.getStartTime()).equals(df.format(activityImp2_.getStartTime()))) //都是usertask，且主节点与后续节点的开始时间相同，说明不是真实的后继节点
                {

                } else {
                    sameActivityImpl1 = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(k).getActivityId());//找到紧跟在后面的一个节点
                    break;
                }

            }
            sameStartTimeNodes.add(sameActivityImpl1); // 将后面第一个节点放在时间相同节点的集合里
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点

                if (df.format(activityImpl1.getStartTime()).equals(df.format(activityImpl2.getStartTime()))) {// 如果第一个节点和第二个节点开始时间相同保存
                    FlowNode sameActivityImpl2 = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityImpl2.getActivityId());
                    sameStartTimeNodes.add(sameActivityImpl2);
                } else {// 有不相同跳出循环
                    break;
                }
            }
            List<SequenceFlow> pvmTransitions = activityImpl.getOutgoingFlows(); // 取出节点的所有出去的线

            for (SequenceFlow pvmTransition : pvmTransitions) {// 对所有的线进行遍历
                FlowNode pvmActivityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(pvmTransition.getTargetRef());// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }
        }
        return highFlows;

    }
}
