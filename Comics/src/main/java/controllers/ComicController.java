
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

import services.AuthorService;
import services.ComicService;
import services.PublisherService;
import services.UserService;
import domain.Author;
import domain.Comic;
import domain.ComicCharacter;
import domain.ComicComicCharacter;
import domain.Comment;
import domain.Publisher;
import domain.User;
import domain.UserComic;
import domain.Volume;

@Controller
@RequestMapping("/comic")
public class ComicController {

	@Autowired
	private ComicService		comicService;
	@Autowired
	private AuthorService		authorService;
	@Autowired
	private PublisherService	publisherService;
	@Autowired
	private UserService			userService;


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
		final Collection<ComicCharacter> comicCharacters = this.comicComicCharacters(comic);
		final Collection<Comment> comments = comic.getComments();
		final Collection<Volume> volumes = comic.getVolumes();

		final User user = this.userService.findByPrincipal();
		final UserComic userComic = this.userComic(user, comic);
		//final ComicComicCharacter comicComicCharacter = this.getCCC(comicCharacters, comic);

		result = new ModelAndView("comic/display");
		result.addObject("comic", comic);
		result.addObject("comicCharacters", comicCharacters);
		result.addObject("comments", comments);
		result.addObject("volumes", volumes);
		result.addObject("userComic", userComic);
		//result.addObject("comicComicCharacter", comicComicCharacter);

		return result;
	}

	/*
	 * private ComicComicCharacter getCCC(final Collection<ComicCharacter> comicCharacters, final Comic c) {
	 * ComicComicCharacter res = null;
	 * for (final ComicCharacter cc : comicCharacters)
	 * for (final ComicComicCharacter ccc1 : cc.getComicComicCharacter())
	 * for (final ComicComicCharacter ccc2 : c.getComicComicCharacter())
	 * if (ccc1.getId() == ccc2.getId()) {
	 * res = ccc1;
	 * break;
	 * }
	 * return res;
	 * }
	 */

	private UserComic userComic(final User u, final Comic c) {
		UserComic res = null;

		for (final UserComic uc : c.getUserComics())
			for (final UserComic uc2 : u.getUserComics())
				if (uc.getId() == uc2.getId()) {
					res = uc2;
					break;
				}

		return res;
	}

	private Collection<ComicCharacter> comicComicCharacters(final Comic c) {
		final Collection<ComicCharacter> res = new ArrayList<ComicCharacter>();
		for (final ComicComicCharacter ccc : c.getComicComicCharacter()) {
			final ComicCharacter cc = ccc.getComicCharacter();
			res.add(cc);
		}
		return res;
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
