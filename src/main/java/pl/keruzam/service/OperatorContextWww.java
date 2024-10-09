package pl.keruzam.service;

import java.io.Serializable;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.PostConstruct;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import org.springframework.stereotype.Component;

import pl.keruzam.service.cmm.HttpServletRequestExecutor;
import pl.keruzam.service.cmm.OperatorPermission;
import pl.keruzam.service.cmm.OperatorRole;
import pl.keruzam.service.cmm.Persistable;
import pl.keruzam.service.cmm.Profiles;

import pl.keruzam.service.cmm.lib.ConstIp;
import pl.keruzam.service.cmm.lib.Date;
import pl.keruzam.service.operator.OperatorDto;
import pl.keruzam.service.operator.OperatorService;

/**
 * Kontekst zalogowanego operatora (WWW)
 *
 * @author Mirek Szajowski
 */
@Component("operatorContextWww")
@SessionScoped
public class OperatorContextWww implements Serializable {

	public static final String BASELINKER_SETTINGS_CONTEXT = "baselinkerSettingsContext";
	private static final String IS_GHOST_COMPANY = "isGhostCompany";
	private static final int ONE_OPERATOR = 1;
	private static final String STREAMSOFT_EMAIL_POSTFIX = "@streamsoft.pl";
	private static final String STYLE_KEY = "styleMode.type";
	private final HashMap<String, Boolean> openTabs = new HashMap<String, Boolean>();
	private final HashMap<String, Object> currentFilters = new HashMap<String, Object>();
	@Inject
	ApplicationProfileService applicationProfileService;
	String login;
	Long idCurrentCompany;
	Boolean isLoadedCompanyActive;
	Long idOperator;
	Long idAccount;
	OperatorRole role;
	List<OperatorPermission> permissions;
	Date creationDate;
	Map<String, Object> contextData = new HashMap<String, Object>();

	Boolean isAccountOwner;

	@Inject
	private ApplicationContextWww applicationContextWww;
	@Inject
	private OperatorService operatorService;
	@Inject
	private OperatorProfileService 	operatorProfileService;

	private String ipAddress;



	@PostConstruct
	public void init() {
		Principal principal = getUserPrincipal();
		if (principal != null) {
			ipAddress = HttpServletRequestExecutor.getClientIpAddr();
			setOperatorContext(principal, ipAddress);
			clearCurrentFilters();

		}
	}



	private Principal getUserPrincipal() {
		if(FacesContext.getCurrentInstance() != null) {
			return FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
		}
		return null;
	}



	public void setOperatorContext(final Principal principal, final String ipAddress) {
		login = principal.getName();
		OperatorDto operatorDto = operatorService.setOperatorContext(login, ipAddress);
		idOperator = operatorDto.getId();
		idAccount = operatorDto.getIdAccount();
		role = operatorDto.getRole();
		permissions = Arrays.asList(operatorDto.getPermisions());
		creationDate = operatorDto.getCreationDate();
	}


	public Boolean isAccountOwner(final Long id) {
		return operatorService.isAccountOwner(id);
	}






	public String getLogin() {
		return login;
	}

	public Long getIdOperator() {
		return idOperator;
	}

	public Long getIdCurrentCompany() {
		return idCurrentCompany;
	}



	public Boolean isAdmin() {
		if (role != null) {
			return role.equals(OperatorRole.ADMIN);
		} else {
			return Boolean.FALSE;
		}
	}

	public void put(final String key, final Object value) {
		contextData.put(key, value);
	}

	public Object get(final String key) {
		return contextData.get(key);
	}

	public Object get(final Class<?> clazz) {
		Object object = get(clazz.getName());
		if (object == null) {
			try {
				Object newInstance = null;
				if (clazz.getSuperclass() == Persistable.class) {
					newInstance = clazz.getConstructor(OperatorProfileService.class).newInstance(operatorProfileService);
				} else {
					newInstance = clazz.newInstance();
				}
				put(clazz.getName(), newInstance);
				return newInstance;
			} catch (Exception e) {
				return null;
			}
		}
		return object;
	}


	public boolean isDemo() {
		return applicationContextWww.getProfile().equals(Profiles.DEMO);
	}



	public Boolean getIsGhostCompany() {
		Boolean isGhost = (Boolean) get(IS_GHOST_COMPANY);
		return isGhost != null ? isGhost : Boolean.FALSE;
	}

	public void setIsGhostCompany(final Boolean isGhost) {
		put(IS_GHOST_COMPANY, isGhost);
	}





	public Boolean getIsAccountOwner() {
		return isAccountOwner;
	}

	public boolean hasGeneralRights(final OperatorPermission permission) {
		if (OperatorRole.ADMIN == role) {
			return true;
		}
		for (OperatorPermission operatorPermission : permissions) {
			if (operatorPermission.toString().contains(permission.toString())) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	public boolean hasRightsToCalendar() {
		return hasRights(OperatorPermission.CALENDAR) || hasRights(OperatorPermission.CALENDAR_OPERATOR);
	}

	public boolean hasRightsToSaleDocuments() {
		return hasRights(OperatorPermission.SALE) || hasRights(OperatorPermission.SALE_OPERATOR);
	}

	public boolean hasRights(final OperatorPermission permision) {
		if (OperatorRole.ADMIN == role) {
			return true;
		}
		return permissions.contains(permision);
	}





	private void clearCurrentFilters() {
		currentFilters.clear();
	}







}
