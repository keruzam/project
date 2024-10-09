package pl.keruzam.service.cmm;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIForm;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.util.Callbacks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.keruzam.service.cmm.lib.Date;
import pl.keruzam.service.cmm.lib.PageUrl;

/**
 * Bazowy kontroler
 *
 * @author Mirek Szajowski
 */
public class AbstractController implements Serializable {

	public static final String DEFAULT_MSG_BUNDLE = "msg";
	protected static final String LINK_PARAM = "link";
	protected static final String SETTINGS_SAVED = "settingsSaved";
	protected static final String ID_PARAM = "id";
	protected static final Logger logger = LoggerFactory.getLogger(Loggers.DIAGNOSTIC);
	private static final String DEFAULT_FILE_NAME = "document.pdf";
	private static final List<String> FORMS_CANDIDATES = Arrays.asList("mainForm");
	private static final String ID_DELETE_PARAM = "idDelete";
	private static final String SHOW_MSG_COMMAND_SEPARATOR = "', '";
	private static final String SHOW_NONSTICKY_MSG_COMMAND_BEGIN = "showNonStickyMsg('";
	private static final String SHOW_STICKY_MSG_COMMAND_BEGIN = "showStickyMsg('";
	private static final String SHOW_STICKY_MSG_COMMAND_END = "');";
	protected Long idSelectedRow;

	/**
	 * Przechodzi do podanej strony
	 */
	public String goTo(final String path) {
		return "/views" + path + PageUrl.FACES_REDIRECT;
	}

	public String goToInfoPage(final String path) {
		return "/info" + path + PageUrl.FACES_REDIRECT;
	}

	public void deleteElement() {
		ExternalContext externalContext = getExternalContext();
		Map map = externalContext.getRequestParameterMap();
		Object object = map.get(ID_DELETE_PARAM);
		Long id = Long.valueOf(object.toString());
		delete(id);
	}

	protected ExternalContext getExternalContext() {
		if (FacesContext.getCurrentInstance() != null) {
			return FacesContext.getCurrentInstance().getExternalContext();
		}
		return null;
	}

	/**
	 * Przechodzi do strony domowej
	 */
	public String goToHomePage() {
		return "/home?faces-redirect=true";
	}

	public void redirectToLogoutPage() {
		try {
			getExternalContext().redirect(getAppContextPath() + "/logout");
		} catch (IOException e) {
			// ignore
		}
	}

	public void resetModalForm() {
	}

	/**
	 * Przechodzi do strony logowania
	 */
	public String goToLoginPage(final String login) {
		return "/login?faces-redirect=true&login=" + login;
	}

	/**
	 * Dodaje paramery do strony a=b
	 */
	public String with(final String current, final String name, final String value) {
		return current + "&" + name + "=" + value;
	}

	/**
	 * Odswieza aktualna strone
	 */
	public String refreshCurrentPage() {
		return null;
	}

	/**
	 * Pobiera aktualny identyfikator(id)
	 */
	public Long getId() {
		Long id = null;
		String string = getParam(ID_PARAM);
		if (StringUtil.isNotEmpty(string)) {
			id = Long.valueOf(string);
		}
		return id;
	}

	/**
	 * Pobiera parametr o podanej nazwie (zdefiniowana klasa)
	 */
	public <I> I getParam(final String name, final Class<I> type) {
		try {
			String param = getParam(name);
			if (!StringUtil.isEmpty(param) && !param.equals("null")) {
				return type.getConstructor(String.class).newInstance(param);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	/**
	 * Pobiera parametr daty
	 */
	public Date getDateParam(final String paramName) {
		String dateString = getParam(paramName);
		if (!StringUtil.isEmpty(dateString)) {
			return Date.from(dateString, Date.YYYY_MM_DD);
		}
		return null;
	}

	/**
	 * Pobiera parametr o podanej nazwie
	 */
	public String getParam(final String name) {
		return getExternalContext() == null ? "" : getExternalContext().getRequestParameterMap().get(name);
	}

	/**
	 * Pokazuje komunikat uzytkownikow
	 */
	protected void showMessage(final String resourceKey, final Object... args) {
		showMessage(FacesMessage.SEVERITY_INFO, resourceKey, args);
	}

	protected void showErrorMessage(final String resourceKey, final Object... args) {
		showMessage(FacesMessage.SEVERITY_ERROR, resourceKey, args);
	}

	protected void showWarnMessage(final String resourceKey, final Object... args) {
		showMessage(FacesMessage.SEVERITY_WARN, resourceKey, args);
	}

	/**
	 * Pokazuje komunikat uzytkownikow dla pola
	 */
	public void showFieldMessage(final String field, final String resourceKey, final Object... args) {
		showFieldMessage(FacesMessage.SEVERITY_INFO, field, resourceKey, args);
	}

	/**
	 * Naprawia wyswietlanie growl po pokazaniu mesage
	 */
	protected void updateGrowlAfterShowMessage() {
		updateView("growl");
	}

	/**
	 * Pokazuje komunikat uzytkownikow dla pola
	 */
	protected void showFieldMessage(final FacesMessage.Severity severity, final String field, final String resourceKey, final Object... args) {
		FacesContext context = FacesContext.getCurrentInstance();
		String message = getMessage(resourceKey, args);
		String id = findFieldId(field);
		context.addMessage(id, new FacesMessage(severity, message, null));
		updateGrowlAfterShowMessage();
	}

	protected void showFieldMessage(final FacesMessage.Severity severity, final String field, final String[] formCandidates, final String resourceKey,
			final Object... args) {
		FacesContext context = FacesContext.getCurrentInstance();
		String message = getMessage(resourceKey, args);
		String id = findFieldId(field, formCandidates);
		context.addMessage(id, new FacesMessage(severity, message, null));
		updateGrowlAfterShowMessage();
	}

	protected void showErrorText(final String text) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, text, null));
	}

