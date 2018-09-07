
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import exceptions.OldPasswordDoesntMatchException;
import forms.EditOwnPasswordForm;
import forms.SearchForm;
import services.ActorService;
import utilities.ApplicationConfig;
import utilities.CheckUtils;
import utilities.ControllerUtils;

@Controller
@RequestMapping("/actors")
public class ActorController extends AbstractController {
    @Autowired private ActorService actorService;

    @RequestMapping(value = "/list")
    public ModelAndView list(@Valid SearchForm searchForm)
    {
        ModelAndView result = new ModelAndView("actors/list");
        result.addObject("actors", actorService.search(searchForm.getTerms()));
        result.addObject("searchForm", searchForm);

        return result;
    }

    @RequestMapping("/edit_own_password")
    public ModelAndView editOwnPassword() {
        CheckUtils.checkAuthenticated();

        return createEditOwnPasswordModelAndView("actors/edit_own_password", "actors/update_own_password.do", null, null, new EditOwnPasswordForm());
    }

    public ModelAndView createEditOwnPasswordModelAndView(String viewName, String formAction, String globalErrorMessage, BindingResult binding, EditOwnPasswordForm form) {
        ModelAndView result = ControllerUtils.createViewWithBinding(viewName, "form", form, binding, globalErrorMessage);
        result.addObject("formAction", formAction);

        return result;
    }

    @RequestMapping(value = "/update_own_password", method = RequestMethod.POST)
    public ModelAndView updateOwnPassword(@Valid @ModelAttribute("form") EditOwnPasswordForm form, BindingResult binding, RedirectAttributes redir) {
        CheckUtils.checkAuthenticated();

        String globalErrorMessage = null;

        if (!binding.hasErrors())
            try {
                actorService.updateOwnPassword(form.getOldPassword(), form.getNewPassword());
                redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
                return ControllerUtils.redirectToReturnAction();
            } catch (OldPasswordDoesntMatchException oops) {
                binding.rejectValue("oldPassword", "actors.error.oldPasswordDoesntMatch");
            } catch (Throwable oops) {
                if (ApplicationConfig.DEBUG) oops.printStackTrace();
                globalErrorMessage = "misc.commit.error";
            }

        return createEditOwnPasswordModelAndView("actors/edit_own_password", "actors/update_own_password.do", globalErrorMessage, binding, form);
    }
}
