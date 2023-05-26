package com.tduck.cloud.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 全局加载shr的url地址
 */
@Component
public class MyComponent {
    @Value("${myapp.api.shrUrl}")
    private String shrUrl;

    public String getShrUrl() {
        return shrUrl;
    }

    public void setShrUrl(String shrUrl) {
        this.shrUrl = shrUrl;
    }

    // 使用apiKey变量
}
