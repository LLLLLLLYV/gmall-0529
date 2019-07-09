package com.atguigu.gamll.cart;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
public class CartVo implements Serializable{
    @Setter
    private List<CartItem> cartItem;

    private BigDecimal totalPrice;

    public void setTotalPrice(List<CartItem> cartItem) {
        if(cartItem!=null) {
            for (CartItem item : cartItem) {
                this.totalPrice.add(item.getTotalPrice());
            }

        }
    }
}
