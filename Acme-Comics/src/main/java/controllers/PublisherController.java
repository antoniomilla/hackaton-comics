
package controllers;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domain.Actor;
import domain.User;
import domain.UserComic;
import services.ComicService;
import services.CommentService;
import services.PublisherService;
import domain.Comic;
import domain.ComicCharacter;
import domain.Comment;
import domain.Publisher;
import utilities.ApplicationConfig;
import utilities.ControllerUtils;
import utilities.PolicyCheckUtils;

@Controller
@RequestMapping("/publishers")
public class PublisherController extends AbstractController {
	@Autowired private PublisherService	publisherService;
	@Autowired private CommentService commentService;
	@Autowired private ComicService comicService;

	@RequestMapping("/list")
	public ModelAndView list()
	{
		List<Publisher> publishers = publisherService.findAll();
		ModelAndView result = new ModelAndView("publishers/list");
		result.addObject("publishers", publishers);

		return result;
	}

	@RequestMapping("/show")
	public ModelAndView show(@RequestParam("id") int id, Model model)
	{
		Publisher publisher = publisherService.getById(id);

		List<Pair<Comic, UserComic>> comicPairs = comicService.findComicsAndUserComicsForPublisherAndPrincipal(publisher);
		List<ComicCharacter> comicCharacters = publisher.getComicCharacters();
		List<Comment> comments = commentService.findForListInPublisher(publisher);

		// Gets the flashed comment from the comments/create.do handler,
		// if an error occured there. The binding result will already be in the model.
		// This is a fairly unorthodox way of doing this, but it lets us get validation feedback
		// from a completely unrelated handler without coupling it to this page, etc.
		Comment comment = (Comment) model.asMap().get("comment");
		if (comment == null) comment = new Comment();

		Actor principal = findPrincipal();
		if (principal instanceof User) {
			comment.setPublisher(publisher);
			comment.setUser((User) principal);
			comment.setCreationTime(new Date());
		}

		ModelAndView result = new ModelAndView("publishers/show");
		result.addObject("publisher", publisher);
		result.addObject("comicPairs", comicPairs);
		result.addObject("comicCharacters", comicCharacters);
		result.addObject("comment", comment);
		result.addObject("comments", comments);

		return result;
	}

	@RequestMapping("/new")
	public ModelAndView new_()
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());
		return createEditModelAndView("publishers/new", "publishers/create.do", null, null, new Publisher());
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView create(@Valid @ModelAttribute("publisher") Publisher publisher, BindingResult binding, RedirectAttributes redir)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());

		String globalErrorMessage = null;

		try {
			if (!binding.hasErrors()) {
				publisherService.create(publisher);
				redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
				return ControllerUtils.redirectToReturnAction();
			}
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			globalErrorMessage = "misc.commit.error";
		}

		return createEditModelAndView("publishers/new", "publishers/create.do", globalErrorMessage, binding, publisher);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam("id") int publisherId)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());
		Publisher publisher = publisherService.getByIdForEdit(publisherId);
		return createEditModelAndView("publishers/edit", "publishers/update.do", null, null, publisher);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(@ModelAttribute("publisher") Publisher publisher, BindingResult binding, RedirectAttributes redir)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());

		String globalErrorMessage = null;

		try {
			publisher = publisherService.bindForUpdate(publisher, binding);
			if (!binding.hasErrors()) {
				publisherService.update(publisher);
				redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
				return ControllerUtils.redirectToReturnAction();
			}
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			globalErrorMessage = "misc.commit.error";
		}

		return createEditModelAndView("publishers/edit", "publishers/update.do", globalErrorMessage, binding, publisher);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ModelAndView delete(@RequestParam("id") int id, RedirectAttributes redir)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());

		try {
			publisherService.delete(id);
			redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
			return ControllerUtils.redirectToReturnAction();
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			redir.addFlashAttribute("globalErrorMessage", "misc.commit.error");
		}

		return ControllerUtils.redirectToReturnAction();
	}

	private ModelAndView createEditModelAndView(String viewName, String formAction, String globalErrorMessage, BindingResult binding, Publisher publisher)
	{
		ModelAndView result = ControllerUtils.createViewWithBinding(
				viewName, "publisher", publisher,
				binding, globalErrorMessage
		);

		result.addObject("formAction", formAction);

		return result;
	}
}
