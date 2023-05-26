package com.tduck.cloud.shrsso.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tduck.cloud.shrsso.entity.AdminOrg;
import com.tduck.cloud.shrsso.entity.Person;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminOrgService extends IService<AdminOrg> {

    List<AdminOrg> getAllData();

    List<AdminOrg> getPageData(int pageNum, int pageSize);


}
