package com.n34.space.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class Circle {
    @TableId
    private String id;
    private String creatorId;
    private String introduction;
    private String avatarFilename;
    private String wallpaperFilename;
    private String name;
    @TableField(fill = FieldFill.INSERT)
    private Date timeCreated;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date timeUpdated;
}