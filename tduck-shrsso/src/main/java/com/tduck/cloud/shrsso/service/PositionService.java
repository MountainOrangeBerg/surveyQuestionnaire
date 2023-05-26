package com.tduck.cloud.shrsso.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tduck.cloud.shrsso.entity.AdminOrg;
import com.tduck.cloud.shrsso.entity.Person;
import com.tduck.cloud.shrsso.entity.Position;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface PositionService extends IService<Position> {

    List<Position> getAllData();

    List<Position> getPageData(int pageNum, int pageSize);



}
