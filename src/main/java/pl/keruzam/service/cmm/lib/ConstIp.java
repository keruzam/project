package pl.keruzam.service.cmm.lib;

/**
 * Stale adresy sieciowe
 * 
 * @author tomasz.mazurek
 * 
 */
public class ConstIp {

	public static final String STREAMSOFT_IP = "81.18.219.18";
	public static final String LOCALHOST = "127.0.0.1";
	public static final String LOCALHOST_IP6 = "0:0:0:0:0:0:0:1";

	public static final String LOCAL_NETWORK_PREFIX = "192.168.72";
	public static final String TRUNK_IP_ADDRESS = "192.168.72.238";
	public static final String NEW_DEV = "192.168.70.185";

	public static final String CLIENT_EMPTY_IP = "123.123.123.123";

	public static boolean isIpAddressFromStreamsoft(final String remoteAddr) {
		return LOCALHOST.equals(remoteAddr) || LOCALHOST_IP6.equals(remoteAddr) || NEW_DEV.equals(remoteAddr)
				|| STREAMSOFT_IP.equals(remoteAddr) || remoteAddr.startsWith(LOCAL_NETWORK_PREFIX);
	}
}
