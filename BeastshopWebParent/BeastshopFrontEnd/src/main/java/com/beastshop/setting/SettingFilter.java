package com.beastshop.setting;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.beastshop.common.entity.Setting;


//Declaring it as a part of component so we can get autowired to work
@Component
public class SettingFilter implements Filter {
	
	@Autowired
	private SettingService service;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		HttpServletRequest servletRequest = (HttpServletRequest)request;
		String url = servletRequest.getRequestURI().toString();
		
		//Filtering out the requests for the static resources
		if(url.endsWith(".css")||url.endsWith(".js")||url.endsWith(".png")||url.endsWith(".jpg")) {
			chain.doFilter(request, response);
			return;
		}
		
		List<Setting> generalSetting = service.getGeneralSetting();
		
		generalSetting.forEach(setting->{
//			System.out.println(setting);
			request.setAttribute(setting.getKey(), setting.getValue());
		});
		
		chain.doFilter(request, response);

	}

}
