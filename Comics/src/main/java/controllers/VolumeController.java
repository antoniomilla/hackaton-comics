
package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AuthorService;
import services.VolumeService;
import domain.Author;
import domain.Comment;
import domain.Volume;

@Controller
@RequestMapping("/volume")
public class VolumeController {

	@Autowired
	private VolumeService	volumeService;
	@Autowired
	private AuthorService	authorService;


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

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int volumeId) {
		ModelAndView result;
		final Volume volume = this.volumeService.findOne(volumeId);
		final Collection<Comment> comments = volume.getComments();
		result = new ModelAndView("volume/display");
		result.addObject("volume", volume);
		result.addObject("comments", comments);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Volume volume;

		volume = this.volumeService.create();
		result = this.createEditModelAndView(volume);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int volumeId) {
		ModelAndView result;
		Volume volume;

		volume = this.volumeService.findOne(volumeId);
		Assert.notNull(volume);
		result = this.createEditModelAndView(volume);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Volume volume, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(volume);
		else
			try {
				this.volumeService.save(volume);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(volume, "volume.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Volume volume, final BindingResult binding) {
		ModelAndView result;

		try {
			this.volumeService.delete(volume);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(volume, "volume.commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Volume volume) {
		ModelAndView result;

		result = this.createEditModelAndView(volume, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Volume volume, final String message) {
		ModelAndView result;
		Author author = null;
		final Collection<Author> authors = this.authorService.findAll();

		if (volume.getAuthor() != null)
			author = volume.getAuthor();

		result = new ModelAndView("volume/edit");
		result.addObject("volume", volume);
		result.addObject("autbor", author);
		result.addObject("authors", authors);
		result.addObject("message", message);

		return result;
	}
}
