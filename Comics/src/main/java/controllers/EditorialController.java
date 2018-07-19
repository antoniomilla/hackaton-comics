
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

import services.PublisherService;
import domain.ComicCharacter;
import domain.Comic;
import domain.Publisher;

@Controller
@RequestMapping("/editorial")
public class EditorialController {

	@Autowired
	private PublisherService	editorialService;


	public EditorialController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Publisher> editoriales;

		editoriales = this.editorialService.findAll();
		result = new ModelAndView("editorial/list");
		result.addObject("requestURI", "editorial/list.do");
		result.addObject("editoriales", editoriales);

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int editorialId) {
		ModelAndView result;
		final Publisher editorial = this.editorialService.findOne(editorialId);
		final Collection<ComicCharacter> personajes = editorial.getCharacters();
		final Collection<Comic> comics = editorial.getComics();
		result = new ModelAndView("editorial/display");
		result.addObject("editorial", editorial);
		result.addObject("personajes", personajes);
		result.addObject("comics", comics);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Publisher editorial;

		editorial = this.editorialService.create();
		result = this.createEditModelAndView(editorial);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int editorialId) {
		ModelAndView result;
		Publisher editorial;

		editorial = this.editorialService.findOne(editorialId);
		Assert.notNull(editorial);
		result = this.createEditModelAndView(editorial);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Publisher editorial, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(editorial);
		else
			try {
				this.editorialService.save(editorial);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(editorial, "editorial.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Publisher editorial, final BindingResult binding) {
		ModelAndView result;

		try {
			this.editorialService.delete(editorial);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(editorial, "editorial.commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Publisher editorial) {
		ModelAndView result;

		result = this.createEditModelAndView(editorial, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Publisher editorial, final String message) {
		ModelAndView result;

		result = new ModelAndView("editorial/edit");
		result.addObject("editorial", editorial);
		result.addObject("message", message);

		return result;
	}

}
