package net.ringo.ringoSwap.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.ringo.ringoSwap.dao.Mapper;

public interface EverService {
	
	public Map<String, Object> service(HttpServletRequest request, Mapper mapper);
}
