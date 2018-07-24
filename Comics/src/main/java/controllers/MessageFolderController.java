
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

import services.MessageFolderService;
import services.UserService;
import domain.MessageFolder;
import domain.User;

@Controller
@RequestMapping("/messageFolder")
public class MessageFolderController {

	@Autowired
	private MessageFolderService	messageFolderService;
	@Autowired
	private UserService				userService;


	public MessageFolderController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final User user = this.userService.findByPrincipal();
		final Collection<MessageFolder> messageFolders = user.getMessageFolders();
		result = new ModelAndView("messageFolder/list");
		result.addObject("requestURI", "messageFolder/list.do");
		result.addObject("messageFolders", messageFolders);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		MessageFolder messageFolder;

		messageFolder = this.messageFolderService.create();
		result = this.createEditModelAndView(messageFolder);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int messageFolderId) {
		ModelAndView result;
		MessageFolder messageFolder;

		messageFolder = this.messageFolderService.findOne(messageFolderId);
		Assert.notNull(messageFolder);
		result = this.createEditModelAndView(messageFolder);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final MessageFolder messageFolder, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			System.out.print(binding.getAllErrors());
			result = this.createEditModelAndView(messageFolder);
		} else
			try {
				this.messageFolderService.save(messageFolder);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(messageFolder, "messageFolder.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final MessageFolder messageFolder, final BindingResult binding) {
		ModelAndView result;

		try {
			this.messageFolderService.delete(messageFolder);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(messageFolder, "messageFolder.commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final MessageFolder messageFolder) {
		ModelAndView result;

		result = this.createEditModelAndView(messageFolder, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final MessageFolder messageFolder, final String message) {
		ModelAndView result;

		result = new ModelAndView("messageFolder/edit");
		result.addObject("messageFolder", messageFolder);
		result.addObject("message", message);

		return result;
	}

}
