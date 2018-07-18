
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
import domain.Author;
import domain.Comic;

@Controller
@RequestMapping("/autor")
public class AutorController {

	//comentario antopnio
	@Autowired
	private AutorService	autorService;


	//COMENTARIO JESUS

	public AutorController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Author> autores;

		autores = this.autorService.findAll();
		result = new ModelAndView("autor/list");
		result.addObject("requestURI", "autor/list.do");
		result.addObject("autores", autores);

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int autorId) {
		ModelAndView result;
		final Author autor = this.autorService.findOne(autorId);
		final Collection<Comic> comics = autor.getComics();
		result = new ModelAndView("autor/display");
		result.addObject("autor", autor);
		result.addObject("comics", comics);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Author autor;

		autor = this.autorService.create();
		result = this.createEditModelAndView(autor);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int autorId) {
		ModelAndView result;
		Author autor;

		autor = this.autorService.findOne(autorId);
		Assert.notNull(autor);
		result = this.createEditModelAndView(autor);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Author autor, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(autor);
		else
			try {
				this.autorService.save(autor);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(autor, "autor.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Author autor, final BindingResult binding) {
		ModelAndView result;

		try {
			this.autorService.delete(autor);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(autor, "autor.commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Author autor) {
		ModelAndView result;

		result = this.createEditModelAndView(autor, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Author autor, final String message) {
		ModelAndView result;

		result = new ModelAndView("autor/edit");
		result.addObject("autor", autor);
		result.addObject("message", message);

		return result;
	}

}
