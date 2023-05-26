package com.tduck.cloud.api.web.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kingdee.shr.api.Response;
import com.kingdee.shr.api.SHRClient;
import com.tduck.cloud.api.annotation.NotLogin;
import com.tduck.cloud.api.config.MyComponent;
import com.tduck.cloud.common.util.QueryWrapperUtils;
import com.tduck.cloud.common.util.Result;
import com.tduck.cloud.shrsso.entity.AdminOrg;
import com.tduck.cloud.shrsso.entity.Person;
import com.tduck.cloud.shrsso.entity.Position;
import com.tduck.cloud.shrsso.service.AdminOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sync/adminOrg")
public class AdminOrgSyncController {

    private final AdminOrgService adminOrgService;

    @Autowired
    public AdminOrgSyncController(AdminOrgService adminOrgService) {
        this.adminOrgService = adminOrgService;
    }

    @Value("${myapp.api.shrUrl}")
    private String shrUrl;

    @PostMapping("/saveBatch")
    @NotLogin
    public Result<Object> saveBatch() throws IOException {
        System.out.println(shrUrl);
        //OSF名称，注意是名称不是编码
        String serviceName = "inteOAGetAdminOrgDataService";

        SHRClient client = new SHRClient();
        //调用OSF所需要的参数
        Map<String, Object> param = new HashMap<String, Object>();
        Response res = client.executeService(shrUrl, serviceName, param);
        System.out.println(res.getData().toString());
        List<AdminOrg> adminOrgList = JSONObject.parseArray(res.getData().toString(), AdminOrg.class);
        List<AdminOrg> failedAdminOrgs = new ArrayList<>();

        for (AdminOrg adminOrg : adminOrgList) {
            System.out.println(adminOrg.toString());
            // 判断数据是否已存在
            boolean saveResult;
            // 数据已存在，执行更新操作
            saveResult = adminOrgService.saveOrUpdate(adminOrg);
            if (!saveResult) {
                // 保存失败，将数据添加到失败集合中
                failedAdminOrgs.add(adminOrg);
            }
        }

        if (failedAdminOrgs.isEmpty()) {
            return Result.success(true, "成功");
        } else {
            return Result.failed("部分数据保存失败", failedAdminOrgs);
        }

    }

    @GetMapping("/queryAll")
    @NotLogin
    public Result<List> queryAll() {
        List<AdminOrg> allData = adminOrgService.getAllData();
        return Result.success(allData);
    }

    @GetMapping("/getPageData")
    public Result getPageData(Page page, AdminOrg adminOrg) {
        QueryWrapper<AdminOrg> simpleQuery = QueryWrapperUtils.toSimpleQuery(adminOrg);
        return Result.success(adminOrgService.page(page, simpleQuery));
    }

}
