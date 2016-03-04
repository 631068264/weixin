package org.weixin.service.extra;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.weixin.domain.response.Music;
import org.weixin.utils.CommonUtil;

public class MusicService {
	/**
	 * 发送http请求取得返回的输入流
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @return InputStream
	 */
	private static InputStream httpRequest(String requestUrl) {
		InputStream inputStream = null;
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoInput(true);
			httpUrlConn.setRequestMethod("GET");
			httpUrlConn.connect();
			// 获得返回的输入流
			inputStream = httpUrlConn.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inputStream;
	}

	/**
	 * 根据名称和作者搜索音乐
	 * 
	 * @param musicTitle
	 *            音乐名称
	 * @param musicAuthor
	 *            音乐作者
	 * @return Music
	 */
	public static Music searchMusic(String musicTitle, String musicAuthor) {
		// 百度音乐搜索地址
		String requestUrl = "http://box.zhangmen.baidu.com/x?op=12&count=1&title={TITLE}$${AUTHOR}$$$$";
		// 对音乐名称、作者进URL编码
		requestUrl = requestUrl.replace("{TITLE}", CommonUtil.toUTF8(musicTitle));

		if (musicAuthor != null) {
			requestUrl = requestUrl.replace("{AUTHOR}", CommonUtil.toUTF8(musicAuthor));
		} else {
			requestUrl = requestUrl.substring(0, requestUrl.indexOf("$") + 2);
		}

		// 处理名称、作者中间的空格
		requestUrl = requestUrl.replaceAll("\\+", "%20");

		// 从返回结果中解析出Music
		Music music = parseMusic(requestUrl);

		// 如果music不为null，设置标题和描述
		if (null != music) {
			music.setTitle(musicTitle);
			// 如果作者不为""，将描述设置为作者
			if (musicAuthor != null)
				music.setDescription(musicAuthor);
			else
				music.setDescription("来自百度音乐");
		}
		return music;
	}

	/**
	 * 解析音乐参数
	 * 
	 * @param requestUrl
	 *            百度音乐搜索API返回的输入流
	 * @return Music
	 */
	@SuppressWarnings("unchecked")
	private static Music parseMusic(String requestUrl) {

		Music music = null;
		try {
			Document doc = Jsoup.connect(requestUrl).timeout(5000).get();
			if (doc != null) {
				// 当搜索到的歌曲数
				String count = doc.getElementsByTag("count").html();

				if (!"0".equals(count)) {
					// 普通品质的encode、decode
					String urlEncode = doc.select("url encode").first().html();
					String urlDecode = doc.select("url decode").first().html();
					String url = urlEncode.substring(0, urlEncode.lastIndexOf("/") + 1) + urlDecode;

					if (-1 != urlDecode.lastIndexOf("&"))
						url = urlEncode.substring(0, urlEncode.lastIndexOf("/") + 1) + urlDecode.substring(0, urlDecode.lastIndexOf("&"));
					// 默认情况下，高音质音乐的URL 等于 普通品质音乐的URL
					String durl = url;
					Element durElement = doc.select("durl encode").first();
					if (durElement != null) {
						String durlEncode = doc.select("durl encode").first().html();
						String durlDecode = doc.select("durl decode").first().html();
						durl = durlEncode.substring(0, durlEncode.lastIndexOf("/") + 1) + durlDecode;
						if (-1 != durlDecode.lastIndexOf("&"))
							durl = durlEncode.substring(0, durlEncode.lastIndexOf("/") + 1) + durlDecode.substring(0, durlDecode.lastIndexOf("&"));
					}
					music = new Music();
					// 设置普通品质音乐链接
					music.setMusicUrl(url);
					// 设置高品质音乐链接
					music.setHQMusicUrl(durl);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return music;
	}

	// 测试方法
	/*
	 * public static void main(String[] args) { Music music =
	 * searchMusic("相信自己", null);
	 * 
	 * System.out.println("音乐名称：" + music.getTitle());
	 * System.out.println("音乐描述：" + music.getDescription());
	 * System.out.println("普通品质链接：" + music.getMusicUrl());
	 * System.out.println("高品质链接：" + music.getHQMusicUrl()); }
	 */
}
