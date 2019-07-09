package com.atguigu.gmall.manager.order;

import java.io.IOException;
import java.util.List;

public interface OrderService {
    /**
     * 创建防重复交易的token
     * @return
     */
    String creatTradeToken();

    /**
     * 验证防重复交易的token
     * @param token
     * @return
     */
    boolean verfyToken(String token);

    /**
     *验证该用户订单里的商品的库存是否足够
     * @param userId
     * @return
     */
    List<String> verfyStock(Integer userId) throws IOException;
}
