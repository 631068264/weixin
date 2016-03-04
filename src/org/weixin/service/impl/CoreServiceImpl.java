package org.weixin.service.impl;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weixin.service.ContentService;
import org.weixin.service.CoreService;
import org.weixin.service.UserLocationService;
import org.weixin.utils.MessageUtil;
import org.weixin.utils.ReplyUtil;
import org.weixin.utils.UsageUtil;


/**
 * 核心处理类
 */
@Service
@Transactional
public class CoreServiceImpl implements CoreService {
	@Resource
	private UserLocationService userLocationService;
	@Resource
	private ContentService contentService;

	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @param reply
	 * @return
	 */
	public String processRequest(HttpServletRequest request) {
		// 默认返回的文本消息内容
		String respMessage = null;
		try {
			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			ReplyUtil reply = new ReplyUtil(requestMap);
			// 消息类型
			String msgType = requestMap.get("MsgType");

			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {

				respMessage = contentService.dealText(requestMap, reply, request);
			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				// 保存地理信息
				boolean b = userLocationService.saveInfo(requestMap);
				if (b) {
					respMessage = reply.sendText("保存地理位置成功 /呲牙 \n\n" + UsageUtil.getMap());
				} else {
					respMessage = reply.sendText("保存失败 稍后再试\n");
				}

			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					respMessage = reply.sendText("谢谢您的关注！\n\n" + UsageUtil.getMenu());
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// 事件KEY值，与创建自定义菜单时指定的KEY值对应
					/*
					 * String eventKey = requestMap.get("EventKey"); if
					 * (eventKey.equals("11")) { respContent = "天气预报菜
					 * 
					 * 单项被点击！"; } else if (eventKey.equals("12")) { respContent
					 * = "公交查询菜单项被点击！"; } else if
					 * 
					 * (eventKey.equals("13")) { respContent = "周边搜索菜单项被点击！"; }
					 * else if (eventKey.equals("14")) { respContent = "历史上的今
					 * 
					 * 天菜单项被点击！"; } else if (eventKey.equals("21")) {
					 * respContent = "歌曲点播菜单项被点击！"; } else if
					 * 
					 * (eventKey.equals("22")) { respContent = "经典游戏菜单项被点击！"; }
					 * else if (eventKey.equals("23")) { respContent = "美女电台菜
					 * 
					 * 单项被点击！"; } else if (eventKey.equals("24")) { respContent
					 * = "人脸识别菜单项被点击！"; } else if
					 * 
					 * (eventKey.equals("25")) { respContent = "聊天唠嗑菜单项被点击！"; }
					 * else if (eventKey.equals("31")) { respContent = "Q友圈菜单项
					 * 
					 * 被点击！"; } else if (eventKey.equals("32")) { respContent =
					 * "电影排行榜菜单项被点击！"; } else if
					 * 
					 * (eventKey.equals("33")) { respContent = "幽默笑话菜单项被点击！"; }
					 */
				}
			}
			if (respMessage == null) {
				respMessage = reply.sendText("请求处理异常，请稍候尝试！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return respMessage;

	}
}