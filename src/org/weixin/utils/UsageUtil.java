package org.weixin.utils;

public class UsageUtil {
	/*
	 * 服务菜单
	 */
	public static String getMenu() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("本平台提供服务如下").append("\n\n");
		buffer.append("tr  翻译").append("\n");
		buffer.append("mu  音乐搜索").append("\n");
		buffer.append("his  当年今天").append("\n");
		buffer.append("map 关键词（如ATM）").append("\n");
		buffer.append("回复 ? 显示主菜单").append("\n");
		return buffer.toString();
	}

	/**
	 * 翻译功能使用说明
	 */
	public static String getTranslation() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("翻译服务使用说明(中英自动翻译)").append("\n\n");
		buffer.append("tr:待翻译的词").append("\n");
		buffer.append("回复 ? 显示主菜单").append("\n");
		return buffer.toString();
	}

	/**
	 * 音乐搜索功能说明
	 */
	public static String getMusic() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("音乐搜索功能说明 ").append("\n\n");
		buffer.append("mu 歌曲名   (模糊查找)").append("\n");
		buffer.append("mu 歌曲名 歌手名 (精确查找)").append("\n");
		buffer.append("回复 ? 显示主菜单").append("\n");
		return buffer.toString();
	}

	/**
	 * 周边搜索功能说明
	 */
	public static String getMap() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("周边搜索功能说明 ").append("\n\n");
		buffer.append("1）发送地理位置").append("\n");
		buffer.append("点击窗口底部的“+”按钮，选择“位置”，点“发送”").append("\n\n");
		buffer.append("2）指定关键词搜索").append("\n");
		buffer.append("格式：map 关键词\n例如：map ATM、map KTV");
		buffer.append("回复 ? 显示主菜单").append("\n");
		return buffer.toString();
	}

}
