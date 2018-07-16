
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

import services.AutorService;
import services.ComicService;
import services.EditorialService;
import domain.Autor;
import domain.Comic;
import domain.Editorial;

@Controller
@RequestMapping("/comic")
public class ComicController {

	@Autowired
	private ComicService		comicService;
	@Autowired
	private AutorService		autorService;
	@Autowired
	private EditorialService	editorialService;


	// Constructors -----------------------------------------------------------

	public ComicController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Comic> comics = this.comicService.findAll();
		result = new ModelAndView("comic/list");
		result.addObject("requestURI", "comic/list.do");
		result.addObject("comics", comics);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Comic comic;

		comic = this.comicService.create();
		result = this.createEditModelAndView(comic);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int comicId) {
		ModelAndView result;
		Comic comic;

		comic = this.comicService.findOne(comicId);
		Assert.notNull(comic);
		result = this.createEditModelAndView(comic);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Comic comic, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(comic);
		else
			try {
				this.comicService.save(comic);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(comic, "comic.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Comic comic, final BindingResult binding) {
		ModelAndView result;

		try {
			this.comicService.delete(comic);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(comic, "comic.commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Comic comic) {
		ModelAndView result;

		result = this.createEditModelAndView(comic, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Comic comic, final String message) {
		ModelAndView result;
		Autor autor = null;
		Editorial editorial = null;
		final Collection<Autor> autores = this.autorService.findAll();
		final Collection<Editorial> editoriales = this.editorialService.findAll();

		if (comic.getAutor() != null)
			autor = comic.getAutor();
		if (comic.getEditorial() != null)
			editorial = comic.getEditorial();

		result = new ModelAndView("comic/edit");
		result.addObject("comic", comic);
		result.addObject("autor", autor);
		result.addObject("editorial", editorial);
		result.addObject("autores", autores);
		result.addObject("editoriales", editoriales);
		result.addObject("message", message);

		return result;
	}

}
