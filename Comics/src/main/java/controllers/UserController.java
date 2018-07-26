
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import services.ActorService;
import services.ComicService;
import services.MessageFolderService;
import services.UserService;
import domain.Actor;
import domain.Comic;
import domain.Comment;
import domain.MessageFolder;
import domain.MessageFolderType;
import domain.User;
import domain.UserComic;
import domain.Volume;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService				userService;
	@Autowired
	private UserAccountService		userAccountService;
	@Autowired
	private MessageFolderService	messageFolderService;
	@Autowired
	private ComicService			comicService;
	@Autowired
	private ActorService			actorService;


	public UserController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result = new ModelAndView("user/list");
		final Collection<User> users = this.userService.findAll();
		if (LoginService.isAuthenticated()) {
			final Actor actor = this.actorService.findByPrincipal();
			users.remove(actor);
			if (actor instanceof User) {
				final User e = (User) actor;
				final Collection<User> friends = e.getFriends();
				result.addObject("friends", friends);
			}
		}

		result.addObject("requestURI", "user/list.do");
		result.addObject("users", users);

		return result;
	}

	@RequestMapping(value = "/displayUser", method = RequestMethod.GET)
	public ModelAndView displayUser(final int userId) {
		ModelAndView result;

		final User user = this.userService.findOne(userId);
		final Collection<Comic> all = this.comicService.findAll();
		final Collection<Comic> comics = this.comics(user, all);
		final Collection<Volume> volumes = user.getUserVolumes();
		final Collection<Comment> comments = user.getUserComments();

		result = new ModelAndView("user/display");
		result.addObject("user", user);
		result.addObject("comics", comics);
		result.addObject("volumes", volumes);
		result.addObject("comments", comments);

		return result;
	}

	@RequestMapping(value = "/displayProfile", method = RequestMethod.GET)
	public ModelAndView displayProfile() {
		ModelAndView result;

		final User user = this.userService.findByPrincipal();
		final Collection<Comic> all = this.comicService.findAll();
		final Collection<Comic> comics = this.comics(user, all);
		final Collection<Volume> volumes = user.getUserVolumes();
		final Collection<Comment> comments = user.getUserComments();

		result = new ModelAndView("user/display");
		result.addObject("user", user);
		result.addObject("comics", comics);
		result.addObject("volumes", volumes);
		result.addObject("comments", comments);

		return result;
	}

	private Collection<Comic> comics(final User u, final Collection<Comic> all) {
		final Collection<Comic> res = new ArrayList<Comic>();

		for (final Comic c : all)
			for (final UserComic uc1 : u.getUserComics())
				for (final UserComic uc2 : c.getUserComics())
					if (uc1.getId() == uc2.getId())
						res.add(uc1.getComic());
		return res;

	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createCustomer() {
		final User user = this.userService.create();
		final UserAccount ua = this.userAccountService.create();
		user.setUserAccount(ua);
		final ModelAndView result = this.createModelAndView(user, null);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView saveCustomer(@Valid final User user, final BindingResult binding) {
		ModelAndView res;
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		if (binding.hasErrors()) {
			System.out.print(binding.getAllErrors());
			res = this.createModelAndView(user, null);
		} else {

			user.getUserAccount().setPassword(encoder.encodePassword(user.getUserAccount().getPassword(), null));

			System.out.println(user.getMessageFolders());
			final User userSaved = this.userService.save(user);
			userSaved.setMessageFolders(this.defaultFolders(userSaved));
			System.out.println(userSaved.getMessageFolders());
			res = new ModelAndView("security/login");
			res.addObject("credentials", userSaved.getUserAccount());
		}
		return res;
	}
	protected ModelAndView createModelAndView(final User client, final String message) {
		ModelAndView result;

		result = new ModelAndView("user/create");
		result.addObject("user", client);
		result.addObject("message", message);

		return result;
	}
	private Collection<MessageFolder> defaultFolders(final User u) {
		final MessageFolder inbox = this.messageFolderService.create(u);
		inbox.setType(MessageFolderType.SYSTEM_INBOX);
		inbox.setName("inbox");
		inbox.setNameForDisplay("Inbox");
		final MessageFolder inboxSaved = this.messageFolderService.save(inbox);
		inboxSaved.setOwner(u);

		final MessageFolder trash = this.messageFolderService.create(u);
		trash.setType(MessageFolderType.SYSTEM_TRASH);
		trash.setName("Trash");
		trash.setNameForDisplay("Trash");
		final MessageFolder trashSaved = this.messageFolderService.save(trash);
		trashSaved.setOwner(u);

		final MessageFolder sent = this.messageFolderService.create(u);
		sent.setType(MessageFolderType.SYSTEM_SENT);
		sent.setName("Sent");
		sent.setNameForDisplay("Sent");
		final MessageFolder sentSaved = this.messageFolderService.save(sent);
		sentSaved.setOwner(u);

		Collection<MessageFolder> folders;
		folders = new ArrayList<MessageFolder>();
		folders.add(inboxSaved);
		folders.add(trash);
		folders.add(sent);
		return folders;

	}

	@RequestMapping(value = "/friend", method = RequestMethod.GET)
	public ModelAndView friend(@RequestParam final int userId) {
		ModelAndView result;

		try {
			this.userService.friend(userId);
			result = new ModelAndView("redirect:/user/list.do");
			result.addObject("message", "user.commit.ok");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/user/list.do");
			result.addObject("message", "user.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/unfriend", method = RequestMethod.GET)
	public ModelAndView unfriend(@RequestParam final int userId) {
		ModelAndView result;

		try {
			this.userService.unfriend(userId);
			;
			result = new ModelAndView("redirect:/user/list.do");
			result.addObject("message", "user.commit.ok");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/user/list.do");
			result.addObject("message", "user.commit.error");
		}

		return result;
	}

}
