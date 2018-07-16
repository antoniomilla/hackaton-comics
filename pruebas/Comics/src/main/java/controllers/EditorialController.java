
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.EditorialService;
import domain.Editorial;

@Controller
@RequestMapping("/editorial")
public class EditorialController {

	@Autowired
	private EditorialService	editorialService;


	public EditorialController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Editorial> editoriales;

		editoriales = this.editorialService.findAll();
		result = new ModelAndView("editorial/list");
		result.addObject("requestURI", "editorial/list.do");
		result.addObject("editoriales", editoriales);

		return result;
	}

}
