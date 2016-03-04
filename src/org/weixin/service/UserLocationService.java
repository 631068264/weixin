package org.weixin.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.weixin.base.BaseService;
import org.weixin.domain.entity.UserLocation;
import org.weixin.utils.ReplyUtil;

public interface UserLocationService extends BaseService<UserLocation> {

	/**
	 * 保存用户地理位置信息
	 * 
	 * @param reply
	 * @param requestMap
	 * @return
	 */
	boolean saveInfo(Map<String, String> requestMap);

	/**
	 * 通过关键词获取周边信息
	 * 
	 * @param key
	 * @param reply
	 * @param requestMap
	 * @return
	 */
	String findPlace(String key, ReplyUtil reply, Map<String, String> requestMap,
			HttpServletRequest request);

}
