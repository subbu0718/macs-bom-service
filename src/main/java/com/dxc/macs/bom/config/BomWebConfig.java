package com.dxc.macs.bom.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class BomWebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .exposedHeaders("Content-Disposition")
                .allowedMethods("GET", "POST")
                .allowedOrigins("http://localhost:4200", "http://9.64.192.179:4200","http://9.64.192.179", "http://localhost:4201", "http://9.64.192.179:4201","http://d2cdyjup4zb548.cloudfront.net","http://macs-bucket.s3-website.eu-south-1.amazonaws.com","https://www.noprod.iveco.dxcmacs.entsvcs.it","https://www.noprod.ferrari.dxcmacs.entsvcs.it","https://www.prod.iveco.dxcmacs.entsvcs.it");
    }
}
