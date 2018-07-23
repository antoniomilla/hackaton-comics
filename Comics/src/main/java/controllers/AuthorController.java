
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
import domain.Author;
import domain.Comic;
import domain.Comment;
import domain.Volume;

@Controller
@RequestMapping("/author")
public class AuthorController {

	@Autowired
	private AuthorService	authorService;


	public AuthorController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Author> authors;

		authors = this.authorService.findAll();
		result = new ModelAndView("author/list");
		result.addObject("requestURI", "author/list.do");
		result.addObject("authors", authors);

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int authorId) {
		ModelAndView result;
		final Author author = this.authorService.findOne(authorId);
		final Collection<Comic> comics = author.getComics();
		final Collection<Comment> comments = author.getComments();
		final Collection<Volume> volumes = author.getVolumes();
		result = new ModelAndView("author/display");
		result.addObject("author", author);
		result.addObject("comics", comics);
		result.addObject("comments", comments);
		result.addObject("volumes", volumes);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Author author;

		author = this.authorService.create();
		result = this.createEditModelAndView(author);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int authorId) {
		ModelAndView result;
		Author author;

		author = this.authorService.findOne(authorId);
		Assert.notNull(author);
		result = this.createEditModelAndView(author);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Author author, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(author);
		else
			try {
				this.authorService.save(author);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(author, "author.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Author author, final BindingResult binding) {
		ModelAndView result;

		try {
			this.authorService.delete(author);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(author, "author.commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Author author) {
		ModelAndView result;

		result = this.createEditModelAndView(author, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Author author, final String message) {
		ModelAndView result;

		result = new ModelAndView("author/edit");
		result.addObject("author", author);
		result.addObject("message", message);

		return result;
	}

}
