package com.example.CGC.Controller;

import com.example.CGC.CGCExceptions.CGCErrorMapping;
import com.example.CGC.CGCExceptions.CGCExceptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configurations {
    private CGCErrorMapping CGCErrorMapping;
    Configurations(CGCErrorMapping CGCErrorMapping){
        this.CGCErrorMapping = CGCErrorMapping;
    }
    @Bean
    @Qualifier("Exceptions")
    public CGCExceptions abyadExceptions(){
        return new CGCExceptions(CGCErrorMapping);
    }
}
