
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
@RequestMapping("/administrator")
public class AdministratorController {

	@Autowired
	private AdministratorService	administratorService;


	public AdministratorController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Admin> administrators;

		administrators = this.administratorService.findAll();
		result = new ModelAndView("administrator/list");
		result.addObject("requestURI", "administrator/list.do");
		result.addObject("administrators", administrators);

		return result;
	}

}
