package com.atguigu.gamll.manager;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseCatalog1 implements Serializable{
    /*设置主键自增*/
    @TableId(type = IdType.AUTO)
    protected Integer id;

    private String name;
}
