package com.lesent.activiti.business.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author lesent
 * @since 2020-04-23
 */
@Data
@TableName("t_deploy")
public class TDeploy implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 业务流程控制器名称
     */
    private String application;

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


    @Override
    public String toString() {
        return "TDeploy{" +
                "id=" + id +
                ", busiKey=" + busiKey +
                ", name=" + name +
                ", pubSts=" + pubSts +
                ", resourcePath=" + resourcePath +
                ", version=" + version +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", updator=" + updator +
                ", creator=" + creator +
                "}";
    }
}
