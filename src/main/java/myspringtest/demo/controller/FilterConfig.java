package myspringtest.demo.controller;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<Tfil> loggingFilter() {
        FilterRegistrationBean<Tfil> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new Tfil());
        registrationBean.addUrlPatterns("/admin/*", "/register/*", "/student/*", "/professor/*", "/course/*", "/discipline/*", "/registerDiscipline/*", "/registerCourse/*");

        return registrationBean;
    }
}
