
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AutorService;
import domain.Autor;

@Controller
@RequestMapping("/autor")
public class AutorController {

	@Autowired
	private AutorService	autorService;


	public AutorController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Autor> autores;

		autores = this.autorService.findAll();
		result = new ModelAndView("autor/list");
		result.addObject("requestURI", "autor/list.do");
		result.addObject("autores", autores);

		return result;
	}

}
