package com.lesent.activiti.common.dto.req.task.query;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ApprovalItemsOr implements Serializable{

    private List<ApprovalItem> approvalItems;

}
