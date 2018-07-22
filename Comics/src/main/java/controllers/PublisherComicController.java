
package controllers;

import java.util.Collection;

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
		final Collection<Comic> comics = this.comicService.getComicsByPublisher("DC Comics");
		result = new ModelAndView("comic/list");
		result.addObject("requestURI", "publisher/comic/listDC.do");
		result.addObject("comics", comics);

		return result;
	}

	@RequestMapping(value = "/listMarvel", method = RequestMethod.GET)
	public ModelAndView listMarvel() {
		ModelAndView result;
		final Collection<Comic> comics = this.comicService.getComicsByPublisher("Marvel Comics");

		result = new ModelAndView("comic/list");
		result.addObject("requestURI", "publisher/comic/listMarvel.do");
		result.addObject("comics", comics);

		return result;
	}

}
