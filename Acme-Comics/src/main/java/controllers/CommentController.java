
package controllers;

import javax.naming.Binding;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import security.Authority;
import services.CommentService;
import domain.Comment;
import utilities.ApplicationConfig;
import utilities.CheckUtils;
import utilities.ControllerUtils;

@Controller
@RequestMapping("/comments")
public class CommentController extends AbstractController {
	@Autowired private CommentService	commentService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView create(@Valid Comment comment, BindingResult binding, RedirectAttributes redir)
	{
		CheckUtils.checkPrincipalAuthority(Authority.USER);

		String globalErrorMessage = null;

		if (!binding.hasErrors()) {
			try {
				commentService.create(comment);
				redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
				return ControllerUtils.redirectToReturnAction();
			} catch (Throwable oops) {
				if (ApplicationConfig.DEBUG) oops.printStackTrace();
				globalErrorMessage = "misc.commit.error";
			}
		}

		redir.addFlashAttribute("globalErrorMessage", globalErrorMessage);
		redir.addFlashAttribute("org.springframework.validation.BindingResult.comment", binding);
		redir.addFlashAttribute("comment", comment);


		return ControllerUtils.redirectToReturnAction();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ModelAndView delete(@RequestParam("id") int id, RedirectAttributes redir) {
		CheckUtils.checkPrincipalAuthority(Authority.ADMINISTRATOR);
		commentService.delete(id);
		redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
		return ControllerUtils.redirectToReturnAction();
	}

}
