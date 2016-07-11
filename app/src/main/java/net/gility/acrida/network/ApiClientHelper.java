package net.gility.acrida.network;

import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.config.AppConfig;

public class ApiClientHelper {
	
	/**
	 * 获得请求的服务端数据的userAgent
	 * @param applicationLoader
	 * @return
	 */
	public static String getUserAgent(ApplicationLoader applicationLoader) {
		StringBuilder ua = new StringBuilder("OSChina.NET");
		ua.append('/' + applicationLoader.getPackageInfo().versionName + '_'
				+  applicationLoader.getPackageInfo().versionCode);// app版本信息
		ua.append("/Android");// 手机系统平台
		ua.append("/" + android.os.Build.VERSION.RELEASE);// 手机系统版本
		ua.append("/" + android.os.Build.MODEL); // 手机型号
		ua.append("/" + AppConfig.getInstance().getAppId());// 客户端唯一标识
		return ua.toString();
	}
}
