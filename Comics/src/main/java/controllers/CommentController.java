
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import domain.Comment;

@Controller
@RequestMapping("/comment")
public class CommentController {

	@Autowired
	private CommentService	commentService;


	public CommentController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int commentId) {
		ModelAndView result;
		final Comment comment = this.commentService.findOne(commentId);

		result = new ModelAndView("comment/display");
		result.addObject("comment", comment);

		return result;
	}

	@RequestMapping(value = "/comic/create", method = RequestMethod.GET)
	public ModelAndView createComic(@RequestParam final Integer comicId) {
		ModelAndView result;
		final Comment comment = this.commentService.createComic(comicId);
		result = this.createEditModelAndView(comment);

		return result;

	}

	@RequestMapping(value = "/publisher/create", method = RequestMethod.GET)
	public ModelAndView createPublisher(@RequestParam final Integer publisherId) {
		ModelAndView result;
		final Comment comment = this.commentService.createPublisher(publisherId);
		result = this.createEditModelAndView(comment);

		return result;

	}

	@RequestMapping(value = "/author/create", method = RequestMethod.GET)
	public ModelAndView createAuthor(@RequestParam final Integer authorId) {
		ModelAndView result;
		final Comment comment = this.commentService.createAuthor(authorId);
		result = this.createEditModelAndView(comment);

		return result;

	}

	@RequestMapping(value = "/comicCharacter/create", method = RequestMethod.GET)
	public ModelAndView createComicCharacter(@RequestParam final Integer comicCharacterId) {
		ModelAndView result;
		final Comment comment = this.commentService.createComicCharacter(comicCharacterId);
		result = this.createEditModelAndView(comment);

		return result;

	}

	@RequestMapping(value = "/volume/create", method = RequestMethod.GET)
	public ModelAndView createVolume(@RequestParam final Integer volumeId) {
		ModelAndView result;
		final Comment comment = this.commentService.createVolume(volumeId);
		result = this.createEditModelAndView(comment);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int commentId) {
		ModelAndView result;
		Comment comment;

		comment = this.commentService.findOne(commentId);
		Assert.notNull(comment);
		result = this.createEditModelAndView(comment);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Comment comment, final BindingResult binding) {
		ModelAndView result = null;

		if (binding.hasErrors()) {
			System.out.print(binding.getAllErrors());
			result = this.createEditModelAndView(comment);
		} else
			try {
				this.commentService.save(comment);
				if (comment.getComic() != null)
					result = new ModelAndView("redirect:/comic/display.do?comicId=" + comment.getComic().getId());
				if (comment.getVolume() != null)
					result = new ModelAndView("redirect:/volume/display.do?volumeId=" + comment.getVolume().getId());
				if (comment.getComicCharacter() != null)
					result = new ModelAndView("redirect:/comicCharacter/display.do?comicCharacterId=" + comment.getComicCharacter().getId());
				if (comment.getAuthor() != null)
					result = new ModelAndView("redirect:/author/display.do?authorId=" + comment.getAuthor().getId());
				if (comment.getPublisher() != null)
					result = new ModelAndView("redirect:/publisher/display.do?publisherId=" + comment.getPublisher().getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(comment, "comment.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Comment comment, final BindingResult binding) {
		ModelAndView result = null;

		try {

			if (comment.getComic() != null) {
				final int comicId = comment.getComic().getId();
				this.commentService.delete(comment);
				result = new ModelAndView("redirect:/comic/display.do?comicId=" + comicId);
			}
			if (comment.getVolume() != null) {
				final int volumeId = comment.getVolume().getId();
				this.commentService.delete(comment);
				result = new ModelAndView("redirect:/volume/display.do?volumeId=" + volumeId);
			}
			if (comment.getComicCharacter() != null) {
				final int comicCharacterId = comment.getComicCharacter().getId();
				this.commentService.delete(comment);
				result = new ModelAndView("redirect:/comicCharacter/display.do?comicCharacterId=" + comicCharacterId);
			}
			if (comment.getAuthor() != null) {
				final int authorId = comment.getAuthor().getId();
				this.commentService.delete(comment);
				result = new ModelAndView("redirect:/author/display.do?authorId=" + authorId);
			}
			if (comment.getPublisher() != null) {
				final int publisherId = comment.getPublisher().getId();
				this.commentService.delete(comment);
				result = new ModelAndView("redirect:/publisher/display.do?publisherId=" + publisherId);
			}
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(comment, "comment.commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Comment comment) {
		ModelAndView result;

		result = this.createEditModelAndView(comment, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Comment comment, final String message) {
		ModelAndView result;

		result = new ModelAndView("comment/edit");
		result.addObject("comment", comment);
		result.addObject("message", message);

		return result;
	}

}
