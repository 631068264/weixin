package org.weixin.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weixin.base.BaseDao;
import org.weixin.base.BaseServiceImpl;
import org.weixin.domain.entity.UserLocation;
import org.weixin.domain.response.Article;
import org.weixin.domain.service.BaiduPlace;
import org.weixin.service.UserLocationService;
import org.weixin.service.extra.MapService;
import org.weixin.utils.ReplyUtil;
import org.weixin.utils.ValidUtil;

@Service
@Transactional
public class UserLocationServiceImpl extends BaseServiceImpl<UserLocation> implements
		UserLocationService {

	@Resource
	public void setDao(BaseDao<UserLocation> dao) {
		super.setDao(dao);
	}

	public boolean saveInfo(Map<String, String> requestMap) {
		UserLocation userLocation = new UserLocation();

		String fromUserName = requestMap.get("FromUserName");
		// 用户发送的经纬度
		String lat = requestMap.get("Location_X");// 纬度
		String lng = requestMap.get("Location_Y");// 经度
		// 坐标转换后的经纬度
		String bd09Lng = null;
		String bd09Lat = null;
		// 调用接口转换坐标
		UserLocation temp = MapService.convertCoord(lng, lat);
		if (null != temp) {
			bd09Lng = temp.getBd09Lng();
			bd09Lat = temp.getBd09Lat();
		}

		// 入库
		userLocation.setOpenId(fromUserName);
		userLocation.setLng(lng);
		userLocation.setLat(lat);
		userLocation.setBd09Lat(bd09Lat);
		userLocation.setBd09Lng(bd09Lng);

		return this.save(userLocation);
	}

	public String findPlace(String key, ReplyUtil reply, Map<String, String> requestMap,
			HttpServletRequest request) {
		// 获取最后一次保存地理位置
		String hql = "from UserLocation u where u.openId = ? order by u.id desc";
		List<UserLocation> userLocationList = this.batchResultsByHQL(hql,
				requestMap.get("FromUserName"));

		if (!ValidUtil.isValid(userLocationList)) {// 没有保存
			return reply.sendText("请发送一次地理位置");
		} else {
			UserLocation location = userLocationList.get(0);
			List<BaiduPlace> placeList = MapService.searchPlace(key, location.getBd09Lng(),
					location.getBd09Lat());
			if (!ValidUtil.isValid(placeList)) {
				return reply.sendText(String.format("/难过 ，附近木有  %s ", key));
			} else {
				List<Article> articleList = MapService.makeArticleList(placeList,
						location.getBd09Lng(), location.getBd09Lat(), request);
				return reply.sendNewsMessage(articleList);
			}

		}

	}
}
