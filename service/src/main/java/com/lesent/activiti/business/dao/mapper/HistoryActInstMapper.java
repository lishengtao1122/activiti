package com.lesent.activiti.business.dao.mapper;

import com.lesent.activiti.common.dto.res.history.TaskAssign;
import com.lesent.activiti.common.dto.res.history.TaskBusiKey;
import com.lesent.activiti.common.dto.res.history.TaskComment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lesent
 * @since 2020-04-23
 */

public interface HistoryActInstMapper {

    @Select({"<script>",
            "SELECT TASK_ID_, ASSIGNEE_, END_TIME_ FROM act_hi_actinst ",
            "WHERE PROC_INST_ID_ = #{instanceId} ",
            "<when test='taskIds!=null and taskIds != \"\"'>",
            "AND TASK_ID_ in (${taskIds})",
            "</when>",
            "</script>"})
    List<TaskAssign> listTaskAssignee(@Param("instanceId") String instanceId,@Param("taskIds") String taskIds);

    @Select({"<script>",
            "SELECT ID_, NAME_ AS BUSI_KEY FROM act_hi_taskinst ",
            "WHERE PROC_INST_ID_ = #{instanceId} AND END_TIME_ IS NOT NULL",
            "<when test='busiKey != null and busiKey != \"\"'>",
            "AND NAME_ = #{busiKey}",
            "</when>",
            "order by ID_ asc",
            "</script>"
    })
    List<TaskBusiKey> queryByInstanceAndBusiKey(@Param("instanceId") String instanceId, @Param("busiKey") String busiKey);

    @Select({"<script>",
            "SELECT TASK_ID_, FULL_MSG_ AS comment FROM act_hi_comment ",
            "WHERE TASK_ID_ in (${taskIds}) ",
            "<when test='commentType != null and commentType != \"\"'>",
            "AND TYPE_ = #{commentType}",
            "</when>",
            "</script>"
    })
    List<TaskComment> queryCommentByTaskIds(@Param("taskIds")String taskIds,@Param("commentType")String commentType);

}
