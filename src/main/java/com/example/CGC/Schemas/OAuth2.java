package com.example.CGC.Schemas;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.jsonwebtoken.impl.DefaultClaims;

import java.util.HashMap;
import java.util.Map;

@JsonFormat
public record OAuth2(String client_id, String client_secret) {
    public DefaultClaims getClaims(){
        Map<String, String> data = new HashMap<>();
        data.put("client_id", client_id);
        data.put("client_secret", client_secret);

        return new DefaultClaims(data);
    }
}
