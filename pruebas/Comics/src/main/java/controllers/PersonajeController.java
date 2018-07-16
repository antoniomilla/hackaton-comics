
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.PersonajeService;
import domain.Personaje;

@Controller
@RequestMapping("/personaje")
public class PersonajeController {

	@Autowired
	private PersonajeService	personajeService;


	public PersonajeController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Personaje> personajes;

		personajes = this.personajeService.findAll();
		result = new ModelAndView("personaje/list");
		result.addObject("requestURI", "personaje/list.do");
		result.addObject("personajes", personajes);

		return result;
	}

}
