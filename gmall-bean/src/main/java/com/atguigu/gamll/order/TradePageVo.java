package com.atguigu.gamll.order;

import com.atguigu.gamll.cart.CartItem;
import com.atguigu.gamll.user.UserAddress;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class TradePageVo implements Serializable{
    private List<UserAddress> userAddresses;

    private List<CartItem> cartItems;

    private BigDecimal totalPrice;

}
