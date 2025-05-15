package com.example.CGC.CGCExceptions;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("errors")
public record CGCErrorMapping(Map<String, Error> error) {
}
