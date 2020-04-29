package com.loserico.common.spring.utils;

import com.loserico.networking.utils.HttpUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Spring 环境下读写Http Servlet相关接口
 * <p>
 * Copyright: (C), 2020/4/23 12:05
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class ServletUtils {
	
	/**
	 * 获取请求头
	 *
	 * @param header
	 * @return String
	 */
	public static String getHeader(String header) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getHeader(header);
	}
	
	/**
	 * 获取Cookie
	 *
	 * @param name
	 * @return String
	 */
	public static String getCookie(String name) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return HttpUtils.getCookie(request, name);
	}
	
	/**
	 * 获取请求参数
	 *
	 * @param parameter
	 * @return String
	 */
	public static String getParameter(String parameter) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getParameter(parameter);
	}
	
	/**
	 * 添加Cookie
	 *
	 * @param name
	 * @param value
	 * @return CookieBuilder
	 */
	public static CookieBuilder addCookie(String name, String value) {
		return new CookieBuilder(name, value);
	}
	
	public static class CookieBuilder {
		
		private static final int MAX_AGE_NOT_SET = -2;
		
		private String name;
		
		private String value;
		
		/**
		 * 0  maxAge 设置为 0, 表示将Cookie删除
		 * -1 表示永久有效
		 * -2 是我自定义的, 表示builder没有设置maxAge
		 */
		private int maxAge = MAX_AGE_NOT_SET;
		
		private String domain;
		
		private String path = "/";
		
		/**
		 * 防止脚本攻击
		 */
		private boolean httpOnly = false;
		
		public CookieBuilder(String name, String value) {
			this.name = name;
			this.value = value;
		}
		
		/**
		 * 0  maxAge 设置为 0, 表示将Cookie删除
		 * -1 表示永久有效
		 *
		 * @param maxAge
		 * @return
		 */
		public CookieBuilder maxAge(int maxAge) {
			this.maxAge = maxAge;
			return this;
		}
		
		public CookieBuilder domain(String domain) {
			this.domain = domain;
			return this;
		}
		
		public CookieBuilder path(String path) {
			this.path = path;
			return this;
		}
		
		public CookieBuilder httpOnly(boolean httpOnly) {
			this.httpOnly = httpOnly;
			return this;
		}
		
		/**
		 * 创建Cookie对象并添加到response
		 *
		 * @return Cookie
		 */
		public Cookie build() {
			HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
			Cookie cookie = new Cookie(name, HttpUtils.urlEncode(value));
			if (MAX_AGE_NOT_SET != maxAge) {
				cookie.setMaxAge(maxAge);
			}
			cookie.setDomain(domain);
			cookie.setPath(path);
			cookie.setHttpOnly(httpOnly);
			response.addCookie(cookie);
			return cookie;
		}
	}
}
