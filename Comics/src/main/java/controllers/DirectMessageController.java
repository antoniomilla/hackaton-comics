
package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.DirectMessageService;
import services.MessageFolderService;
import domain.Actor;
import domain.DirectMessage;
import domain.MessageFolder;
import domain.User;

@Controller
@RequestMapping("/directMessage")
public class DirectMessageController {

	@Autowired
	private DirectMessageService	directMessageService;
	@Autowired
	private MessageFolderService	messageFolderService;
	@Autowired
	private ActorService			actorService;


	public DirectMessageController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int messageFolderId) {
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

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		DirectMessage directMessage;

		directMessage = this.directMessageService.create();
		result = this.createEditModelAndView(directMessage);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int directMessageId) {
		ModelAndView result;
		DirectMessage directMessage;

		directMessage = this.directMessageService.findOne(directMessageId);
		Assert.notNull(directMessage);
		result = this.createEditModelAndView(directMessage);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final DirectMessage directMessage, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			System.out.println(binding.getAllErrors());
			result = this.createEditModelAndView(directMessage);
		} else
			try {

				this.directMessageService.save(directMessage);
				final List<MessageFolder> folders = new ArrayList<MessageFolder>(directMessage.getMessageFolder());
				result = new ModelAndView("redirect:list.do?messageFolderId=" + folders.get(1).getId());
			} catch (final Throwable oops) {

				result = this.createEditModelAndView(directMessage, "directMessage.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final DirectMessage directMessage, final BindingResult binding) {
		ModelAndView result;

		try {
			this.directMessageService.delete(directMessage);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(directMessage, "directMessage.commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final DirectMessage directMessage) {
		ModelAndView result;

		result = this.createEditModelAndView(directMessage, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final DirectMessage directMessage, final String message) {
		ModelAndView result;
		final Collection<Actor> recipients = this.actorService.findAll();
		recipients.remove(this.actorService.findByPrincipal());

		result = new ModelAndView("directMessage/edit");
		result.addObject("directMessage", directMessage);
		result.addObject("recipients", recipients);
		result.addObject("message", message);

		return result;
	}

}
