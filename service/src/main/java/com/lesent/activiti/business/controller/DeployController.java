package com.lesent.activiti.business.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsdk.activiti.business.service.DeployBusiService;
import com.lesent.activiti.common.dto.ResEntity;
import com.lesent.activiti.common.dto.req.deploy.DeployListReq;
import com.lesent.activiti.common.dto.res.deploy.DeployResVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;

@Api(description = "deploy_controller 流程部署管理")
@RestController
@RequestMapping("/api/v1/deploy")
public class DeployController {

    private static final Logger logger = LoggerFactory.getLogger(DeployController.class);

    @Autowired
    private DeployBusiService deployBusiService;

    /**
     * 上传部署文件
     * @param file
     * @param busiKey
     * @param name
     * @param category
     * @return
     */
    @ApiOperation(value = "部署流程", httpMethod = "POST")
    @RequestMapping("/add")
    public ResEntity addProcess(@ApiParam(value = "bpmn流程文件",type = "file") MultipartFile file
            ,@ApiParam(value = "流程文件process的Id")String busiKey
            ,@ApiParam(value = "流程名称")String name
            ,@ApiParam(value = "流程分类（非必填）")String category
            ,@ApiParam(value = "流程所属应用名称")String application){
        return deployBusiService.saveProcess(file,busiKey,name,category,application);
    }

    /**
     * 发布流程
     * @param processId
     * @return
     */
    @ApiOperation(value = "发布流程", httpMethod = "GET")
    @ApiModelProperty(value = "processId 流程实例Id")
    @RequestMapping("/publish/{processId}")
    public ResEntity publishProcess(@PathVariable("processId") Integer processId){
        return deployBusiService.publishProcess(processId);
    }

    /**
     * 取消发布流程
     * @param processId
     * @return
     */
    @ApiOperation(value = "取消发布流程", httpMethod = "GET")
    @ApiModelProperty(value = "processId 流程实例Id")
    @RequestMapping("/unpublish/{processId}")
    public ResEntity unPublishProcess(@PathVariable("processId") Integer processId){
        return deployBusiService.unPublishProcess(processId);
    }

    /**
     * 删除流程
     * @param processId
     * @return
     */
    @ApiOperation(value = "删除流程", httpMethod = "GET")
    @ApiModelProperty(value = "processId 流程实例Id")
    @RequestMapping("/delete/{processId}")
    public ResEntity deleteProcess(@PathVariable("processId") Integer processId){
        return deployBusiService.deleteProcess(processId);
    }

    /**
     * 查看流程节点
     * @param processDefineId
     * @return
     */
    @ApiOperation(value = "查看流程节点", httpMethod = "GET")
    @ApiModelProperty(value = "processDefineId 流程实例定义Id")
    @RequestMapping("/userTasks/{processDefineId}")
    public ResEntity processUserTaskS(@PathVariable("processDefineId") String processDefineId){
        return deployBusiService.listUserTaskS(processDefineId);
    }

    /**
     * 部署流程
     */
    @RequestMapping("/list")
    @ApiOperation(value = "部署流程列表", httpMethod = "POST")
    public ResEntity<Page<DeployResVO>> listDeploy(@RequestBody @Valid DeployListReq req){
        return deployBusiService.listProcess(req);
    }

    /**
     * 查看当前流程图片
     * @param instanceId
     */
    @ApiOperation(value = "查看当前流程图片", httpMethod = "GET")
    @ApiModelProperty(value = "processDefineId 流程实例定义Id")
    @RequestMapping(value = "/flow-chat/{instanceId}",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] flowChat(@PathVariable("instanceId") String instanceId) throws IOException {
        InputStream inputStream = deployBusiService.generateProcessDiagram(instanceId);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
    }


}
