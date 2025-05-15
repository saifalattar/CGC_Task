package com.example.CGC.Shared;
import com.example.CGC.Schemas.LogData;
import com.example.CGC.Schemas.OAuth2;
import com.example.CGC.Schemas.User;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class SharedFunctions {
    @Autowired
    public static Properties properties;
    SharedFunctions(Properties properties2){
        properties = properties2;
    }

    public static ArrayList<LogData> logs = new ArrayList<>();

    public static boolean isOAuthDataValid(OAuth2 oAuth2){
        return (properties.getProperty("client_id").equals(oAuth2.client_id()) && properties.getProperty("client_secret").equals(oAuth2.client_secret()));
    }

    public static boolean isOAuthTokenValid(String token){
        DefaultClaims defaultClaims = (DefaultClaims)decodeToken(token).getBody();
        return (defaultClaims.get("client_id").equals(properties.getProperty("client_id")) && defaultClaims.get("client_secret").equals(properties.getProperty("client_secret")));
    }

    public static String generateToken(User user){
        return Jwts.builder()
                .setClaims(user.userClaims())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 10)).compact(); // token available for 10 days
    }

    public static String generateToken(OAuth2 oauth2){
        System.out.println(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 5));
        return Jwts.builder()
                .setClaims(oauth2.getClaims())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 5)).compact(); // token available for 5 days
    }

    public static Jwt decodeToken(String token){
        return Jwts.parserBuilder().build().parse(token);
    }
    public static boolean isTokenValid(String token){
        Jwt decoded = decodeToken(token);
        if(((DefaultClaims)decoded.getBody()).get("email").toString().isEmpty()){
            return false;
        }
        return true;
    }

    public static void log (String typeOfLog,String msg){
        logs.add(new LogData(typeOfLog, msg));
    }

    public static ArrayList<LogData> getLogs() {
        return logs;
    }
}
