package com.atguigu.gamll.manager;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseCatalog2 implements Serializable{
    /*设置主键自增*/
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private String name;

    private Integer catalog1Id;
}
