package com.lesent.activiti.common.dto.res.listener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallBackEntity implements Serializable {

    private String application;

    private String callBackUrl;

}
