package com.tduck.cloud.api.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kingdee.shr.api.Response;
import com.kingdee.shr.api.SHRClient;
import com.tduck.cloud.api.annotation.NotLogin;
import com.tduck.cloud.common.util.QueryWrapperUtils;
import com.tduck.cloud.common.util.Result;
import com.tduck.cloud.shrsso.entity.Person;
import com.tduck.cloud.shrsso.entity.Position;
import com.tduck.cloud.shrsso.service.PositionService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sync/position")
public class PositionSyncController {

    private final PositionService positionService;

    @Autowired
    public PositionSyncController(PositionService positionService) {
        this.positionService = positionService;
    }

    @Value("${myapp.api.shrUrl}")
    private String shrUrl;


    @PostMapping("/saveBatch")
    @NotLogin
    public Result<Object> saveBatch() throws IOException {

        String serviceName = "inteOAGetPositionDataService";

        SHRClient client = new SHRClient();
        //调用OSF所需要的参数
        Map<String, Object> param = new HashMap<String, Object>();
        /**
         * 参数       filterType 不填或者 filterType=1 时，取的是系统中用户状态为启用
         * 的用工关系状态的员工；参数 filterType=0 时，取的是用户状态为禁用的用工关系的
         * 员工
         */
        Response res = client.executeService(shrUrl, serviceName, param);
        System.out.println(res.getData().toString());
        List<Position> positionList = JSONArray.parseArray(res.getData().toString(), Position.class);
        List<Position> failedPositions = new ArrayList<>();

        for (Position position : positionList) {
            System.out.println(position.toString());
            // 判断数据是否存在
            // 数据不存在，执行插入操作
            boolean saveResult = positionService.saveOrUpdate(position);
            if (!saveResult) {
                // 保存失败，将数据添加到失败集合中
                failedPositions.add(position);
            }
        }
        Boolean flag = failedPositions.size() > 0 ? false : true;
        if (flag) {
            return Result.success(flag, "成功");
        } else {
            return Result.failed("部分职位同步失败", failedPositions);
        }
    }

    @GetMapping("/queryAll")
    @NotLogin
    public Result<List> queryAll() {
        List<Position> allData = positionService.getAllData();
        return Result.success(allData);
    }


    @GetMapping("/getPageData")
    public Result getPageData(Page page, Position position) {
        QueryWrapper<Position> simpleQuery = QueryWrapperUtils.toSimpleQuery(position);
        System.out.println(simpleQuery.toString());
        System.out.println(simpleQuery);
        return Result.success(positionService.page(page, simpleQuery));
    }

}
