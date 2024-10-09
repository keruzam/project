package pl.keruzam.service.cmm.context;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.URL;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import pl.keruzam.service.cmm.Profiles;
import pl.keruzam.service.cmm.lib.ConstHost;

/**
 * Kontekst aplikacji
 *
 * @author Mirek Szajowski
 */

public class ApplicationContext implements Serializable {

	public static final String SERVICE_PASSWORD = "firmino321";
	public static final String FIRMINO_BASE_URL = ConstHost.FIRMINO_BASE_URL;
	public static final String FIRMINO_URL = ConstHost.FIRMINO_URL;
	public static final String FIRMINO_DEMO_URL = ConstHost.FIRMINO_DEMO_URL;
	public static final String PARTNER_FIRMINO_URL = ConstHost.PARTNER_FIRMINO_URL;
	public static final String APP_LOCATION = "appLocation";
	private static final String PL = "pl";
	public static final String LOCALE = PL;
	private static final int PORT_APP_FIRST_INSTANCE = 8180;
	private static final int PORT_APP_SECOND_INSTANCE = 8280;
	private static final int PORT_DEMO = 8380;
	private final String appLocation;
	private final String host;
	private final int port;
	private final String fileDir;
	private final Integer version;
	Logger logger = LoggerFactory.getLogger(ApplicationContext.class);
	@Inject
	Environment environment;
	private String node = "uknown";

	//private Ehcache cache;

	ApplicationContext() {
		this.appLocation = "";
		this.host = "host";
		this.port = 0;
		this.fileDir = "";
		this.version = 0;
	}

	ApplicationContext(String appLocation, String host, int port, String fileDir, Integer version) {
		this.appLocation = appLocation;
		this.host = host;
		this.port = port;
		this.fileDir = fileDir;
		this.version = version;
	}

	public ApplicationContext(final String appLocation, final String fileDir, final Integer version, final Integer port) {
		try {
			this.appLocation = appLocation;
			URL urlObject = new URL(appLocation);
			this.host = urlObject.getHost();
			this.port = port;
			try {
				this.node = InetAddress.getLocalHost().toString();
			} catch (Exception e) {
				// do nothing
			}
			this.version = version;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		this.fileDir = fileDir;
		logger.info("APPLICATION STARTING ... version: " + version + "| Host: " + host + "| port: " + port + "| node" + node);
	}

	private void initCache() {
		//		if (cache == null) {
		//			cache = CacheManager.getInstance().addCacheIfAbsent("applicationContextCache");
		//		}
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getNode() {
		return node;
	}

	public String getAppLocation() {
		return appLocation;
	}

	public String getProfile() {
		return environment.getActiveProfiles()[0];
	}

	public String getLocale() {
		return LOCALE;
	}

	public String getFileDir() {
		return fileDir;
	}

	public Integer getVersion() {
		return version;
	}

	/**
	 * Czy system jest na produkcji
	 */
	public boolean isProduction() {
		return appLocation.contains(FIRMINO_BASE_URL);
	}

	/**
	 * Czy system jest na produkcji i jest to app
	 */
	public boolean isAppProduction() {
		return Profiles.PRODUCTION.equals(getProfile()) && appLocation.contains(FIRMINO_URL);
	}

	/**
	 * Czy moze byc puszczony timer systemowy (tylko na app1 produkcyjnym port:8180 - pierwsza instancja)
	 */
	public boolean isAppProductionOnPort8180() {
		return isAppProduction() && isFirstInstancePort();
	}

	private boolean isFirstInstancePort() {
		return this.port == PORT_APP_FIRST_INSTANCE;
	}

	/**
	 * Czy system jest na produkcji i jest to demo
	 */
	public boolean isDemoProduction() {
		return Profiles.DEMO.equals(getProfile()) && appLocation.contains(FIRMINO_DEMO_URL);
	}

	/**
	 * Czy system jest na produkcji i jest to portal partnera
	 */
	public boolean isPartnerProduction() {
		return Profiles.PRODUCTION.equals(getProfile()) && appLocation.contains(PARTNER_FIRMINO_URL);
	}

	public Serializable get(final String key) {
		//		initCache();
		//		Element element = cache.get(key);
		//		if (element != null) {
		//			return element.getValue();
		//		}
		return null;
	}

	public void put(final String key, final Serializable value) {
		//		initCache();
		//		cache.put(new Element(key, value));
	}

	public void remove(final String key) {
		//		initCache();
		//		cache.remove(key);
	}

	public boolean isTestProfile() {
		return Profiles.TESTING.equals(getProfile());
	}
}
