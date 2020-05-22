package com.lesent.activiti.common.dto.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageReq implements Serializable{

    private int pageNum = 0;

    private int pageSize = 10;

}
