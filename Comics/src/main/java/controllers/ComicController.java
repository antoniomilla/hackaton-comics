
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

import services.AuthorService;
import services.ComicService;
import services.PublisherService;
import domain.Author;
import domain.Comic;
import domain.ComicCharacter;
import domain.Publisher;

@Controller
@RequestMapping("/comic")
public class ComicController {

	@Autowired
	private ComicService		comicService;
	@Autowired
	private AuthorService		authorService;
	@Autowired
	private PublisherService	publisherService;


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

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int comicId) {
		ModelAndView result;
		final Comic comic = this.comicService.findOne(comicId);
		final Collection<ComicCharacter> characters = comic.getCharacters();
		result = new ModelAndView("comic/display");
		result.addObject("comic", comic);
		result.addObject("characters", characters);

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
		Author author = null;
		Publisher publisher = null;
		final Collection<Author> authors = this.authorService.findAll();
		final Collection<Publisher> publishers = this.publisherService.findAll();

		if (comic.getAuthor() != null)
			author = comic.getAuthor();
		if (comic.getPublisher() != null)
			publisher = comic.getPublisher();

		result = new ModelAndView("comic/edit");
		result.addObject("comic", comic);
		result.addObject("autbor", author);
		result.addObject("publisher", publisher);
		result.addObject("authors", authors);
		result.addObject("publishers", publishers);
		result.addObject("message", message);

		return result;
	}

}
