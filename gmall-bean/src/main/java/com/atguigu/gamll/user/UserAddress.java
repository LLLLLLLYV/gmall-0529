package com.atguigu.gamll.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserAddress implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    //    id  user_address        user_id  consignee  phone_num  is_default
    private String userAddress;
    private Integer userId;
    private String consignee; //收货人
    private String phoneNum;
    private String isDefault; //是否默认，1是默认

}
