
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import domain.Admin;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {

	@Autowired
	private AdministratorService	administradorService;


	public AdministradorController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Admin> administradores;

		administradores = this.administradorService.findAll();
		result = new ModelAndView("administrador/list");
		result.addObject("requestURI", "administrador/list.do");
		result.addObject("administradores", administradores);

		return result;
	}

}
