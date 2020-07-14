package com.project.shoppingcart;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
     //  @Override
     // public void addViewControllers(ViewControllerRegistry registry) {
        
    //   registry.addViewController("/").setViewName("home");
    //}
// added because maybe can cause problem in editing a product 
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
registry.addResourceHandler("/media/**")
.addResourceLocations("file:/C:/Users/91954/Desktop/shoppingcart/src/main/resources/static/media/");

    } 
}