
package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.PublishingCompanyService;
import services.PersonajeService;
import domain.ComicCharacter;
import domain.Comic;
import domain.Publisher;

@Controller
@RequestMapping("/personaje")
public class PersonajeController {

	@Autowired
	private PersonajeService	personajeService;
	@Autowired
	private PublishingCompanyService	editorialService;


	public PersonajeController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<ComicCharacter> personajes;

		personajes = this.personajeService.findAll();
		result = new ModelAndView("personaje/list");
		result.addObject("requestURI", "personaje/list.do");
		result.addObject("personajes", personajes);

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int personajeId) {
		ModelAndView result;
		final ComicCharacter personaje = this.personajeService.findOne(personajeId);
		final Collection<Comic> comics = personaje.getAppearsIn();
		result = new ModelAndView("personaje/display");
		result.addObject("personaje", personaje);
		result.addObject("comics", comics);

		return result;
	}
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		ComicCharacter personaje;

		personaje = this.personajeService.create();
		result = this.createEditModelAndView(personaje);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int personajeId) {
		ModelAndView result;
		ComicCharacter personaje;

		personaje = this.personajeService.findOne(personajeId);
		Assert.notNull(personaje);
		result = this.createEditModelAndView(personaje);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ComicCharacter personaje, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(personaje);
		else
			try {
				this.personajeService.save(personaje);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(personaje, "personaje.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final ComicCharacter personaje, final BindingResult binding) {
		ModelAndView result;

		try {
			this.personajeService.delete(personaje);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(personaje, "personaje.commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final ComicCharacter personaje) {
		ModelAndView result;

		result = this.createEditModelAndView(personaje, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final ComicCharacter personaje, final String message) {
		ModelAndView result;
		Publisher editorial = null;
		final Collection<Publisher> editoriales = this.editorialService.findAll();

		if (personaje.getPublishingCompany() != null)
			editorial = personaje.getPublishingCompany();

		result = new ModelAndView("personaje/edit");
		result.addObject("personaje", personaje);
		result.addObject("editorial", editorial);
		result.addObject("editoriales", editoriales);
		result.addObject("message", message);

		return result;
	}

}
