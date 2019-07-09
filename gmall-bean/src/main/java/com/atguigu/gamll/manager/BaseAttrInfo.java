package com.atguigu.gamll.manager;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BaseAttrInfo implements Serializable{

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String attrName;

    private Integer catalog3Id;

    @TableField(exist = false) /*此字段不在数据库中*/
    private List<BaseAttrValue> attrValues;

}
