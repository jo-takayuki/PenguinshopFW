package com.example.app.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.app.filter.AuthFilter;
import com.example.app.filter.AuthFilterCompany;
import com.example.app.filter.AuthFilterMember;

@Configuration
public class AppConfig implements WebMvcConfigurer {

	@Override
	public Validator getValidator() {
		var validator = new LocalValidatorFactoryBean();
		validator.setValidationMessageSource(messageSource());
		return validator;
	}

	@Bean
	public MessageSource messageSource() {
		var messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("validation");
		return messageSource;
	}

	@Bean
	public FilterRegistrationBean<AuthFilter> authFilter() {
		var bean = new FilterRegistrationBean<>(new AuthFilter());
		bean.addUrlPatterns("/admin/*");
		return bean;
	}

	@Bean
	public FilterRegistrationBean<AuthFilterMember> authFilterMember() {
		var bean = new FilterRegistrationBean<>(new AuthFilterMember());
		bean.addUrlPatterns("/indiv/*");
		return bean;
	}

	@Bean
	public FilterRegistrationBean<AuthFilterCompany> authFilterCompany() {
		var bean = new FilterRegistrationBean<>(new AuthFilterCompany());
		bean.addUrlPatterns("/corp/*");
		return bean;
	}

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/uploads/**").addResourceLocations("file:///C:/Users/zd1P16/uploads/");
		registry.addResourceHandler("/uploads/**").addResourceLocations("file:/home/trainee/uploads/");
	}

}
