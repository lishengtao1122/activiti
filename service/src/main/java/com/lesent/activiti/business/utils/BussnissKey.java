package com.lesent.activiti.business.utils;

import com.lesent.activiti.common.constan.Constant;
import lombok.Getter;

@Getter
public class BussnissKey {

    private String vals;

    public BussnissKey(String... keys){
        StringBuilder sb = new StringBuilder();
        int lenth = keys.length;
        for (int i = 0;i < lenth;i++){
            sb.append(keys[i]);
            if(i != lenth - 1){
                sb.append(Constant.SYMBOL_COLON);
            }
        }
        vals = sb.toString();
    }

}
