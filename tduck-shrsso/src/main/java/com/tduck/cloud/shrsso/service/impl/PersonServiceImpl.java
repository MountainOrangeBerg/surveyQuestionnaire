package com.tduck.cloud.shrsso.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tduck.cloud.shrsso.entity.Person;
import com.tduck.cloud.shrsso.mapper.PersonMapper;
import com.tduck.cloud.shrsso.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("personService")
@RequiredArgsConstructor
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements PersonService {

    @Autowired
    private BaseMapper<Person> personBaseMapper;

    /**
     * 查询全部数据
     * @return
     */
    @Override
    public List<Person> getAllData() {
        // 创建一个空的查询条件
        QueryWrapper<Person> queryWrapper = new QueryWrapper<>();

        // 调用 selectList 方法获取表的全部数据
        return personBaseMapper.selectList(queryWrapper);
    }

    public List<Person> getPageData(int pageNum, int pageSize) {
        // 创建一个分页对象
        Page<Person> page = new Page<>(pageNum, pageSize);
        // 执行分页查询
        IPage<Person> resultPage = personBaseMapper.selectPage(page, null);
        // 获取查询结果列表
        return resultPage.getRecords();
    }
}
