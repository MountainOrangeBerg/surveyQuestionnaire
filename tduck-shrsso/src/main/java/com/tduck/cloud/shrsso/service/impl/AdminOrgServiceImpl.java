package com.tduck.cloud.shrsso.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tduck.cloud.shrsso.entity.AdminOrg;
import com.tduck.cloud.shrsso.entity.Person;
import com.tduck.cloud.shrsso.mapper.AdminOrgMapper;
import com.tduck.cloud.shrsso.service.AdminOrgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("adminOrgService")
@RequiredArgsConstructor
public class AdminOrgServiceImpl extends ServiceImpl<AdminOrgMapper, AdminOrg> implements AdminOrgService {

    @Autowired
    private BaseMapper<AdminOrg> adminOrgBaseMapper;

    @Override
    public List<AdminOrg> getAllData() {
        QueryWrapper<AdminOrg> queryWrapper = new QueryWrapper<>();

        return adminOrgBaseMapper.selectList(queryWrapper);
    }

    public List<AdminOrg> getPageData(int pageNum, int pageSize) {
        // 创建一个分页对象
        Page<AdminOrg> page = new Page<>(pageNum, pageSize);
        // 执行分页查询
        IPage<AdminOrg> resultPage = adminOrgBaseMapper.selectPage(page, null);
        // 获取查询结果列表
        return resultPage.getRecords();
    }
}
