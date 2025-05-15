package com.example.CGC.Shared;//package com.example.abyad.Shared;
//
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//
//@Service
//public class TestSchedule {
//    @Scheduled(fixedRate = 5000)
//    public void test(){
//        UriComponentsBuilder url = UriComponentsBuilder.fromUriString("http://localhost:8080/api/v1.0.0/test");
//        String url2 = url.queryParam("test", "saiffff").toUriString();
//        System.out.println(url2);
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("headers", "saif");
//        HttpEntity httpEntity = new HttpEntity(httpHeaders);
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<?> response = restTemplate.exchange(url2, HttpMethod.GET, httpEntity, String.class);
//        System.out.println(response.getBody());
//    }
//}
