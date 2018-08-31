
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domain.Administrator;
import security.Authority;
import services.AdministratorService;
import utilities.ApplicationConfig;
import utilities.CheckUtils;
import utilities.ControllerUtils;

@Controller
@RequestMapping("/administrators")
public class AdministratorController extends AbstractController {
    @Autowired private AdministratorService administratorService;

    @RequestMapping(value = "/edit")
    public ModelAndView edit()
    {
        CheckUtils.checkPrincipalAuthority(Authority.ADMINISTRATOR);

        ModelAndView result = createEditModelAndView("actors/edit", "administrators/update.do", null, null, (Administrator) getPrincipal());
        return result;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView update(@ModelAttribute("administrator") Administrator administrator, BindingResult binding, RedirectAttributes redir)
    {
        CheckUtils.checkPrincipalAuthority(Authority.ADMINISTRATOR);

        String globalErrorMessage = null;

        administrator = administratorService.bindForUpdate(administrator, binding);
        if (!binding.hasErrors()) {
            try {
                administratorService.update(administrator);
                redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
                return ControllerUtils.redirectToReturnAction();
            } catch (Throwable oops) {
                if (ApplicationConfig.DEBUG) oops.printStackTrace();
                globalErrorMessage = "misc.commit.error";
            }
        }

        return createEditModelAndView("actors/edit", "administrators/update.do", globalErrorMessage, binding, administrator);
    }

    public ModelAndView createEditModelAndView(String viewName, String formAction, String globalErrorMessage, BindingResult binding, Administrator actor)
    {
        ModelAndView result = ControllerUtils.createViewWithBinding(
                viewName, "actor", actor,
                binding, globalErrorMessage
        );

        result.addObject("formAction", formAction);

        return result;
    }
}
