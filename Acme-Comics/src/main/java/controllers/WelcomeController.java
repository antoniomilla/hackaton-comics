/*
 * WelcomeController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Actor;
import security.LoginService;
import security.UserAccountService;
import services.ActorService;
import domain.User;
import utilities.ControllerUtils;
import utilities.UserAccountUtils;

@Controller
@RequestMapping("/welcome")
public class WelcomeController extends AbstractController {
	// Constructors -----------------------------------------------------------

	public WelcomeController() {
		super();
	}

	// Index ------------------------------------------------------------------		

	@RequestMapping(value = "/index")
	public ModelAndView index(@RequestParam(required = false, defaultValue = "John Doe") final String name, final HttpServletRequest request, final HttpServletResponse response) throws LoginException {
		ModelAndView result;
		SimpleDateFormat formatter;
		String moment;
		result = new ModelAndView("welcome/index");
		formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		moment = formatter.format(new Date());


		return result;
	}

	@RequestMapping(value = "/blocked")
	public ModelAndView blocked()
	{
		Actor principal = findPrincipal();
		if (principal instanceof User && ((User) principal).getBlocked()) {
			ModelAndView result = new ModelAndView("welcome/blocked");
			result.addObject("user", principal);

			// Log user out.
			UserAccountUtils.setSessionAccount(null);
			return result;
		}

		// If not actually blocked, redirect to index.
		return ControllerUtils.redirect("/welcome/index.do");
	}
}
