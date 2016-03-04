package org.weixin.service.extra;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HistoryService {
	/**
	 * 发起http get请求获取网页源代码
	 * 
	 * @param requestUrl
	 * @return
	 */
	private static String httpRequest(String requestUrl) {
		StringBuffer buffer = null;

		try {
			// 建立连接
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoInput(true);
			httpUrlConn.setRequestMethod("GET");

			// 获取输入流
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			// 读取返回结果
			buffer = new StringBuffer();
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			httpUrlConn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	private static String extract(String url) {
		StringBuffer buffer = null;
		// 日期标签：区分是昨天还是今天
		String dateTag = null;

		try {
			Document doc = Jsoup.connect(url).timeout(5000).get();
			if (doc != null) {
				Elements listrens = doc.getElementsByClass("listren");
				// 防止网站不更新
				dateTag = (listrens.text().contains(getMonthDay(-1))) ? getMonthDay(-1) : getMonthDay(0);
				if (listrens.text().contains(getMonthDay(-1))) {
					dateTag = getMonthDay(-1);
				}
				// 拼装标题
				buffer = new StringBuffer();
				buffer.append("历史上的").append(dateTag).append("\n\n");

				for (Element listren : listrens) {
					String text = listren.select("li").select("a").html();
					text = text.replace(dateTag, "");
					buffer.append(text).append("\n\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 将buffer最后两个换行符移除并返回
		return (null == buffer) ? null : buffer.substring(0, buffer.lastIndexOf("\n\n"));
	}

	/**
	 * 获取前/后n天日期(M月d日)
	 * 
	 * @return
	 */
	private static String getMonthDay(int diff) {
		DateFormat df = new SimpleDateFormat("M月d日");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, diff);// 当前日期 + diff
		return df.format(c.getTime());
	}

	/**
	 * 封装历史上的今天查询方法，供外部调用
	 * 
	 * @return
	 */
	public static String getTodayInHistoryInfo() {
		String result = extract("http://www.rijiben.com/");
		return result;
	}

	/**
	 * 通过main在本地测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String info = getTodayInHistoryInfo();
		System.out.println(info);
	}
}
