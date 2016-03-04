package org.weixin.service.impl;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weixin.domain.response.Music;
import org.weixin.service.ContentService;
import org.weixin.service.UserLocationService;
import org.weixin.service.extra.HistoryService;
import org.weixin.service.extra.MusicService;
import org.weixin.service.extra.TranslateService;
import org.weixin.utils.ReplyUtil;
import org.weixin.utils.UsageUtil;

@Service
@Transactional
public class ContentServiceImpl implements ContentService {
	@Resource
	private UserLocationService userLocationService;

	public String dealText(Map<String, String> requestMap, ReplyUtil reply,
			HttpServletRequest request) {

		// 获取用户输入
		String content = requestMap.get("Content").trim();

		String respMessage = null;
		// 功能执行
		if (content.startsWith("?") || content.startsWith("？")) {
			respMessage = reply.sendText(UsageUtil.getMenu());

		} else if (content.startsWith("tr")) {// 翻译功能
			String key = content.replaceAll("^tr", "");
			if ("tr".equals(content)) {
				respMessage = reply.sendText(UsageUtil.getTranslation());
			} else {
				respMessage = reply.sendText(TranslateService.translate(key));

				if (respMessage == null) {
					respMessage = reply.sendText(UsageUtil.getTranslation());
				}
			}
		} else if (content.startsWith("map")) {// 周边搜索功能
			String key = content.replaceAll("^map", "").trim();
			if ("map".equals(content)) {
				respMessage = reply.sendText(UsageUtil.getMap());
			} else {
				respMessage = userLocationService.findPlace(key, reply, requestMap, request);
			}

		} else if (content.startsWith("his")) {// 当年今天功能
			if ("his".equals(content)) {
				respMessage = reply.sendText(HistoryService.getTodayInHistoryInfo());
			}

		} else if (content.startsWith("mu")) {// 音乐搜索功能

			String key = content.replaceAll("^mu", "").trim();
			if ("mu".equals(content)) {
				respMessage = reply.sendText(UsageUtil.getMusic());
			} else {
				// 歌曲名 歌手名
				String[] arr = key.split(" ");
				String musicName = arr[0];
				// 可以没有歌手名
				String singer = null;
				if (arr.length == 2) {
					singer = arr[1];
				}

				Music music = MusicService.searchMusic(musicName, singer);
				respMessage = reply.sendMusic(music);

				if (respMessage == null) {
					respMessage = reply.sendText(UsageUtil.getMusic());
				}
			}
		}

		return respMessage;
	}

}
