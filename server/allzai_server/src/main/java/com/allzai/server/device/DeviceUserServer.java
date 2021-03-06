package com.allzai.server.device;

import org.apache.log4j.Logger;

import com.allzai.bean.MobileDeviceBean;
import com.allzai.dao.AutoBeanDao;
import com.allzai.dao.decive.DeviceUserDao;
import com.allzai.form.device.DeviceUserForm;
import com.allzai.util.StringUtil;
import com.restfb.json.JsonObject;

public class DeviceUserServer {

	private static final Logger logger = Logger.getLogger(DeviceUserServer.class);

	private static final DeviceUserServer deviceUserServer = new DeviceUserServer();

	public static DeviceUserServer getInstance() {
		return deviceUserServer;
	}

	public JsonObject doDeviceUser(DeviceUserForm form) throws Exception {

		JsonObject json = new JsonObject();
		
		try {
			/**
			 * Ax0003:参数错误
			 */
			if (form == null || (StringUtil.isEmpty(form.getImei()) && StringUtil.isEmpty(form.getMac()))) {
				json.put("result", Boolean.FALSE);
				json.put("code", "Ax0003");
				return json;
			}

			logger.info("Device Report, imei = " + form.getImei() + ", mac = " + form.getMac());

			MobileDeviceBean device = DeviceUserDao.getInstance().getDeviceUser(form.getImei());
			if (device == null) {
				device = form.convertBaseBean();
				device.setSdk_ver(form.getVer());
				AutoBeanDao.getInstance().getAddSingleBean(device);
			} else {
				// 系统版本或SDK版本变更时, 同步更新设备信息
				if (!form.getVersion().equals(device.getVersion()) 
						|| !form.getVer().equals(device.getSdk_ver())) {
					device.setVersion(form.getVersion());
					device.setSdk_ver(form.getVer());
					AutoBeanDao.getInstance().editSingleBean(device);
				}
			}

			/**
			 * Ax0000:设备上报成功
			 */
			json.put("result", Boolean.TRUE);
			json.put("code", "Ax0000");
			return json;
		} catch (Exception e) {
			
			/**
			 * Ax0001:内部异常
			 */
			json.put("result", Boolean.FALSE);
			json.put("code", "Ax0001");
			return json;
		}
	}

}
