package pl.keruzam;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;


@Component
@Named
@RequestScoped
public class CsrfToken {

	private String parameterName;
	private String token;

	public String getParameterName() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
			CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
			return csrfToken != null ? csrfToken.getParameterName() : null;
		}
		return null;
	}

	public String getToken() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
			CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
			return csrfToken != null ? csrfToken.getToken() : null;
		}
		return null;
	}
}

