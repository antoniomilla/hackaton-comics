
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

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Comment comment;

		comment = this.commentService.create();
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
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(comment);
		else
			try {
				this.commentService.save(comment);
				result = new ModelAndView("redirect:comic/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(comment, "comment.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Comment comment, final BindingResult binding) {
		ModelAndView result;

		try {
			this.commentService.delete(comment);
			result = new ModelAndView("redirect:comic/list.do");
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
