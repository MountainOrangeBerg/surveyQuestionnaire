package com.tduck.cloud.shrsso.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import nonapi.io.github.classgraph.json.Id;

import java.util.Date;

@TableName("Position")

public class Position {

    @TableId(type = IdType.AUTO)
    private String eas_id;// 职位 ID 职位 ID
    @TableField("fnumber")
    private String fnumber;// 职位编码 职位编码
    @TableField("name")
    private String name;// 职位名称 职位名称
    @TableField("descn")
    private String descn;// 职位描述 职位描述
    @TableField("isrespposition")
    private Integer  isrespposition;// 负责人职位 为 1 则是，为 0 则否
    @TableField("parent_id")
    private String parent_id;// 上级职位 ID 上级职位 ID
    @TableField("dept_id")
    private String dept_id;// 组织 ID 组织 ID
    @TableField("positiontype_id")
    private String positiontype_id;// 职层 ID 职层 ID（不是编码）
    @TableField("positiontype_name")
    private String positiontype_name;// 职层名称 职层名称
    @TableField("deletedstatus")
    private Integer  deletedstatus;// 封存状态 为 1 是正常，为 2 是封存
    @TableField("effectdate")
    private Date effectdate;// 职位生效日期 职位生效日期
    @TableField("fcreateTime")
    private Date fcreateTime;// 创建时间 创建时间
    @TableField("fLastUpdateTime")
    private Date fLastUpdateTime;// 最后修改时间 最后修改时间

    public Position() {
    }

    public Position(String eas_id, String fnumber, String name, String descn, Integer  isrespposition, String parent_id, String dept_id, String positiontype_id, String positiontype_name, Integer  deletedstatus, Date effectdate, Date fcreateTime, Date fLastUpdateTime) {
        this.eas_id = eas_id;
        this.fnumber = fnumber;
        this.name = name;
        this.descn = descn;
        this.isrespposition = isrespposition;
        this.parent_id = parent_id;
        this.dept_id = dept_id;
        this.positiontype_id = positiontype_id;
        this.positiontype_name = positiontype_name;
        this.deletedstatus = deletedstatus;
        this.effectdate = effectdate;
        this.fcreateTime = fcreateTime;
        this.fLastUpdateTime = fLastUpdateTime;
    }

    public String getEas_id() {
        return eas_id;
    }

    public void setEas_id(String eas_id) {
        this.eas_id = eas_id;
    }

    public String getFnumber() {
        return fnumber;
    }

    public void setFnumber(String fnumber) {
        this.fnumber = fnumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    public Integer  getIsrespposition() {
        return isrespposition;
    }

    public void setIsrespposition(Integer  isrespposition) {
        this.isrespposition = isrespposition;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getDept_id() {
        return dept_id;
    }

    public void setDept_id(String dept_id) {
        this.dept_id = dept_id;
    }

    public String getPositiontype_id() {
        return positiontype_id;
    }

    public void setPositiontype_id(String positiontype_id) {
        this.positiontype_id = positiontype_id;
    }

    public String getPositiontype_name() {
        return positiontype_name;
    }

    public void setPositiontype_name(String positiontype_name) {
        this.positiontype_name = positiontype_name;
    }

    public Integer  getDeletedstatus() {
        return deletedstatus;
    }

    public void setDeletedstatus(Integer  deletedstatus) {
        this.deletedstatus = deletedstatus;
    }

    public Date getEffectdate() {
        return effectdate;
    }

    public void setEffectdate(Date effectdate) {
        this.effectdate = effectdate;
    }

    public Date getFcreateTime() {
        return fcreateTime;
    }

    public void setFcreateTime(Date fcreateTime) {
        this.fcreateTime = fcreateTime;
    }

    public Date getFLastUpdateTime() {
        return fLastUpdateTime;
    }

    public void setFLastUpdateTime(Date fLastUpdateTime) {
        this.fLastUpdateTime = fLastUpdateTime;
    }

    @Override
    public String toString() {
        return "Position{" +
                "eas_id='" + eas_id + '\'' +
                ", fnumber='" + fnumber + '\'' +
                ", name='" + name + '\'' +
                ", descn='" + descn + '\'' +
                ", isrespposition=" + isrespposition +
                ", parent_id='" + parent_id + '\'' +
                ", dept_id='" + dept_id + '\'' +
                ", positiontype_id='" + positiontype_id + '\'' +
                ", positiontype_name='" + positiontype_name + '\'' +
                ", deletedstatus=" + deletedstatus +
                ", effectdate=" + effectdate +
                ", fcreateTime=" + fcreateTime +
                ", fLastUpdateTime=" + fLastUpdateTime +
                '}';
    }
}
