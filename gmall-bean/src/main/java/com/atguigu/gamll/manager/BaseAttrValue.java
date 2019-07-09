package com.atguigu.gamll.manager;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseAttrValue implements Serializable{

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String valueName;

    private Integer attrId;

}
