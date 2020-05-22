package com.lesent.activiti.common.dto.req.task;

import lombok.Data;

import java.io.Serializable;

@Data
public class KeyValue implements Serializable{

    private String key;

    private Object value;

}
