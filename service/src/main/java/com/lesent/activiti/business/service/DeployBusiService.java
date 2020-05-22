package com.lesent.activiti.business.service;

import com.lesent.activiti.common.dto.ResEntity;
import com.lesent.activiti.common.dto.req.deploy.DeployListReq;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface DeployBusiService {

    InputStream generateProcessDiagram(String instanceId);

    //添加流程文件
    ResEntity saveProcess(MultipartFile file, String busiKey, String name, String category, String application);

    //发布流程
    ResEntity publishProcess(Integer processId);

    //关闭流程
    ResEntity unPublishProcess(Integer processId);

    //删除流程
    ResEntity deleteProcess(Integer processId);

    //流程列表
    ResEntity listProcess(DeployListReq req);

    //获取流程图的userTask
    ResEntity listUserTaskS(String processDefineId);
}
