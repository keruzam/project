package pl.keruzam.service.cmm;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;
import pl.keruzam.service.cmm.lib.ConstIp;

/**
 *
 * @author Tomasz Mazurek
 *
 */
public class HttpServletRequestExecutor {

	public static boolean isStreamsoftIP() {
		return ConstIp.STREAMSOFT_IP.equals(getClientIpAddr());
	}

	public static String getClientIpAddr() {
		String ip = getClientIpFromRequest();
		return emptyIp(ip) ? ConstIp.CLIENT_EMPTY_IP : ip;
	}

	public static String getHeader(final String name) {
		HttpServletRequest request = getRequest();
		return request.getHeader(name);
	}

	public static String getAuthHeader() {
		return getHeader("Authorization");
	}

	private static String getClientIpFromRequest() {
		String ip = "";
		HttpServletRequest request = null;
		try {
			request = getRequest();
		} catch (Exception e) {
			return ConstIp.CLIENT_EMPTY_IP;
		}
		/** W pierwszej kolejności ip z mobilki */
		ip = request.getHeader("clientIpAddr");
		if (emptyIp(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (emptyIp(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (emptyIp(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (emptyIp(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (emptyIp(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (emptyIp(ip)) {
			ip = request.getRemoteAddr();
		}
		if (emptyIp(ip)) {
			ip = ConstIp.CLIENT_EMPTY_IP;
		}
		return ip;
	}

	private static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}

	private static Boolean emptyIp(final String ip) {
		return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
	}

}
