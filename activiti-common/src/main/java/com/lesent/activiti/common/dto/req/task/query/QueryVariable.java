package com.lesent.activiti.common.dto.req.task.query;

import com.lesent.activiti.common.enums.AndOrEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryVariable implements Serializable {

    private AndOrEnum andOrEnum;

    private List<QueryVariableItem> queryVariableItems;

}
