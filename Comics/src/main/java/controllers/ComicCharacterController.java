
package controllers;

import java.util.ArrayList;
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

import services.ComicCharacterService;
import services.PublisherService;
import domain.Comic;
import domain.ComicCharacter;
import domain.ComicComicCharacter;
import domain.Comment;
import domain.Publisher;

@Controller
@RequestMapping("/comicCharacter")
public class ComicCharacterController {

	@Autowired
	private ComicCharacterService	comicCharacterService;
	@Autowired
	private PublisherService		publisherService;


	public ComicCharacterController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<ComicCharacter> comicCharacters;

		comicCharacters = this.comicCharacterService.findAll();
		result = new ModelAndView("comicCharacter/list");
		result.addObject("requestURI", "comicCharacter/list.do");
		result.addObject("comicCharacters", comicCharacters);

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int comicCharacterId) {
		ModelAndView result;
		final ComicCharacter comicCharacter = this.comicCharacterService.findOne(comicCharacterId);
		final Collection<Comic> comics = this.comicCharacterComics(comicCharacter);
		final Collection<Comment> comments = comicCharacter.getComments();
		result = new ModelAndView("comicCharacter/display");
		result.addObject("comicCharacter", comicCharacter);
		result.addObject("comics", comics);
		result.addObject("comments", comments);

		return result;
	}

	private Collection<Comic> comicCharacterComics(final ComicCharacter cc) {
		final Collection<Comic> res = new ArrayList<Comic>();
		for (final ComicComicCharacter ccc : cc.getComicComicCharacter()) {
			final Comic c = ccc.getComic();
			res.add(c);
		}
		return res;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		ComicCharacter comicCharacter;

		comicCharacter = this.comicCharacterService.create();
		result = this.createEditModelAndView(comicCharacter);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int comicCharacterId) {
		ModelAndView result;
		ComicCharacter comicCharacter;

		comicCharacter = this.comicCharacterService.findOne(comicCharacterId);
		Assert.notNull(comicCharacter);
		result = this.createEditModelAndView(comicCharacter);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ComicCharacter comicCharacter, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(comicCharacter);
		else
			try {
				this.comicCharacterService.save(comicCharacter);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(comicCharacter, "comicCharacter.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final ComicCharacter comicCharacter, final BindingResult binding) {
		ModelAndView result;

		try {
			this.comicCharacterService.delete(comicCharacter);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(comicCharacter, "comicCharacter.commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final ComicCharacter comicCharacter) {
		ModelAndView result;

		result = this.createEditModelAndView(comicCharacter, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final ComicCharacter comicCharacter, final String message) {
		ModelAndView result;
		Publisher publisher = null;
		final Collection<Publisher> publishers = this.publisherService.findAll();

		if (comicCharacter.getPublisher() != null)
			publisher = comicCharacter.getPublisher();

		result = new ModelAndView("comicCharacter/edit");
		result.addObject("comicCharacter", comicCharacter);
		result.addObject("publisher", publisher);
		result.addObject("publishers", publishers);
		result.addObject("message", message);

		return result;
	}

}
