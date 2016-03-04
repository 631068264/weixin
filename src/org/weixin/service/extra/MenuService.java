package org.weixin.service.extra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.weixin.domain.menu.Button;
import org.weixin.domain.menu.ClickButton;
import org.weixin.domain.menu.Menu;
import org.weixin.domain.menu.ParentButton;
import org.weixin.domain.menu.ViewButton;
import org.weixin.utils.CommonUtil;
import org.weixin.utils.MenuUtil;

public class MenuService {
	private static Logger log = LoggerFactory.getLogger(MenuService.class);

	public static void createMenu() {
		// 第三方用户唯一凭证
		String appId = CommonUtil.appId;
		// 第三方用户唯一凭证密钥
		String appSecret = CommonUtil.appSecret;

		// 调用接口获取access_token
		String accessToken = CommonUtil.getToken(appId, appSecret);

		if (null != accessToken) {
			// 调用接口创建菜单
			boolean result = MenuUtil.createMenu(getMenu(), accessToken);

			// 判断菜单创建结果
			if (result)
				log.info("菜单创建成功！");
			else
				log.info("菜单创建失败，错误码：" + result);
		}
	}

	/**
	 * 组装菜单数据
	 * 
	 * @return
	 */
	private static Menu getMenu() {
		ClickButton btn11 = new ClickButton();
		btn11.setName("开源中国");
		btn11.setType("click");
		btn11.setKey("oschina");

		ClickButton btn12 = new ClickButton();
		btn12.setName("ITeye");
		btn12.setType("click");
		btn12.setKey("iteye");

		ViewButton btn13 = new ViewButton();
		btn13.setName("CocoaChina");
		btn13.setType("view");
		btn13.setUrl("http://www.iteye.com");

		ViewButton btn21 = new ViewButton();
		btn21.setName("淘宝");
		btn21.setType("view");
		btn21.setUrl("http://m.taobao.com");

		ViewButton btn22 = new ViewButton();
		btn22.setName("京东");
		btn22.setType("view");
		btn22.setUrl("http://m.jd.com");

		ViewButton btn23 = new ViewButton();
		btn23.setName("唯品会");
		btn23.setType("view");
		btn23.setUrl("http://m.vipshop.com");

		ViewButton btn24 = new ViewButton();
		btn24.setName("当当网");
		btn24.setType("view");
		btn24.setUrl("http://m.dangdang.com");

		ViewButton btn25 = new ViewButton();
		btn25.setName("苏宁易购");
		btn25.setType("view");
		btn25.setUrl("http://m.suning.com");

		ViewButton btn31 = new ViewButton();
		btn31.setName("多泡");
		btn31.setType("view");
		btn31.setUrl("http://www.duopao.com");

		ViewButton btn32 = new ViewButton();
		btn32.setName("一窝88");
		btn32.setType("view");
		btn32.setUrl("http://www.yi588.com");

		ParentButton mainBtn1 = new ParentButton();
		mainBtn1.setName("技术交流");
		mainBtn1.setSub_button(new Button[] { btn11, btn12, btn13 });

		ParentButton mainBtn2 = new ParentButton();
		mainBtn2.setName("购物");
		mainBtn2.setSub_button(new Button[] { btn21, btn22, btn23, btn24, btn25 });

		ParentButton mainBtn3 = new ParentButton();
		mainBtn3.setName("网页游戏");
		mainBtn3.setSub_button(new Button[] { btn31, btn32 });

		Menu menu = new Menu();
		menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });

		return menu;
		/**
		 * 这是公众号xiaoqrobot目前的菜单结构，每个一级菜单都有二级菜单项
		 * 
		 * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？
		 * 比如，第三个一级菜单项不是“更多体验”，而直接是“幽默笑话”，那么menu应该这样定义 menu.setButton(new
		 * Button[] { mainBtn1, mainBtn2, btn33 });
		 */

	}
}
