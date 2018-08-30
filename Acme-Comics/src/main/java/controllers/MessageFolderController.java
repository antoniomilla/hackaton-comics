
package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domain.ComicCharacter;
import services.MessageFolderService;
import services.UserService;
import domain.MessageFolder;
import domain.User;
import utilities.ApplicationConfig;
import utilities.CheckUtils;
import utilities.ControllerUtils;
import utilities.PolicyCheckUtils;

@Controller
@RequestMapping("/message_folders")
public class MessageFolderController extends AbstractController {
	@Autowired private MessageFolderService	messageFolderService;

	@RequestMapping("/new")
	public ModelAndView new_()
	{
		CheckUtils.checkAuthenticated();
		return createEditModelAndView("message_folders/new", "message_folders/create.do", null, null, new MessageFolder(getPrincipal()));
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView create(@Valid @ModelAttribute("messageFolder") MessageFolder messageFolder, BindingResult binding, RedirectAttributes redir)
	{
		CheckUtils.checkAuthenticated();

		String globalErrorMessage = null;

		try {
			if (!binding.hasErrors()) {
				messageFolderService.create(messageFolder);
				redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
				return ControllerUtils.redirectToReturnAction();
			}
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			globalErrorMessage = "misc.commit.error";
		}

		return createEditModelAndView("message_folders/new", "message_folders/create.do", globalErrorMessage, binding, messageFolder);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam("id") int messageFolderId)
	{
		CheckUtils.checkAuthenticated();
		MessageFolder messageFolder = messageFolderService.getByIdForEdit(messageFolderId);
		return createEditModelAndView("message_folders/edit", "message_folders/update.do", null, null, messageFolder);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(@ModelAttribute("messageFolder") MessageFolder messageFolder, BindingResult binding, RedirectAttributes redir)
	{
		CheckUtils.checkAuthenticated();

		String globalErrorMessage = null;

		try {
			messageFolder = messageFolderService.bindForUpdate(messageFolder, binding);
			if (!binding.hasErrors()) {
				messageFolderService.update(messageFolder);
				redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
				return ControllerUtils.redirectToReturnAction();
			}
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			globalErrorMessage = "misc.commit.error";
		}

		return createEditModelAndView("message_folders/edit", "message_folders/update.do", globalErrorMessage, binding, messageFolder);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ModelAndView delete(@RequestParam("id") int id, RedirectAttributes redir)
	{
		CheckUtils.checkAuthenticated();

		try {
			messageFolderService.delete(id);
			redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
			return ControllerUtils.redirectToReturnAction();
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			redir.addFlashAttribute("globalErrorMessage", "misc.commit.error");
		}

		return ControllerUtils.redirectToReturnAction();
	}

	private ModelAndView createEditModelAndView(String viewName, String formAction, String globalErrorMessage, BindingResult binding, MessageFolder messageFolder)
	{
		ModelAndView result = ControllerUtils.createViewWithBinding(
				viewName, "messageFolder", messageFolder,
				binding, globalErrorMessage
		);

		result.addObject("formAction", formAction);

		return result;
	}
}
