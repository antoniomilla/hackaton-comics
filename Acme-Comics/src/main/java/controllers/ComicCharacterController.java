
package controllers;

import java.util.ArrayList;
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
import domain.Author;
import domain.User;
import domain.UserComic;
import domain.Volume;
import services.AuthorService;
import services.ComicCharacterService;
import services.ComicComicCharacterService;
import services.ComicService;
import services.CommentService;
import services.PublisherService;
import domain.Comic;
import domain.ComicCharacter;
import domain.ComicComicCharacter;
import domain.Comment;
import domain.Publisher;
import services.VolumeService;
import utilities.ApplicationConfig;
import utilities.ControllerUtils;
import utilities.PolicyCheckUtils;

@Controller
@RequestMapping("/comic_characters")
public class ComicCharacterController extends AbstractController {
	@Autowired private ComicCharacterService comicCharacterService;
	@Autowired private CommentService commentService;
	@Autowired private ComicComicCharacterService comicComicCharacterService;
	@Autowired private PublisherService publisherService;

	@RequestMapping("/list")
	public ModelAndView list()
	{
		List<ComicCharacter> comicCharacters = comicCharacterService.findAll();
		ModelAndView result = new ModelAndView("comic_characters/list");
		result.addObject("comicCharacters", comicCharacters);

		return result;
	}

	@RequestMapping("/show")
	public ModelAndView show(@RequestParam("id") int id, Model model)
	{
		Actor principal = findPrincipal();

		ComicCharacter comicCharacter = comicCharacterService.getById(id);

		List<Pair<ComicComicCharacter, UserComic>> comicPairs = comicComicCharacterService.findComicComicCharactersAndUserComicsForComicCharacterAndPrincipal(comicCharacter);
		List<Comment> comments = commentService.findForListInComicCharacter(comicCharacter);

		// Gets the flashed comment from the comments/create.do handler,
		// if an error occured there. The binding result will already be in the model.
		// This is a fairly unorthodox way of doing this, but it lets us get validation feedback
		// from a completely unrelated handler without coupling it to this page, etc.
		Comment comment = (Comment) model.asMap().get("comment");
		if (comment == null) comment = new Comment();


		if (principal instanceof User) {
			comment.setComicCharacter(comicCharacter);
			comment.setUser((User) principal);
			comment.setCreationTime(new Date());
		}

		ModelAndView result = new ModelAndView("comic_characters/show");
		result.addObject("comicCharacter", comicCharacter);
		result.addObject("comicPairs", comicPairs);
		result.addObject("comment", comment);
		result.addObject("comments", comments);

		return result;
	}

	@RequestMapping("/new")
	public ModelAndView new_()
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());
		return createEditModelAndView("comic_characters/new", "comic_characters/create.do", null, null, new ComicCharacter());
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView create(@Valid @ModelAttribute("comicCharacter") ComicCharacter comicCharacter, BindingResult binding, RedirectAttributes redir)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());

		String globalErrorMessage = null;

		try {
			if (!binding.hasErrors()) {
				comicCharacterService.create(comicCharacter);
				redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
				redir.addAttribute("id", comicCharacter.getId());
				return ControllerUtils.redirect("/comic_characters/show.do");
			}
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			globalErrorMessage = "misc.commit.error";
		}

		return createEditModelAndView("comic_characters/new", "comic_characters/create.do", globalErrorMessage, binding, comicCharacter);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam("id") int comicCharacterId)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());
		ComicCharacter comicCharacter = comicCharacterService.getByIdForEdit(comicCharacterId);
		return createEditModelAndView("comic_characters/edit", "comic_characters/update.do", null, null, comicCharacter);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(@ModelAttribute("comicCharacter") ComicCharacter comicCharacter, BindingResult binding, RedirectAttributes redir)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());

		String globalErrorMessage = null;

		try {
			comicCharacter = comicCharacterService.bindForUpdate(comicCharacter, binding);
			if (!binding.hasErrors()) {
				comicCharacterService.update(comicCharacter);
				redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
				return ControllerUtils.redirectToReturnAction();
			}
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			globalErrorMessage = "misc.commit.error";
		}

		return createEditModelAndView("comic_characters/edit", "comic_characters/update.do", globalErrorMessage, binding, comicCharacter);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ModelAndView delete(@RequestParam("id") int id, RedirectAttributes redir)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());

		try {
			comicCharacterService.delete(id);
			redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
			return ControllerUtils.redirectToReturnAction();
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			redir.addFlashAttribute("globalErrorMessage", "misc.commit.error");
		}

		return ControllerUtils.redirectToReturnAction();
	}

	private ModelAndView createEditModelAndView(String viewName, String formAction, String globalErrorMessage, BindingResult binding, ComicCharacter comicCharacter)
	{
		ModelAndView result = ControllerUtils.createViewWithBinding(
				viewName, "comicCharacter", comicCharacter,
				binding, globalErrorMessage
		);

		result.addObject("formAction", formAction);
		result.addObject("publishers", publisherService.findAll());

		return result;
	}
}
