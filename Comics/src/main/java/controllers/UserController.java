
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.UserService;
import domain.User;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService	clienteService;


	public UserController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<User> clientes;

		clientes = this.clienteService.findAll();
		result = new ModelAndView("cliente/list");
		result.addObject("requestURI", "cliente/list.do");
		result.addObject("clientes", clientes);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createCustomer() {
		final User customer = this.clienteService.create();

		final ModelAndView result = this.createModelAndView(customer, null);

		return result;
	}
	protected ModelAndView createModelAndView(final User client, final String message) {
		ModelAndView result;

		result = new ModelAndView("client/create");
		result.addObject("client", client);

		return result;
	}
}
