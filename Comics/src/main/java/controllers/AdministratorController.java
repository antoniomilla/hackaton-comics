
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import domain.Admin;

@Controller
@RequestMapping("/administrator")
public class AdministratorController {

	@Autowired
	private AdministratorService	administratorService;


	public AdministratorController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Admin> administrators;

		administrators = this.administratorService.findAll();
		result = new ModelAndView("administrator/list");
		result.addObject("requestURI", "administrator/list.do");
		result.addObject("administrators", administrators);

		return result;
	}

	@RequestMapping(value = "/trust", method = RequestMethod.GET)
	public ModelAndView trust(@RequestParam final int userId) {
		ModelAndView result;

		try {
			this.administratorService.trust(userId);
			result = new ModelAndView("redirect:/user/list.do");
			result.addObject("message", "administrator.commit.ok");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/user/list.do");
			result.addObject("message", "administrator.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/untrust", method = RequestMethod.GET)
	public ModelAndView untrust(@RequestParam final int userId) {
		ModelAndView result;

		try {
			this.administratorService.untrust(userId);
			;
			result = new ModelAndView("redirect:/user/list.do");
			result.addObject("message", "administrator.commit.ok");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/user/list.do");
			result.addObject("message", "administrator.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/block", method = RequestMethod.GET)
	public ModelAndView block(@RequestParam final int userId) {
		ModelAndView result;

		try {
			this.administratorService.block(userId);
			result = new ModelAndView("redirect:/user/list.do");
			result.addObject("message", "administrator.commit.ok");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/user/list.do");
			result.addObject("message", "administrator.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/unblock", method = RequestMethod.GET)
	public ModelAndView unblock(@RequestParam final int userId) {
		ModelAndView result;

		try {
			this.administratorService.unblock(userId);
			;
			result = new ModelAndView("redirect:/user/list.do");
			result.addObject("message", "administrator.commit.ok");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/user/list.do");
			result.addObject("message", "administrator.commit.error");
		}

		return result;
	}

}
