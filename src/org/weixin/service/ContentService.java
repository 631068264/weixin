package org.weixin.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.weixin.utils.ReplyUtil;

public interface ContentService {
	/**
	 * 处理文字信息
	 * 
	 * @param requestMap
	 * @param reply
	 * @param request
	 * @return
	 */
	public String dealText(Map<String, String> requestMap, ReplyUtil reply, HttpServletRequest request);
}
