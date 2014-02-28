package com.yeahmobi.gamelala.action.gcm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yeahmobi.gamelala.action.BaseActionSupport;
import com.yeahmobi.gamelala.form.gcm.GcmReportForm;
import com.yeahmobi.gamelala.server.gcm.GcmReportServer;

public class GcmReportAction extends BaseActionSupport {

	/**
	 * 上报GCM的注册ID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String doAutoAction(Object obj, HttpServletRequest req,
			HttpServletResponse resp) throws Exception {

		GcmReportForm form = (GcmReportForm) obj;
		
		return GcmReportServer.getInstance().doGcmReport(form);
	}

	@Override
	public Class<GcmReportForm> getFromBean() {
		return GcmReportForm.class;
	}

}
