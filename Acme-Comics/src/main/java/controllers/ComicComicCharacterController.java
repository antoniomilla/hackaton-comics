
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import domain.Comic;
import domain.ComicComicCharacter;
import exceptions.ResourceNotFoundException;
import exceptions.ResourceNotUniqueException;
import services.ComicCharacterService;
import services.ComicComicCharacterService;
import services.ComicService;
import utilities.ApplicationConfig;
import utilities.ControllerUtils;
import utilities.PolicyCheckUtils;

@Controller
@RequestMapping("/comic_comic_characters")
public class ComicComicCharacterController extends AbstractController {
	@Autowired private ComicComicCharacterService comicComicCharacterService;
	@Autowired private ComicCharacterService comicCharacterService;
	@Autowired private ComicService comicService;

	@RequestMapping("/new")
	public ModelAndView new_(@RequestParam("comic") int comicId)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());
		Comic comic = comicService.getById(comicId);
		return createEditModelAndView("comic_comic_characters/new", "comic_comic_characters/create.do", null, null, new ComicComicCharacter(comic));
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView create(@Valid @ModelAttribute("comicComicCharacter") ComicComicCharacter comicComicCharacter, BindingResult binding, RedirectAttributes redir)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());

		String globalErrorMessage = null;

		try {
			if (!binding.hasErrors()) {
				comicComicCharacterService.create(comicComicCharacter);
				redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
				return ControllerUtils.redirectToReturnAction();
			}
		} catch (ResourceNotUniqueException oops) {
			globalErrorMessage = "comic_comic_characters.error.notUnique";
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			globalErrorMessage = "misc.commit.error";
		}

		return createEditModelAndView("comic_comic_characters/new", "comic_comic_characters/create.do", globalErrorMessage, binding, comicComicCharacter);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam("id") int comicComicCharacterId)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());
		ComicComicCharacter comicComicCharacter = comicComicCharacterService.getByIdForEdit(comicComicCharacterId);
		return createEditModelAndView("comic_comic_characters/edit", "comic_comic_characters/update.do", null, null, comicComicCharacter);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(@ModelAttribute("comicComicCharacter") ComicComicCharacter comicComicCharacter, BindingResult binding, RedirectAttributes redir)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());

		String globalErrorMessage = null;

		try {
			comicComicCharacter = comicComicCharacterService.bindForUpdate(comicComicCharacter, binding);
			if (!binding.hasErrors()) {
				comicComicCharacterService.update(comicComicCharacter);
				redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
				return ControllerUtils.redirectToReturnAction();
			}
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			globalErrorMessage = "misc.commit.error";
		}

		return createEditModelAndView("comic_comic_characters/edit", "comic_comic_characters/update.do", globalErrorMessage, binding, comicComicCharacter);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ModelAndView delete(@RequestParam("id") int id, RedirectAttributes redir)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());

		try {
			comicComicCharacterService.delete(id);
			redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
			return ControllerUtils.redirectToReturnAction();
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			redir.addFlashAttribute("globalErrorMessage", "misc.commit.error");
		}

		return ControllerUtils.redirectToReturnAction();
	}

	private ModelAndView createEditModelAndView(String viewName, String formAction, String globalErrorMessage, BindingResult binding, ComicComicCharacter comicComicCharacter)
	{
		ModelAndView result = ControllerUtils.createViewWithBinding(
				viewName, "comicComicCharacter", comicComicCharacter,
				binding, globalErrorMessage
		);

		result.addObject("formAction", formAction);
		result.addObject("comicCharacters", comicCharacterService.findAll());

		return result;
	}
}
