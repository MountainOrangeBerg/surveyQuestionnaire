package com.tduck.cloud.api.web.controller;

import cn.hutool.core.util.ObjUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kingdee.shr.api.Response;
import com.kingdee.shr.api.SHRClient;
import com.tduck.cloud.account.constant.AccountConstants;
import com.tduck.cloud.account.entity.UserEntity;
import com.tduck.cloud.account.util.PasswordUtils;
import com.tduck.cloud.api.annotation.NotLogin;
import com.tduck.cloud.common.util.QueryWrapperUtils;
import com.tduck.cloud.common.util.Result;
import com.tduck.cloud.common.validator.group.AddGroup;
import com.tduck.cloud.shrsso.entity.AdminOrg;
import com.tduck.cloud.shrsso.entity.Person;
import com.tduck.cloud.shrsso.entity.Position;
import com.tduck.cloud.shrsso.service.AdminOrgService;
import com.tduck.cloud.shrsso.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sync/person")
public class PersonSyncController {

    private final PersonService personService;

    @Autowired
    public PersonSyncController(PersonService personService) {
        this.personService = personService;
    }

    @Value("${myapp.api.shrUrl}")
    private String shrUrl;


    @PostMapping("/saveBatch")
    @NotLogin
    public Result<Object> saveBatch() throws IOException {
        String serviceName = "inteOAGetPersonDataService";
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
        List<Person> personList = JSONArray.parseArray(res.getData().toString(), Person.class);
        List<Person> failedPersons = new ArrayList<>();
        for (Person person : personList) {
            // 判断数据是否已存在
            boolean saveResult;
            // 数据已存在，执行更新操作
            saveResult = personService.saveOrUpdate(person);
            if (!saveResult) {
                // 保存失败，将数据添加到失败集合中
                failedPersons.add(person);
            }
        }
        if (failedPersons.isEmpty()) {
            return Result.success(true, "成功");
        } else {
            return Result.failed("部分数据保存失败", failedPersons);
        }
    }


    @GetMapping("/getPageData")
    public Result getPageData(Page page, Person person) {
        QueryWrapper<Person> simpleQuery = QueryWrapperUtils.toSimpleQuery(person);
        return Result.success(personService.page(page, simpleQuery));
    }

    @GetMapping(value = "/{id}")
    public Result<Person> getInfo(@PathVariable("id") String id) {
        QueryWrapper<Person> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fnumber", id);
        return Result.success(personService.getOne(queryWrapper));
    }

    @PostMapping("/add")
    public Result<Boolean> add(@Validated(AddGroup.class) @RequestBody Person person) {
        return Result.success(personService.save(person));
    }

    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody Person person) {
        return Result.success(personService.updateById(person));
    }

    @PostMapping("/delete/{ids}")
    public Result<Boolean> delete(@PathVariable List<String> ids) {
        return Result.success(personService.removeByIds(ids));
    }

}
