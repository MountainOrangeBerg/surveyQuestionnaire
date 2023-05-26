package com.tduck.cloud.shrsso.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tduck.cloud.shrsso.entity.Person;
import com.tduck.cloud.shrsso.entity.Position;
import com.tduck.cloud.shrsso.mapper.PositionMapper;
import com.tduck.cloud.shrsso.service.PositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("positionService")
@RequiredArgsConstructor
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements PositionService {
    @Autowired
    private BaseMapper<Position> positionBaseMapper;

    @Override
    public List<Position> getAllData() {

        QueryWrapper<Position> queryWrapper = new QueryWrapper<>();

        return positionBaseMapper.selectList(queryWrapper);
    }

    public List<Position> getPageData(int pageNum, int pageSize) {
        // 创建一个分页对象
        Page<Position> page = new Page<>(pageNum, pageSize);
        // 执行分页查询
        IPage<Position> resultPage = positionBaseMapper.selectPage(page, null);
        // 获取查询结果列表
        return resultPage.getRecords();
    }
}
