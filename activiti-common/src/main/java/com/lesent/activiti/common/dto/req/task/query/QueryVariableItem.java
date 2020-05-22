package com.lesent.activiti.common.dto.req.task.query;

import com.lesent.activiti.common.enums.QueryOPEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryVariableItem implements Serializable{

    private String key;

    private QueryOPEnum op;

    private Object value;

}
