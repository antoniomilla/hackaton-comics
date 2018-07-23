
package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.UserAccount;
import security.UserAccountService;
import services.UserService;
import domain.User;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService			userService;
	@Autowired
	private UserAccountService	userAccountService;


	public UserController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<User> clientes;

		clientes = this.userService.findAll();
		result = new ModelAndView("cliente/list");
		result.addObject("requestURI", "cliente/list.do");
		result.addObject("clientes", clientes);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createCustomer() {
		final User user = this.userService.create();
		final UserAccount ua = this.userAccountService.create();
		user.setUserAccount(ua);
		final ModelAndView result = this.createModelAndView(user, null);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView saveCustomer(@Valid final User user, final BindingResult binding) {
		ModelAndView res;
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		if (binding.hasErrors()) {
			System.out.print(binding.getAllErrors());
			res = this.createModelAndView(user, null);
		} else {
			user.getUserAccount().setPassword(encoder.encodePassword(user.getUserAccount().getPassword(), null));
			final User userSaved = this.userService.save(user);
			res = new ModelAndView("security/login");
			res.addObject("credentials", userSaved.getUserAccount());
		}
		return res;
	}
	protected ModelAndView createModelAndView(final User client, final String message) {
		ModelAndView result;

		result = new ModelAndView("user/create");
		result.addObject("user", client);
		result.addObject("message", message);

		return result;
	}
}
