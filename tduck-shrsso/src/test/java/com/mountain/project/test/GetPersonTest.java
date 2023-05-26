package com.mountain.project.test;

import com.alibaba.fastjson.JSONArray;
import com.kingdee.shr.api.Response;
import com.kingdee.shr.api.SHRClient;
import com.tduck.cloud.shrsso.entity.Person;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetPersonTest {

    public static void main(String[] args) throws IOException {
        String SHR_LOCAL = "http://192.168.3.107:80/shr";

        String serviceName = "inteOAGetPersonDataService";

        SHRClient client = new SHRClient();
//调用OSF所需要的参数
        Map<String, Object> param = new HashMap<String, Object>();
/**
 * 参数 filterType 不填或者 filterType=1 时，取的是系统中用户状态为启用
 * 的用工关系状态的员工；参数 filterType=0 时，取的是用户状态为禁用的用工关系的
 * 员工
 */

        Response res = client.executeService(SHR_LOCAL, serviceName, param);
        List<Person> people = JSONArray.parseArray(res.getData().toString(), Person.class);
        System.out.println(people.get(2).toString());

    }

}
