
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
import org.springframework.web.servlet.ModelAndView;

import security.UserAccount;
import security.UserAccountService;
import services.ComicService;
import services.UserService;
import domain.Comic;
import domain.Comment;
import domain.User;
import domain.UserComic;
import domain.Volume;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService			userService;
	@Autowired
	private UserAccountService	userAccountService;
	@Autowired
	private ComicService		comicService;


	public UserController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<User> users;

		users = this.userService.findAll();
		result = new ModelAndView("user/list");
		result.addObject("requestURI", "user/list.do");
		result.addObject("users", users);

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
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
			final User userSaved = this.userService.save(user);
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
}