	/**
	 * Pobiera komunikat z message.properties
	 */
	protected String getMessage(final String resourceKey, final Object... args) {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = context.getApplication().getResourceBundle(context, DEFAULT_MSG_BUNDLE);
		String message;
		try {
			message = bundle.getString(resourceKey);
			if (containsArgs(args)) {
				message = MessageFormat.format(message, args);
			}
		} catch (Exception e) {
			message = resourceKey;
		}
		return message;
	}

	private String findFieldId(final String field) {
		String id = null;
		if (field != null) {
			for (String formId : FORMS_CANDIDATES) {
				UIComponent component = FacesContext.getCurrentInstance().getViewRoot().findComponent(formId);
				id = findFieldId(component.getChildren(), field + "Field");
				if (id != null) {
					break;
				}
			}
		}
		return id;
	}

	private String findFieldId(final String field, final String[] formCandidates) {
		String id = null;
		if (field != null) {
			for (String formId : formCandidates) {
				UIComponent component = FacesContext.getCurrentInstance().getViewRoot().findComponent(formId);
				id = findFieldId(component.getChildren(), field + "Field");
				if (id != null) {
					break;
				}
			}
		}
		return id;
	}

	private boolean containsArgs(final Object... args) {
		return args != null && args.length > 0;
	}

	private String findFieldId(final List<UIComponent> components, final String field) {
		for (UIComponent component : components) {
			String clientId = component.getClientId();
			if (clientId.endsWith(field)) {
				((UIInput) component).setValid(false);
				return clientId;
			}
			if (!component.getChildren().isEmpty()) {
				String findFieldId = findFieldId(component.getChildren(), field);
				if (findFieldId != null) {
					return findFieldId;
				}
			}
		}
		return null;
	}

	/**
	 * Pokazuje komunikat uzytkownikow
	 */
	protected void showMessage(final FacesMessage.Severity severity, final String resourceKey, final Object... args) {
		showFieldMessage(severity, null, resourceKey, args);
	}

	/**
	 * Pokazuje komukniakt o wykonaniu operacji
	 */
	protected void showActionDoneMsg() {
		showMessage("action.done");
	}

	/**
	 * Usuwa wiersz tabeli o podanym id
	 */
	public void delete(final Long id) {

	}

	public ServletOutputStream getPDFResponseOutputStream(final String fileName) {
		String name = fileName;
		if (name == null || name.isEmpty()) {
			name = DEFAULT_FILE_NAME;
		} else {
			name = name.replaceAll(" ", "_");
			name = StringUtil.replacePolishChars(name);
			name += ".pdf";
		}
		HttpServletResponse response = (HttpServletResponse) getExternalContext().getResponse();
		response.reset();
		response.setContentType("application/pdf");
		response.setHeader("Content-Description", "File Transfer");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Disposition", "attachment; filename=" + name);
		response.setHeader("Cache-Control", "cache, must-revalidate");
		response.setHeader("Expires", "0");
		response.setHeader("Pragma", "public");
		ServletOutputStream servletOutputStream = null;
		try {
			servletOutputStream = response.getOutputStream();
		} catch (Exception e) {
			// FIXME logger
		}
		return servletOutputStream;
	}

	public String getAppContextPath() {
		Object request = getExternalContext().getRequest();
		if (request instanceof HttpServletRequest) {
			String uri = ((HttpServletRequest) request).getRequestURI();
			String url = ((HttpServletRequest) request).getRequestURL().toString();
			String contextPath = ((HttpServletRequest) request).getContextPath();
			return url.substring(0, url.length() - uri.length()) + contextPath;
		} else {
			return "";
		}
	}

