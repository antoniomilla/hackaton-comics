
package controllers;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ComicService;
import domain.Comic;

@Controller
@RequestMapping("/editorial/comic")
public class EditorialComicController {

	@Autowired
	private ComicService	comicService;


	public EditorialComicController() {
		super();
	}

	@RequestMapping(value = "/listDC", method = RequestMethod.GET)
	public ModelAndView listDC() {
		ModelAndView result;
		final Collection<Comic> todos = this.comicService.findAll();
		final Collection<Comic> comics = this.dc(todos);
		result = new ModelAndView("comic/list");
		result.addObject("requestURI", "editorial/comic/listDC.do");
		result.addObject("comics", comics);

		return result;
	}

	private Collection<Comic> dc(final Collection<Comic> lista) {
		final Collection<Comic> res = new HashSet<Comic>();
		for (final Comic c : lista)
			if (c.getPublishingCompany().getName().equals("DC"))
				res.add(c);
		return res;
	}

	@RequestMapping(value = "/listMarvel", method = RequestMethod.GET)
	public ModelAndView listMarvel() {
		ModelAndView result;
		final Collection<Comic> todos = this.comicService.findAll();
		final Collection<Comic> comics = this.marvel(todos);
		result = new ModelAndView("comic/list");
		result.addObject("requestURI", "editorial/comic/listMarvel.do");
		result.addObject("comics", comics);

		return result;
	}

	private Collection<Comic> marvel(final Collection<Comic> lista) {
		final Collection<Comic> res = new HashSet<Comic>();
		for (final Comic c : lista)
			if (c.getPublishingCompany().getName().equals("Marvel"))
				res.add(c);
		return res;
	}

}
