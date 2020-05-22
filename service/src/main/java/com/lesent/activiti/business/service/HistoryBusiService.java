package com.lesent.activiti.business.service;

import com.lesent.activiti.common.dto.ResEntity;
import com.lesent.activiti.common.dto.req.history.HistoryReq;
import com.lesent.activiti.common.dto.res.history.HistoryTaskResVO;

import java.util.List;

public interface HistoryBusiService {

    ResEntity<List<HistoryTaskResVO>> historicActiviti(HistoryReq req);


}
