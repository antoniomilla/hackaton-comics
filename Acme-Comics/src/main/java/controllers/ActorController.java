
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import forms.SearchForm;
import services.ActorService;

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
}
