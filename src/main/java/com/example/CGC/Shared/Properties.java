package com.example.CGC.Shared;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("properties")
public record Properties(Map<String, Object> property) {
    public Object getProperty(String key){
        return property.get(key);
    }
}
