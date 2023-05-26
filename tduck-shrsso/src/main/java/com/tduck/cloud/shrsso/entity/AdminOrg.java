package com.tduck.cloud.shrsso.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import nonapi.io.github.classgraph.json.Id;

import java.util.Date;

@TableName("admin_org")
public class AdminOrg {
    //组织 ID
    @TableId(type = IdType.AUTO)
    private String easdept_id;
    // 组织编码
    @TableField("fnumber")
    private String fnumber;
    //组织名称
    @TableField("name")
    private String name;
    //是否封存 为 1 是，为 0 否
    @TableField("status")
    private Integer  status;
    //上级行政组织 ID
    @TableField("superior")
    private String superior;
    //上级行政组织名称
    @TableField("supname")
    private String supname;
    //上级组织编码
    @TableField("supFnumber")
    private String supFnumber;
    //是否叶子节点为 1 是，为 0 否
    @TableField("endflag")
    private Integer  endflag;
    //排序码
    @TableField("sortCode")
    private String sortCode;
    //生效日期
    @TableField("fEffectDate")
    private Date fEffectDate;
    //最后修改时间
    @TableField("flastUpdateTime")
    private Date flastUpdateTime;
    //创建时间
    @TableField("fcreateTime")
    private Date fcreateTime;

    public AdminOrg() {
    }

    public AdminOrg(String easdept_id, String fnumber, String name, Integer  status, String superior, String supname, String supFnumber, Integer  endflag, String sortCode, Date fEffectDate, Date flastUpdateTime, Date fcreateTime) {
        this.easdept_id = easdept_id;
        this.fnumber = fnumber;
        this.name = name;
        this.status = status;
        this.superior = superior;
        this.supname = supname;
        this.supFnumber = supFnumber;
        this.endflag = endflag;
        this.sortCode = sortCode;
        this.fEffectDate = fEffectDate;
        this.flastUpdateTime = flastUpdateTime;
        this.fcreateTime = fcreateTime;
    }

    public String getEasdept_id() {
        return easdept_id;
    }

    public void setEasdept_id(String easdept_id) {
        this.easdept_id = easdept_id;
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

    public Integer  getStatus() {
        return status;
    }

    public void setStatus(Integer  status) {
        this.status = status;
    }

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
        this.superior = superior;
    }

    public String getSupname() {
        return supname;
    }

    public void setSupname(String supname) {
        this.supname = supname;
    }

    public String getSupFnumber() {
        return supFnumber;
    }

    public void setSupFnumber(String supFnumber) {
        this.supFnumber = supFnumber;
    }

    public Integer  getEndflag() {
        return endflag;
    }

    public void setEndflag(Integer  endflag) {
        this.endflag = endflag;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public Date getFEffectDate() {
        return fEffectDate;
    }

    public void setFEffectDate(Date fEffectDate) {
        this.fEffectDate = fEffectDate;
    }

    public Date getFlastUpdateTime() {
        return flastUpdateTime;
    }

    public void setFlastUpdateTime(Date flastUpdateTime) {
        this.flastUpdateTime = flastUpdateTime;
    }

    public Date getFcreateTime() {
        return fcreateTime;
    }

    public void setFcreateTime(Date fcreateTime) {
        this.fcreateTime = fcreateTime;
    }

    @Override
    public String toString() {
        return "{" +
                "easdept_id='" + easdept_id + '\'' +
                ", fnumber='" + fnumber + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", superior='" + superior + '\'' +
                ", supname='" + supname + '\'' +
                ", supFnumber='" + supFnumber + '\'' +
                ", endflag=" + endflag +
                ", sortCode='" + sortCode + '\'' +
                ", fEffectDate=" + fEffectDate +
                ", flastUpdateTime=" + flastUpdateTime +
                ", fcreateTime=" + fcreateTime +
                '}';
    }
}
