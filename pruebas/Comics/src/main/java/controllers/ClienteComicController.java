
package controllers;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ClienteService;
import services.ComicService;
import domain.Cliente;
import domain.Comic;

@Controller
@RequestMapping("/cliente/comic")
public class ClienteComicController {

	@Autowired
	private ClienteService	clienteService;
	@Autowired
	private ComicService	comicService;


	public ClienteComicController() {
		super();
	}

	@RequestMapping(value = "/listLeidos", method = RequestMethod.GET)
	public ModelAndView listLeidos() {
		ModelAndView result;
		final Cliente c = this.clienteService.findByPrincipal();
		final Collection<Comic> comics = c.getComicsLeidos();
		result = new ModelAndView("comic/list");
		result.addObject("requestURI", "cliente/comicLeidos/list.do");
		result.addObject("comics", comics);

		return result;
	}

	@RequestMapping(value = "/listNoLeidos", method = RequestMethod.GET)
	public ModelAndView listNoLeidos() {
		ModelAndView result;
		final Cliente c = this.clienteService.findByPrincipal();
		final Collection<Comic> todos = this.comicService.findAll();
		final Collection<Comic> comics = this.noLeidos(todos, c);
		result = new ModelAndView("comic/list");
		result.addObject("requestURI", "cliente/comicNoLeidos/list.do");
		result.addObject("comics", comics);

		return result;
	}

	private Collection<Comic> noLeidos(final Collection<Comic> lista, final Cliente cliente) {
		final Collection<Comic> res = new HashSet<Comic>();
		final Collection<Comic> leidos = cliente.getComicsLeidos();

		for (final Comic c : lista)
			if (!leidos.contains(c))
				res.add(c);
		return res;
	}

}
