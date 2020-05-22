package com.lesent.activiti.business.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.lesent.activiti.business.dao.mapper.HistoryActInstMapper;
import com.lesent.activiti.business.exception.ActivitiBusiException;
import com.lesent.activiti.business.service.HistoryBusiService;
import com.lesent.activiti.business.utils.DateUtils;
import com.lsdk.activiti.business.dao.mapper.HistoryActInstMapper;
import com.lesent.activiti.common.enums.CommentTypeEnum;
import com.lesent.activiti.common.enums.RestStsEnum;
import com.lsdk.activiti.business.exception.ActivitiBusiException;
import com.lsdk.activiti.business.service.HistoryBusiService;
import com.lsdk.activiti.business.utils.DateUtils;
import com.lesent.activiti.common.dto.ResEntity;
import com.lesent.activiti.common.dto.ResEntityUtils;
import com.lesent.activiti.common.dto.req.history.HistoryReq;
import com.lesent.activiti.common.dto.res.history.HistoryTaskResVO;
import com.lesent.activiti.common.dto.res.history.TaskAssign;
import com.lesent.activiti.common.dto.res.history.TaskBusiKey;
import com.lesent.activiti.common.dto.res.history.TaskComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class HistoryBusiServiceImpl extends BaseBusiServiceImpl implements HistoryBusiService {

    @Autowired
    private HistoryActInstMapper historyActInstMapper;

    @Override
    public ResEntity<List<HistoryTaskResVO>> historicActiviti(HistoryReq req) {

        if(req == null)
            throw new ActivitiBusiException(RestStsEnum.PARAM_VALID);
        CommentTypeEnum typeEnum = null;
        if(req.getCommentType() != null){
            typeEnum = CommentTypeEnum.instanceByCode(req.getCommentType());
            if(typeEnum == null){
                throw new ActivitiBusiException(RestStsEnum.PARAM_VALID);
            }
        }else {
            typeEnum = CommentTypeEnum.CONMENT;
        }

        //查询taskIds
        List<TaskBusiKey> taskBusiKeyList = historyActInstMapper.queryByInstanceAndBusiKey(req.getInstanceId(),req.getBusiKey());

        if(CollectionUtils.isEmpty(taskBusiKeyList))
            return ResEntityUtils.success();

        List<String> taskIds = taskBusiKeyList.stream().map(TaskBusiKey::getId).collect(Collectors.toList());

        //查询审批人
        List<TaskAssign> taskAssignList = historyActInstMapper.listTaskAssignee(req.getInstanceId(),String.join(",", taskIds));

        Map<String,TaskAssign> taskAssignMap = null;

        if(CollectionUtils.isNotEmpty(taskAssignList)){
            taskAssignMap = taskAssignList.stream().collect(Collectors.toMap(TaskAssign::getTaskId, Function.identity()));
        }
        //查询审批意见
        List<TaskComment> taskCommentList = historyActInstMapper.queryCommentByTaskIds(String.join(",", taskIds),typeEnum.getCommentType());

        Map<String,List<TaskComment>> taskCommontMap = null;

        if(CollectionUtils.isNotEmpty(taskCommentList)){
            taskCommontMap = taskCommentList.stream().collect(Collectors.groupingBy(TaskComment::getTaskId));
        }

        List<HistoryTaskResVO> taskResVOList = new ArrayList<>();

        for (TaskBusiKey taskBusiKey:taskBusiKeyList){

            HistoryTaskResVO vo = new HistoryTaskResVO();
            vo.setTaskId(taskBusiKey.getId());
            vo.setBusiKey(taskBusiKey.getBusiKey());

            //审批人
            if(taskAssignMap != null && !taskAssignMap.isEmpty()){
                TaskAssign taskAssign = taskAssignMap.get(taskBusiKey.getId());
                if(taskAssign != null){
                    vo.setApprover(taskAssign.getAssignee());
                    vo.setDueDate(DateUtils.parseDate_yyyyMMddHHmmss(taskAssign.getEndTime()));
                }
            }

            //审批意见
            if(taskCommontMap != null && !taskCommontMap.isEmpty()){
                List<TaskComment> taskComments = taskCommontMap.get(taskBusiKey.getId());
                if(CollectionUtils.isNotEmpty(taskComments)){
                    List<String> comments = new ArrayList<>();
                    for (TaskComment taskComment:taskComments){
                        if(taskComment.getComment() != null){
                            try {
                                comments.add(new String(taskComment.getComment(),"UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    vo.setCommonts(comments);
                }
            }
            taskResVOList.add(vo);
        }
        return ResEntityUtils.success(taskResVOList);
    }
}
