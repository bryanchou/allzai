package com.allzai.action;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.allzai.des3.ThreeDESUtil;
import com.allzai.util.Constants;
import com.allzai.util.Hosts;
import com.allzai.util.LangUtil;
import com.allzai.util.StringUtil;
import com.restfb.json.JsonObject;

/**
 * 响应动作支持类，返回类型为JSON<p>
 */
public abstract class BaseActionSupport extends HttpServlet 
{

	private static final long serialVersionUID = 7371056226740639176L;
	
	private static final Logger logger = Logger.getLogger(BaseActionSupport.class);
	
	/**
	 * 自动响应执行结果<p>
	 * 
	 * @param  input
	 * @param  req
	 * @param  resp 
	 * @return String 执行结果
	 * @throws Exception
	 */
	public abstract JsonObject doAutoAction(Object obj, HttpServletRequest req, HttpServletResponse resp) throws Exception;
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException 
	{
		/**
		 * -10000:不支持GET请求
		 */
		try {
			resp.getWriter().append(Hosts.InvalidRequestResponse(Hosts.getIpAddr(req),  "-1x0000"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException 
	{
		try {
			this.initService(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initService(HttpServletRequest req, HttpServletResponse resp)
			throws Exception 
	{
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/xml");
		
		String ip = Hosts.getIpAddr(req);
		String lang = LangUtil.defaultLang;

		Object obj = null;
		try 
		{
			//解析浏览器
			String user_agent = req.getHeader("User-Agent");
			if(!StringUtil.isEmpty(user_agent)) {
				if(!user_agent.endsWith("UA-lalasdk")) {
					/**
					 * -1x0007:错误的引擎
					 */
					resp.getWriter().append(Hosts.InvalidRequestResponse(ip,  "-1x0007"));
					return;
				}
			} else {
				/**
				 * -1x0006:没有对UA封装
				 */
				resp.getWriter().append(Hosts.InvalidRequestResponse(ip, "-1x0006"));
				return;
			}

			if(getFromBean() != null)
			{
				obj = getFromBean().newInstance();
				
				//解析加密串
				String index = req.getParameter("index");
				String gll = req.getParameter("GLL");
				if(!StringUtil.isEmpty(gll) && !StringUtil.isEmpty(index)) {
					int key = Integer.parseInt(index);
					if(key <= 0 || key >= ThreeDESUtil.get3desKeyLength()) {
						/**
						 * -1x0003:加密下标错误
						 */
						resp.getWriter().append(Hosts.InvalidRequestResponse(ip, "-1x0003"));
						return;
					}
					
					gll = ThreeDESUtil.Decode(gll, key);
				} else {
					/**
					 * -1x0005:没有加密参数
					 */
					resp.getWriter().append(Hosts.InvalidRequestResponse(ip, "-1x0005"));
					return;
				}
				
				//封装参数项
				Map<String, Object> map = Hosts.getReqKeys(gll);
				if(!map.containsKey("imei") 
						|| !map.containsKey("mac") 
						|| !map.containsKey("ver") 
						|| !map.containsKey("platform")) {
					/**
					 * -1x0009:必填参数缺少时, 不允许通过
					 */
					resp.getWriter().append(Hosts.InvalidRequestResponse(ip, "-1x0009"));
					return;
				}
				
				String imei = String.valueOf(map.get("imei"));
				String mac = String.valueOf(map.get("mac"));
				if(StringUtil.isEmpty(imei) && StringUtil.isEmpty(mac)) {
					/**
					 * -1x0010:IMEI和MAC同时为空
					 */
					resp.getWriter().append(Hosts.InvalidRequestResponse(ip, "-1x0010"));
					return;
				}
				
				map.put("ip", ip);
				if(!map.containsKey("lang") 
						|| StringUtil.isEmpty(String.valueOf(map.get("lang")))) {
					map.put("lang", "EN");
				}
				lang = String.valueOf(map.get("lang"));
				if(StringUtil.isEmpty(imei)) {map.put("imei", mac);}

				//对tk进行校验
				if(map.containsKey("userId")) {
					
					boolean ok = false;
					try {
						String h_tk = req.getHeader("tk");
						if(map.containsKey("tk") &&  !StringUtil.isEmpty(h_tk)) {
							
							if(h_tk.equals(String.valueOf(map.get("tk")))) {
								h_tk = ThreeDESUtil.Decode(h_tk, Constants.index_tk_deocde);
								
								String[] keys = h_tk.split("_");
								if(keys.length == 3 
										&& keys[0].equals(String.valueOf(map.get("userId"))) 
										//&& keys[1].equals(String.valueOf(map.get("imei"))) //设备号存在取不到的情况, 忽略这个校验
										&& (keys[2]).equals("0") || keys[2].equals("1")) {
									ok = true;
								}
								keys = null;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if(!ok) {
						/**
						 * -1x0008:校验tk参数错误
						 */
						resp.getWriter().append(Hosts.InvalidRequestResponse(ip, "-1x0008"));
						return;
					}
				}
				
				logger.info("ip = " + ip + ", gll = " + gll);
				BeanUtils.populate(obj, map);
				logger.info("ip = " + ip + ", obj = " + obj.toString());
			} else {
				/**
				 * -1x0004:没有参数
				 */
				resp.getWriter().append(Hosts.InvalidRequestResponse(ip, "-1x0004"));
				return;
			}
		} 
		catch (Exception e) 
		{
			logger.warn("parse parameter error: ", e);
			
			/**
			 * -1x0001:内部异常
			 */
			resp.getWriter().append(Hosts.InvalidRequestResponse(ip, "-1x0001"));
			return;
		}

		try 
		{
			JsonObject json = doAutoAction(obj, req, resp);
			json.put("info", LangUtil.getCodeInfoByLang(lang, json.getString("code")));
			
			logger.info("ip = " + ip + ", ret = " + json.toString());
			
			 resp.getWriter().append(ThreeDESUtil.Encode(json.toString(), Constants.index_az_decode));
		} 
		catch (Exception e) 
		{
			logger.error("Failed: Method is called doAutoAction failure!", e);

			/**
			 * -1x0002:自动处理异常
			 */
			resp.getWriter().append(Hosts.InvalidRequestResponse(ip, "-1x0002"));
		}
	}

	/**
	 * 匹配接口参数类型
	 * 
	 * @return
	 */
	public abstract Class<?> getFromBean();
}

