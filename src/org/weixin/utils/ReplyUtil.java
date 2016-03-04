package org.weixin.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.weixin.domain.response.Article;
import org.weixin.domain.response.Music;
import org.weixin.domain.response.MusicMessage;
import org.weixin.domain.response.NewsMessage;
import org.weixin.domain.response.TextMessage;

/**
 * 回复工具类
 */
public class ReplyUtil {
	private Map<String, String> requestMap = new HashMap<String, String>();
	// 发送方帐号（open_id）
	private String fromUserName;
	// 公众帐号
	private String toUserName;

	public ReplyUtil(Map<String, String> requestMap) {
		this.requestMap = requestMap;
		this.fromUserName = requestMap.get("FromUserName");
		this.toUserName = requestMap.get("ToUserName");
	}

	/**
	 * 回复文本消息
	 */
	public String sendText(String content) {
		TextMessage textMessage = new TextMessage();
		textMessage.setToUserName(fromUserName);
		textMessage.setFromUserName(toUserName);
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
		textMessage.setContent(content);
		return MessageUtil.messageToXml(textMessage);
	}

	/**
	 * 回复音乐消息
	 */
	public String sendMusic(Music music) {
		MusicMessage musicMessage = new MusicMessage();
		musicMessage.setToUserName(fromUserName);
		musicMessage.setFromUserName(toUserName);
		musicMessage.setCreateTime(new Date().getTime());
		musicMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_MUSIC);
		musicMessage.setMusic(music);
		return MessageUtil.messageToXml(musicMessage);
	}

	/**
	 * 回复图文消息
	 */
	public String sendNewsMessage(List<Article> articleList) {
		NewsMessage newsMessage = new NewsMessage();
		newsMessage.setToUserName(fromUserName);
		newsMessage.setFromUserName(toUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);

		// 设置图文消息个数
		newsMessage.setArticleCount(articleList.size());
		// 设置图文消息包含的图文集合
		newsMessage.setArticles(articleList);
		// 将图文消息对象转换成xml字符串
		return MessageUtil.messageToXml(newsMessage);
	}

}
