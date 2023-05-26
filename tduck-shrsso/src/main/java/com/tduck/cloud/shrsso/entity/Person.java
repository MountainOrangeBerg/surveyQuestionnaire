package com.tduck.cloud.shrsso.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import nonapi.io.github.classgraph.json.Id;

import java.math.BigDecimal;
import java.util.Date;

@TableName("Person")
public class Person {
    //id
    @TableId(type = IdType.AUTO)
    private String easuser_id;
    //姓名
    @TableField("username")
    private String username;
    //员工编码
    @TableField("loginid")
    private String loginid;
    @TableField("fnumber")
    private String fnumber;
    //员工描述
    @TableField("descn")
    private String descn;
    //性别
    @TableField("sex")
    private Integer  sex;
    //出生日期
    @TableField("birthday")
    private Date birthday;
    //邮箱
    @TableField("email")
    private String email;
    //联系地址
    @TableField("address")
    private String address;
    //办公室电话
    @TableField("officephone")
    private String officephone;
    //所在部门
    @TableField("dept_id")
    private String dept_id;
    //用户编码，唯一标识
    @TableField("eas_loginid")
    private String eas_loginid;
    //家庭电话
    @TableField("homephone")
    private String homephone;
    //手机号码
    @TableField("mobile")
    private BigDecimal mobile;
    //创建时间
    @TableField("fcreateTime")
    private Date fcreateTime;
    //最后修改时间
    @TableField("fLastUpdateTime")
    private Date fLastUpdateTime;
    //入职日期
    @TableField("enterDate")
    private Date enterDate;
    //隶属组织编码
    @TableField("org_number")
    private String org_number;

    public Person() {
    }

    public Person(String username, String loginid, String fnumber, String descn, Integer  sex, Date birthday, String email, String address, String officephone, String dept_id, String eas_loginid, String easuser_id, String homephone, BigDecimal mobile, Date fcreateTime, Date fLastUpdateTime, Date enterDate, String org_number) {
        this.username = username;
        this.loginid = loginid;
        this.fnumber = fnumber;
        this.descn = descn;
        this.sex = sex;
        this.birthday = birthday;
        this.email = email;
        this.address = address;
        this.officephone = officephone;
        this.dept_id = dept_id;
        this.eas_loginid = eas_loginid;
        this.easuser_id = easuser_id;
        this.homephone = homephone;
        this.mobile = mobile;
        this.fcreateTime = fcreateTime;
        this.fLastUpdateTime = fLastUpdateTime;
        this.enterDate = enterDate;
        this.org_number = org_number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getFnumber() {
        return fnumber;
    }

    public void setFnumber(String fnumber) {
        this.fnumber = fnumber;
    }

    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    public Integer  getSex() {
        return sex;
    }

    public void setSex(Integer  sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOfficephone() {
        return officephone;
    }

    public void setOfficephone(String officephone) {
        this.officephone = officephone;
    }

    public String getDept_id() {
        return dept_id;
    }

    public void setDept_id(String dept_id) {
        this.dept_id = dept_id;
    }

    public String getEas_loginid() {
        return eas_loginid;
    }

    public void setEas_loginid(String eas_loginid) {
        this.eas_loginid = eas_loginid;
    }

    public String getEasuser_id() {
        return easuser_id;
    }

    public void setEasuser_id(String easuser_id) {
        this.easuser_id = easuser_id;
    }

    public String getHomephone() {
        return homephone;
    }

    public void setHomephone(String homephone) {
        this.homephone = homephone;
    }

    public BigDecimal getMobile() {
        return mobile;
    }

    public void setMobile(BigDecimal mobile) {
        this.mobile = mobile;
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

    public Date getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }

    public String getOrg_number() {
        return org_number;
    }

    public void setOrg_number(String org_number) {
        this.org_number = org_number;
    }

    @Override
    public String toString() {
        return "Person{" +
                " username='" + username + '\'' +
                ", loginid='" + loginid + '\'' +
                ", fnumber='" + fnumber + '\'' +
                ", descn='" + descn + '\'' +
                ", sex=" + sex +
                ", birthday=" + birthday +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", officephone='" + officephone + '\'' +
                ", dept_id='" + dept_id + '\'' +
                ", eas_loginid='" + eas_loginid + '\'' +
                ", easuser_id='" + easuser_id + '\'' +
                ", homephone='" + homephone + '\'' +
                ", mobile=" + mobile +
                ", fcreateTime=" + fcreateTime +
                ", fLastUpdateTime=" + fLastUpdateTime +
                ", enterDate=" + enterDate +
                ", org_number='" + org_number + '\'' +
                '}';
    }
}
