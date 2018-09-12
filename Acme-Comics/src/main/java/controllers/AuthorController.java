
package controllers;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domain.Actor;
import domain.ComicCharacter;
import domain.Publisher;
import domain.User;
import domain.UserComic;
import services.AuthorService;
import domain.Author;
import domain.Comic;
import domain.Comment;
import domain.Volume;
import services.ComicService;
import services.CommentService;
import services.PublisherService;
import services.VolumeService;
import utilities.ApplicationConfig;
import utilities.ControllerUtils;
import utilities.PolicyCheckUtils;

@Controller
@RequestMapping("/authors")
public class AuthorController extends AbstractController {
	@Autowired private AuthorService authorService;
	@Autowired private CommentService commentService;
	@Autowired private VolumeService volumeService;
	@Autowired private ComicService comicService;

	@RequestMapping("/list")
	public ModelAndView list()
	{
		List<Author> authors = authorService.findAll();
		ModelAndView result = new ModelAndView("authors/list");
		result.addObject("authors", authors);

		return result;
	}

	@RequestMapping("/show")
	public ModelAndView show(@RequestParam("id") int id, Model model)
	{
		Actor principal = findPrincipal();

		Author author = authorService.getById(id);

		List<Pair<Comic, UserComic>> comicPairs = comicService.findComicsAndUserComicsForAuthorAndPrincipal(author);
		List<Pair<Volume, UserComic>> volumePairs = volumeService.findVolumesAndUserComicsForAuthorAndPrincipal(author);
		List<Comment> comments = commentService.findForListInAuthor(author);

		// Gets the flashed comment from the comments/create.do handler,
		// if an error occured there. The binding result will already be in the model.
		// This is a fairly unorthodox way of doing this, but it lets us get validation feedback
		// from a completely unrelated handler without coupling it to this page, etc.
		Comment comment = (Comment) model.asMap().get("comment");
		if (comment == null) comment = new Comment();


		if (principal instanceof User) {
			comment.setAuthor(author);
			comment.setUser((User) principal);
			comment.setCreationTime(new Date());
		}

		ModelAndView result = new ModelAndView("authors/show");
		result.addObject("author", author);
		result.addObject("comicPairs", comicPairs);
		result.addObject("volumePairs", volumePairs);
		result.addObject("comment", comment);
		result.addObject("comments", comments);

		return result;
	}

	@RequestMapping("/new")
	public ModelAndView new_()
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());
		return createEditModelAndView("authors/new", "authors/create.do", null, null, new Author());
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView create(@Valid @ModelAttribute("author") Author author, BindingResult binding, RedirectAttributes redir)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());

		String globalErrorMessage = null;

		try {
			if (!binding.hasErrors()) {
				author = authorService.create(author);
				redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
				redir.addAttribute("id", author.getId());
				return ControllerUtils.redirect("/authors/show.do");
			}
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			globalErrorMessage = "misc.commit.error";
		}

		return createEditModelAndView("authors/new", "authors/create.do", globalErrorMessage, binding, author);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam("id") int authorId)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());
		Author author = authorService.getByIdForEdit(authorId);
		return createEditModelAndView("authors/edit", "authors/update.do", null, null, author);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(@ModelAttribute("author") Author author, BindingResult binding, RedirectAttributes redir)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());

		String globalErrorMessage = null;

		try {
			author = authorService.bindForUpdate(author, binding);
			if (!binding.hasErrors()) {
				authorService.update(author);
				redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
				return ControllerUtils.redirectToReturnAction();
			}
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			globalErrorMessage = "misc.commit.error";
		}

		return createEditModelAndView("authors/edit", "authors/update.do", globalErrorMessage, binding, author);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ModelAndView delete(@RequestParam("id") int id, RedirectAttributes redir)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());

		try {
			authorService.delete(id);
			redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
			return ControllerUtils.redirectToReturnAction();
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			redir.addFlashAttribute("globalErrorMessage", "misc.commit.error");
		}

		return ControllerUtils.redirectToReturnAction();
	}

	private ModelAndView createEditModelAndView(String viewName, String formAction, String globalErrorMessage, BindingResult binding, Author author)
	{
		ModelAndView result = ControllerUtils.createViewWithBinding(
				viewName, "author", author,
				binding, globalErrorMessage
		);

		result.addObject("formAction", formAction);

		return result;
	}
}
