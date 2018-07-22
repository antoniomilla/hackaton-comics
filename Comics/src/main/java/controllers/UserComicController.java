
package controllers;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ComicService;
import services.UserService;
import domain.Comic;
import domain.User;

@Controller
@RequestMapping("/user/comic")
public class UserComicController {

	@Autowired
	private UserService		userService;
	@Autowired
	private ComicService	comicService;


	public UserComicController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Comic> comics = this.comicService.findAll();
		final User user = this.userService.findByPrincipal();
		//final Collection<Comic> read = user.getComicsRead();
		result = new ModelAndView("comic/list");
		result.addObject("requestURI", "comic/list.do");
		result.addObject("comics", comics);
		//result.addObject("read", read);

		return result;
	}

	@RequestMapping(value = "/listRead", method = RequestMethod.GET)
	public ModelAndView listRead() {
		ModelAndView result;
		final User user = this.userService.findByPrincipal();
		//final Collection<Comic> comics = user.getComicsRead();
		result = new ModelAndView("comic/list");
		result.addObject("requestURI", "user/readComics/list.do");
		//result.addObject("comics", comics);

		return result;
	}

	@RequestMapping(value = "/listUnread", method = RequestMethod.GET)
	public ModelAndView listUnread() {
		ModelAndView result;
		final User user = this.userService.findByPrincipal();
		final Collection<Comic> all = this.comicService.findAll();
		final Collection<Comic> comics = this.unread(all, user);
		result = new ModelAndView("comic/list");
		result.addObject("requestURI", "user/unreadComics/list.do");
		result.addObject("comics", comics);

		return result;
	}

	private Collection<Comic> unread(final Collection<Comic> list, final User user) {
		final Collection<Comic> res = new HashSet<Comic>();
		/*
		 * final Collection<Comic> read = user.getComicsRead();
		 * 
		 * for (final Comic c : list)
		 * if (!read.contains(c))
		 * res.add(c);
		 */
		return res;

	}

	@RequestMapping(value = "/read", method = RequestMethod.GET)
	public ModelAndView read(@RequestParam final int comicId) {
		ModelAndView result;

		try {
			this.userService.setRead(comicId);
			result = this.list();
			result.addObject("message", "comic.commit.ok");
		} catch (final Throwable oops) {
			result = this.list();
			result.addObject("message", "comic.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/unread", method = RequestMethod.GET)
	public ModelAndView unread(@RequestParam final int comicId) {
		ModelAndView result;

		try {
			this.userService.setUnread(comicId);
			;
			result = this.list();
			result.addObject("message", "comic.commit.ok");
		} catch (final Throwable oops) {
			result = this.list();
			result.addObject("message", "comic.commit.error");
		}

		return result;
	}

}
