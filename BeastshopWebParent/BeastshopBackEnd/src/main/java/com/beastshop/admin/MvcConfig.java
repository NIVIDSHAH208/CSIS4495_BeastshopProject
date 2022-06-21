package com.beastshop.admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String dirName= "user-photos";
		Path userPhotosDirPath = Paths.get(dirName);
		String userPhotosPath=userPhotosDirPath.toFile().getAbsolutePath();
		
		//method below expose the absolute path of user photos directory to the end user
		registry.addResourceHandler("/"+dirName+"/**").addResourceLocations("file:/"+userPhotosPath+"/");
		
		
		String categoryImagesDirName= "../category-images";
		Path categoryImageDir = Paths.get(categoryImagesDirName);
		String categoryImagesPath=categoryImageDir.toFile().getAbsolutePath();
		
		//method below expose the absolute path of user photos directory to the end user
		registry.addResourceHandler("/category-images/**")
			.addResourceLocations("file:/"+categoryImagesPath+"/");
		
		
		String brandLogosDirName= "../brand-logos";
		Path brandLogosDir = Paths.get(brandLogosDirName);
		String brandLogosPath=brandLogosDir.toFile().getAbsolutePath();
		
		//method below expose the absolute path of user photos directory to the end user
		registry.addResourceHandler("/brand-logos/**")
			.addResourceLocations("file:/"+brandLogosPath+"/");
	}
	
}
