package pl.keruzam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class SecurityController {

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/perform_login")
	public String performLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated()) {
			return "redirect:/views/home.xhtml";
		}
		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";  // redirect to login page after logout
	}
}