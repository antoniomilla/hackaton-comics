
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.VolumeService;
import domain.User;
import domain.Volume;

@Controller
@RequestMapping("/volume")
public class VolumeController {

	@Autowired
	private VolumeService	volumeService;


	public VolumeController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Volume> volumes;

		volumes = this.volumeService.findAll();
		result = new ModelAndView("volume/list");
		result.addObject("requestURI", "volume/list.do");
		result.addObject("volumes", volumes);

		return result;
	}

	protected ModelAndView createModelAndView(final User client, final String message) {
		ModelAndView result;

		result = new ModelAndView("user/create");
		result.addObject("user", client);
		result.addObject("message", message);

		return result;
	}
}
