
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.DirectMessageService;
import services.MessageFolderService;
import services.UserService;
import domain.DirectMessage;
import domain.MessageFolder;
import domain.User;

@Controller
@RequestMapping("/directMessage")
public class DirectMessageController {

	@Autowired
	private DirectMessageService	directMessageService;
	@Autowired
	private UserService				userService;
	@Autowired
	private MessageFolderService	messageFolderService;


	public DirectMessageController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final int messageFolderId) {
		ModelAndView result;
		final MessageFolder messageFolder = this.messageFolderService.findOne(messageFolderId);
		final String name = messageFolder.getNameForDisplay();
		final Collection<DirectMessage> directMessages = messageFolder.getMessages();
		result = new ModelAndView("directMessage/list");
		result.addObject("requestURI", "directMessage/list.do");
		result.addObject("directMessages", directMessages);
		result.addObject("name", name);

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int directMessageId) {
		ModelAndView result;

		final DirectMessage directMessage = this.directMessageService.findOne(directMessageId);
		final User sender = (User) directMessage.getSender();
		final User recipient = (User) directMessage.getRecipient();

		result = new ModelAndView("directMessage/display");
		result.addObject("directMessage", directMessage);
		result.addObject("sender", sender);
		result.addObject("recipient", recipient);

		return result;
	}

}
