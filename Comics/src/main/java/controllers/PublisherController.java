
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
import domain.Comic;
import domain.ComicCharacter;
import domain.Comment;
import domain.Publisher;

@Controller
@RequestMapping("/publisher")
public class PublisherController {

	@Autowired
	private PublisherService	publisherService;


	public PublisherController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Publisher> publishers;

		publishers = this.publisherService.findAll();
		result = new ModelAndView("publisher/list");
		result.addObject("requestURI", "publisher/list.do");
		result.addObject("publishers", publishers);

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int publisherId) {
		ModelAndView result;
		final Publisher publisher = this.publisherService.findOne(publisherId);

		final Collection<Comic> comics = publisher.getComics();
		final Collection<ComicCharacter> comicCharacters = publisher.getComicCharacters();
		final Collection<Comment> comments = publisher.getComments();
		result = new ModelAndView("publisher/display");
		result.addObject("publisher", publisher);
		result.addObject("comics", comics);
		result.addObject("comicCharacters", comicCharacters);
		result.addObject("comments", comments);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Publisher publisher;

		publisher = this.publisherService.create();
		result = this.createEditModelAndView(publisher);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int publisherId) {
		ModelAndView result;
		Publisher publisher;

		publisher = this.publisherService.findOne(publisherId);
		Assert.notNull(publisher);
		result = this.createEditModelAndView(publisher);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Publisher publisher, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(publisher);
		else
			try {
				this.publisherService.save(publisher);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(publisher, "publisher.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Publisher publisher, final BindingResult binding) {
		ModelAndView result;

		try {
			this.publisherService.delete(publisher);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(publisher, "publisher.commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Publisher publisher) {
		ModelAndView result;

		result = this.createEditModelAndView(publisher, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Publisher publisher, final String message) {
		ModelAndView result;

		result = new ModelAndView("publisher/edit");
		result.addObject("editorial", publisher);
		result.addObject("message", message);

		return result;
	}

}
