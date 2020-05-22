package com.lesent.activiti.common.dto.res.deploy;

import com.lesent.activiti.common.dto.IdVO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DeployResVO extends IdVO implements Serializable {

    /**
     * 流程Key
     */
    private String busiKey;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 分类
     */
    private String category;

    /**
     * 流程定义Id
     */
    private String processDefineId;

    /**
     * 发布状态
     */
    private Integer pubSts;

    /**
     * 流程图资源地址
     */
    private String resourcePath;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 修改人
     */
    private String updator;

    /**
     * 创建人
     */
    private String creator;

}
