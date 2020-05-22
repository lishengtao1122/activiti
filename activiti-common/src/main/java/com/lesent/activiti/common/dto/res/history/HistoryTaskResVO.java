package com.lesent.activiti.common.dto.res.history;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HistoryTaskResVO implements Serializable {

    private String taskId;

    private String approver;

    private String busiKey;

    private List<String> commonts;

    private String dueDate;

}
