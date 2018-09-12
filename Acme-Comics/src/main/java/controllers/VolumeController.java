
package controllers;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

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
import services.AuthorService;
import services.ComicService;
import services.CommentService;
import services.UserComicService;
import services.VolumeService;
import domain.Comic;
import domain.Comment;
import domain.Volume;
import utilities.ApplicationConfig;
import utilities.ControllerUtils;
import utilities.PolicyCheckUtils;

@Controller
@RequestMapping("/volumes")
public class VolumeController extends AbstractController {
	@Autowired private VolumeService	volumeService;
	@Autowired private CommentService commentService;
	@Autowired private ComicService	comicService;
	@Autowired private UserComicService userComicService;
	@Autowired private AuthorService authorService;

	@RequestMapping("/show")
	public ModelAndView show(@RequestParam("id") int id, Model model) {
		Volume volume = volumeService.getById(id);
		List<Comment> comments = commentService.findForListInVolume(volume);

		UserComic userComic = null;

		Actor principal = findPrincipal();

		// Gets the flashed comment from the comments/create.do handler,
		// if an error occured there. The binding result will already be in the model.
		// This is a fairly unorthodox way of doing this, but it lets us get validation feedback
		// from a completely unrelated handler without coupling it to this page, etc.
		Comment comment = (Comment) model.asMap().get("comment");
		if (comment == null) comment = new Comment();

		if (principal instanceof User) {
			userComic = userComicService.getByUserAndComic((User) principal, volume.getComic());

			comment.setVolume(volume);
			comment.setUser((User) principal);
			comment.setCreationTime(new Date());
		}

		ModelAndView result = new ModelAndView("volumes/show");
		result.addObject("volume", volume);
		result.addObject("comments", comments);
		result.addObject("userComic", userComic);
		result.addObject("comment", comment);

		return result;
	}

	@RequestMapping("/read")
	public ModelAndView read(@RequestParam("id") int id, RedirectAttributes redir)
	{
		Volume volume = volumeService.getById(id);
		try {
			userComicService.readVolume(volume);
			redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
			return ControllerUtils.redirectToReturnAction();
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			redir.addFlashAttribute("globalErrorMessage", "misc.commit.error");
			return ControllerUtils.redirectToReturnAction();
		}

	}

	@RequestMapping("/unread")
	public ModelAndView unread(@RequestParam("id") int id, RedirectAttributes redir)
	{
		Volume volume = volumeService.getById(id);
		try {
			userComicService.unreadVolume(volume);
			redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
			return ControllerUtils.redirectToReturnAction();
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			redir.addFlashAttribute("globalErrorMessage", "misc.commit.error");
			return ControllerUtils.redirectToReturnAction();
		}
	}

	@RequestMapping("/new")
	public ModelAndView new_(@RequestParam("comic") int comicId)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());
		Comic comic = comicService.getById(comicId);
		return createEditModelAndView("volumes/new", "volumes/create.do", null, null, new Volume(comic));
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView create(@Valid @ModelAttribute("volume") Volume volume, BindingResult binding, RedirectAttributes redir)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());

		String globalErrorMessage = null;

		try {
			if (!binding.hasErrors()) {
				volume = volumeService.create(volume);
				redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
				redir.addAttribute("id", volume.getId());
				return ControllerUtils.redirect("/volumes/show.do");
			}
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			globalErrorMessage = "misc.commit.error";
		}

		return createEditModelAndView("volumes/new", "volumes/create.do", globalErrorMessage, binding, volume);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam("id") int volumeId)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());
		Volume volume = volumeService.getByIdForEdit(volumeId);
		return createEditModelAndView("volumes/edit", "volumes/update.do", null, null, volume);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(@ModelAttribute("volume") Volume volume, BindingResult binding, RedirectAttributes redir)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());

		String globalErrorMessage = null;

		try {
			volume = volumeService.bindForUpdate(volume, binding);
			if (!binding.hasErrors()) {
				volumeService.update(volume);
				redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
				return ControllerUtils.redirectToReturnAction();
			}
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			globalErrorMessage = "misc.commit.error";
		}

		return createEditModelAndView("volumes/edit", "volumes/update.do", globalErrorMessage, binding, volume);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ModelAndView delete(@RequestParam("id") int id, RedirectAttributes redir)
	{
		PolicyCheckUtils.checkCanEditContent(findPrincipal());

		try {
			volumeService.delete(id);
			redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
			return ControllerUtils.redirectToReturnAction();
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			redir.addFlashAttribute("globalErrorMessage", "misc.commit.error");
		}

		return ControllerUtils.redirectToReturnAction();
	}

	private ModelAndView createEditModelAndView(String viewName, String formAction, String globalErrorMessage, BindingResult binding, Volume volume)
	{
		ModelAndView result = ControllerUtils.createViewWithBinding(
				viewName, "volume", volume,
				binding, globalErrorMessage
		);

		result.addObject("formAction", formAction);
		result.addObject("authors", authorService.findAll());

		return result;
	}
}
