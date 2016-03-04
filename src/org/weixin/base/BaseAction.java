package org.weixin.base;

import javax.annotation.Resource;

import org.weixin.service.ContentService;
import org.weixin.service.UserLocationService;


public class BaseAction {
	@Resource
	protected UserLocationService userLocationService;
	@Resource
	protected ContentService contentService;
}
