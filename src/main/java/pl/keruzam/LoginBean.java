package pl.keruzam;


import jakarta.enterprise.context.RequestScoped;
import org.springframework.stereotype.Component;

@Component
@RequestScoped
public class LoginBean {

	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String login() {
		// Your login logic here
		if ("user".equals(username) && "pass".equals(password)) {
			return "home?faces-redirect=true";
		} else {
			return "login?faces-redirect=true&error=true";
		}
	}
}
