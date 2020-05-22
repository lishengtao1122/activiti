package com.lesent.activiti.common.dto.res.history;

import lombok.Data;

import java.io.Serializable;
import java.sql.Blob;

@Data
public class TaskComment implements Serializable {

    private String taskId;

    //new String(blob.getBytes(1, (int)blob.length()))
    private byte[] comment;
}
