package pl.keruzam.service;

import java.io.Serializable;

import jakarta.inject.Inject;
import org.springframework.stereotype.Component;
import pl.keruzam.service.cmm.context.ApplicationContext;

/**
 * Kontekst aplikacji
 *
 * @author Pawel Niedzielan
 */

@Component("applicationContextWww")
public class ApplicationContextWww implements Serializable {

	@Inject
	ApplicationContext appContext;
	@Inject
	ApplicationProfileService applicationProfileService;

	public String getHost() {
		return appContext.getHost();
	}

	public String getNode() {
		return appContext.getNode();
	}

	public String getAppLocation() {
		return appContext.getAppLocation();
	}

	public String getProfile() {
		return appContext.getProfile();
	}

	public String getLocale() {
		return "pl";
	}

	public Integer getVersion() {
		return appContext.getVersion();
	}

	public boolean isProduction() {
		return appContext.isProduction();
	}

	public boolean isAppProduction() {
		return appContext.isAppProduction();
	}

	public boolean isDemoProduction() {
		return appContext.isDemoProduction();
	}

	public String getEmailAdmin() {
		return "t.mazurek@streamsoft.pl";
	}

	public Boolean isDomainCheckerActive() {
		return applicationProfileService.isDomainCheckerActive();
	}

}
