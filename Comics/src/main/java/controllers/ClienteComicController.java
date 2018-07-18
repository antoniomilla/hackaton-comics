
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
@RequestMapping("/cliente/comic")
public class ClienteComicController {

	@Autowired
	private UserService	clienteService;
	@Autowired
	private ComicService	comicService;


	public ClienteComicController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Comic> comics = this.comicService.findAll();
		final User cliente = this.clienteService.findByPrincipal();
		final Collection<Comic> leidos = cliente.getComicsRead();
		result = new ModelAndView("comic/list");
		result.addObject("requestURI", "comic/list.do");
		result.addObject("comics", comics);
		result.addObject("leidos", leidos);

		return result;
	}

	@RequestMapping(value = "/listLeidos", method = RequestMethod.GET)
	public ModelAndView listLeidos() {
		ModelAndView result;
		final User c = this.clienteService.findByPrincipal();
		final Collection<Comic> comics = c.getComicsRead();
		result = new ModelAndView("comic/list");
		result.addObject("requestURI", "cliente/comicLeidos/list.do");
		result.addObject("comics", comics);

		return result;
	}

	@RequestMapping(value = "/listNoLeidos", method = RequestMethod.GET)
	public ModelAndView listNoLeidos() {
		ModelAndView result;
		final User c = this.clienteService.findByPrincipal();
		final Collection<Comic> todos = this.comicService.findAll();
		final Collection<Comic> comics = this.noLeidos(todos, c);
		result = new ModelAndView("comic/list");
		result.addObject("requestURI", "cliente/comicNoLeidos/list.do");
		result.addObject("comics", comics);

		return result;
	}

	private Collection<Comic> noLeidos(final Collection<Comic> lista, final User cliente) {
		final Collection<Comic> res = new HashSet<Comic>();
		final Collection<Comic> leidos = cliente.getComicsRead();

		for (final Comic c : lista)
			if (!leidos.contains(c))
				res.add(c);
		return res;
	}

	@RequestMapping(value = "/leido", method = RequestMethod.GET)
	public ModelAndView leido(@RequestParam final int comicId) {
		ModelAndView result;

		try {
			this.clienteService.marcarLeido(comicId);
			result = this.list();
			result.addObject("message", "comic.commit.ok");
		} catch (final Throwable oops) {
			result = this.list();
			result.addObject("message", "comic.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/noLeido", method = RequestMethod.GET)
	public ModelAndView noLeido(@RequestParam final int comicId) {
		ModelAndView result;

		try {
			this.clienteService.marcarNoLeido(comicId);
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