	public String getRequestUri() {
		Object request = getExternalContext().getRequest();
		if (request instanceof HttpServletRequest) {
			String uri = ((HttpServletRequest) request).getRequestURI();
			return uri.substring(uri.indexOf("views") + 5);
		} else {
			return "";
		}
	}

	public String getRequestUrl() {
		Object request = getExternalContext().getRequest();
		if (request instanceof HttpServletRequest) {
			return ((HttpServletRequest) request).getRequestURL().toString();
		} else {
			return "";
		}
	}

	/**
	 * Lata bag z podpowiadaniem wartosci po walidacji
	 *
	 * @param uiComponent
	 */
	protected void resetFormState(final UIComponent uiComponent) {
		// get form component
		UIComponent parentComponent = uiComponent.getParent();
		if (uiComponent instanceof UIForm) {
			resetFields(uiComponent);
		} else if (parentComponent != null) {
			resetFormState(parentComponent);
		} else {
			resetFields(uiComponent);
		}
	}

	private void resetFields(final UIComponent baseComponent) {
		for (UIComponent c : baseComponent.getChildren()) {
			if (c.getChildCount() > 0) {
				resetFields(c);
			}
			if (c instanceof UIInput) {
				((UIInput) c).resetValue();
			}
		}
	}

	protected void showDialog(final String dialogName) {
		executeAction("PF('" + dialogName + "').show();");
	}

	protected void executeAction(final String action) {
		PrimeFaces.current().executeScript(action);
	}

	protected void hideDialog(final String dialogName) {
		executeAction("PF('" + dialogName + "').hide();");
	}

	protected void updateView(final String idFieldName) {
		PrimeFaces.current().ajax().update(idFieldName);
	}

	protected void redirect(final String pageName) {
		String url = createUrl(pageName);
		try {
			getExternalContext().redirect(url);
		} catch (IOException e) {
			// do nothing
		}
	}

	protected void redirectPartner(final String pageName) {
		String url = createUrlPartner(pageName);
		try {
			getExternalContext().redirect(url);
		} catch (IOException e) {
			// do nothing
		}
	}

	private String createUrl(final String pageName) {
		return getAppContextPath() + goTo(pageName + ".do");
	}

	private String createUrlPartner(final String pageName) {
		return getAppContextPath() + "/partner/" + goTo(pageName + ".do");
	}

	protected Map<String, String> getRequestParameterMap() {
		return getExternalContext().getRequestParameterMap();
	}

	/**
	 * Wyswietla info w okienku, ktore nie zamknie sie tak dlugo jak nie przeladuje sie strony, lub uzytkownik sam go nie zamknie
	 */
	protected void showMessageInStickyGrowl(final FacesMessage.Severity severity, final String msg) {
		PrimeFaces.current().executeScript(createShowMessageGrowlRequestFunction(SHOW_STICKY_MSG_COMMAND_BEGIN, severity, msg));
	}

	/**
	 * Wyswietla info w okienku, ktore nie jest automatycznie zamykane przy AJAX-ie
	 */
	protected void showMessageInNonStickyGrowl(final FacesMessage.Severity severity, final String msg) {
		PrimeFaces.current().executeScript(createShowMessageGrowlRequestFunction(SHOW_NONSTICKY_MSG_COMMAND_BEGIN, severity, msg));
	}

	protected void showInfoMessage(final String msg) {
		showMessageInNonStickyGrowl(FacesMessage.SEVERITY_INFO, msg);
	}

	private String createShowMessageGrowlRequestFunction(final String commandBegin, final FacesMessage.Severity severity, final String msg) {
		// @formatter:off
		return commandBegin
					+ msg 
					+ SHOW_MSG_COMMAND_SEPARATOR
					+ severity.toString().toLowerCase()
					+ SHOW_STICKY_MSG_COMMAND_END;
	}
		// @formatter:off
	
	/**
	 * Czy kontroler sam zarzada prezentacja wyjatkow
	 */
	protected boolean ownExceptionPresentationEnabled() {
		return false;
	}

	/**
	 * Obsluga prezentacji wyjatkow
	 */
	public void handleException(final Exception e) {
		
	}

	protected String getBackUrl(final String defaultUrl) {
		String backToParam = getParam("backTo");
		String url = ConstRedirectLinks.getUrl(backToParam);
		if (url != null && url.length() > 0) {
			return url;
		} else {
			return defaultUrl;
		}
	}
	
	public void goBackToPrev(){
		PrimeFaces.current().executeScript("backToPreviousPage");
	}
	
	public void resetAndUpdate(final String idField){
		PrimeFaces.current().resetInputs(idField);
		PrimeFaces.current().ajax().update(idField);
	}

	public DefaultStreamedContent buildDefaultStreamedContent(InputStream is, String contentType, String name) {
		return DefaultStreamedContent.builder().stream((Callbacks.SerializableSupplier<InputStream>) is)
				.contentType(contentType).name(name).build();
	}


}
