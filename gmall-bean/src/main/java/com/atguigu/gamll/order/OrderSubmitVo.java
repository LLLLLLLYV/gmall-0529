package com.atguigu.gamll.order;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单提交需要的参数
 */
@Data
public class OrderSubmitVo implements Serializable{
    private Integer userAddressId;//用户地址的Id
    private String orderComment;//订单的备注

    private String token;//防止重复提交令牌
}
