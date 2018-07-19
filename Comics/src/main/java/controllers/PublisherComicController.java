
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
@RequestMapping("/publisher/comic")
public class PublisherComicController {

	@Autowired
	private ComicService	comicService;


	public PublisherComicController() {
		super();
	}

	@RequestMapping(value = "/listDC", method = RequestMethod.GET)
	public ModelAndView listDC() {
		ModelAndView result;
		final Collection<Comic> all = this.comicService.findAll();
		final Collection<Comic> comics = this.dc(all);
		result = new ModelAndView("comic/list");
		result.addObject("requestURI", "publisher/comic/listDC.do");
		result.addObject("comics", comics);

		return result;
	}

	private Collection<Comic> dc(final Collection<Comic> list) {
		final Collection<Comic> res = new HashSet<Comic>();
		for (final Comic c : list)
			if (c.getPublisher().getName().equals("DC"))
				res.add(c);
		return res;
	}

	@RequestMapping(value = "/listMarvel", method = RequestMethod.GET)
	public ModelAndView listMarvel() {
		ModelAndView result;
		final Collection<Comic> all = this.comicService.findAll();
		final Collection<Comic> comics = this.marvel(all);
		result = new ModelAndView("comic/list");
		result.addObject("requestURI", "publisher/comic/listMarvel.do");
		result.addObject("comics", comics);

		return result;
	}

	private Collection<Comic> marvel(final Collection<Comic> list) {
		final Collection<Comic> res = new HashSet<Comic>();
		for (final Comic c : list)
			if (c.getPublisher().getName().equals("Marvel"))
				res.add(c);
		return res;
	}

}
