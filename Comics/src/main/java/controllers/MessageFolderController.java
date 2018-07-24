
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

}
