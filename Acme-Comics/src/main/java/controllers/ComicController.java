
package controllers;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import domain.Actor;
import domain.Comic;
import domain.Comment;
import domain.User;
import domain.UserComic;
import domain.UserComicStatus;
import domain.Volume;
import exceptions.VolumesNotReadException;
import forms.SearchForm;
import security.Authority;
import services.AuthorService;
import services.ComicService;
import services.CommentService;
import services.PublisherService;
import services.UserComicService;
import services.VolumeService;
import utilities.ApplicationConfig;
import utilities.CheckUtils;
import utilities.ControllerUtils;
import utilities.PolicyCheckUtils;

@Controller
@RequestMapping("/comics")
public class ComicController extends AbstractController {
    @Autowired private ComicService comicService;
    @Autowired private UserComicService userComicService;
    @Autowired private AuthorService authorService;
    @Autowired private PublisherService publisherService;
    @Autowired private CommentService commentService;
    @Autowired private VolumeService volumeService;

    @RequestMapping("/list")
    public ModelAndView list(@Valid SearchForm searchForm)
    {
        ModelAndView result;

        List<Comic> comics = comicService.search(searchForm.getTerms());
        List<Pair<Comic, UserComic>> comicPairs = new ArrayList<>();

        Actor principal = findPrincipal();

        if (comics != null) {
            if (principal instanceof User) {
                List<UserComic> userComics = userComicService.getForComics((User) principal, comics);
                for (UserComic userComic : userComics) {
                    comicPairs.add(Pair.of(userComic.getComic(), userComic));
                }
            } else {
                for (Comic comic : comics) {
                    comicPairs.add(Pair.<Comic, UserComic>of(comic, null));
                }
            }
        } else {
            comicPairs = comicService.findComicsAndUserComicsForPrincipal();
        }

        result = new ModelAndView("comics/list");
        result.addObject("comicPairs", comicPairs);

        return result;
    }

    @RequestMapping(value = "/star", method = RequestMethod.POST)
    public ModelAndView star(@RequestParam("id") int id, RedirectAttributes redir)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        try {
            userComicService.star(comicService.getById(id));
            redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
            return ControllerUtils.redirectToReturnAction();
        } catch (Throwable oops) {
            if (ApplicationConfig.DEBUG) oops.printStackTrace();
            redir.addFlashAttribute("globalErrorMessage", "misc.commit.error");
            return ControllerUtils.redirectToReturnAction();
        }
    }

    @RequestMapping(value = "/unstar", method = RequestMethod.POST)
    public ModelAndView unstar(@RequestParam("id") int id, RedirectAttributes redir)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        try {
            userComicService.unstar(comicService.getById(id));
            redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
            return ControllerUtils.redirectToReturnAction();
        } catch (Throwable oops) {
            if (ApplicationConfig.DEBUG) oops.printStackTrace();
            redir.addFlashAttribute("globalErrorMessage", "misc.commit.error");
            return ControllerUtils.redirectToReturnAction();
        }
    }

    @RequestMapping(value = "/set_status", method = RequestMethod.POST)
    public ModelAndView setStatus(@RequestParam("id") int id, @RequestParam("status") UserComicStatus status, RedirectAttributes redir)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        try {
            userComicService.setStatus(comicService.getById(id), status);
            redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
            return ControllerUtils.redirectToReturnAction();
        } catch(VolumesNotReadException oops) {
            redir.addFlashAttribute("globalErrorMessage", "comics.user.");
            return ControllerUtils.redirectToReturnAction();
        } catch (Throwable oops) {
            if (ApplicationConfig.DEBUG) oops.printStackTrace();
            redir.addFlashAttribute("globalErrorMessage", "misc.commit.error");
            return ControllerUtils.redirectToReturnAction();
        }
    }

    @RequestMapping(value = "/rate", method = RequestMethod.POST)
    public ModelAndView rate(@RequestParam("id") int id, @RequestParam("score") int score, RedirectAttributes redir)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        try {
            userComicService.rate(comicService.getById(id), score);
            redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
            return ControllerUtils.redirectToReturnAction();
        } catch (Throwable oops) {
            if (ApplicationConfig.DEBUG) oops.printStackTrace();
            redir.addFlashAttribute("globalErrorMessage", "misc.commit.error");
            return ControllerUtils.redirectToReturnAction();
        }
    }

    @RequestMapping(value = "/unrate", method = RequestMethod.POST)
    public ModelAndView unrate(@RequestParam("id") int id, RedirectAttributes redir)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        try {
            userComicService.unrate(comicService.getById(id));
            redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
            return ControllerUtils.redirectToReturnAction();
        } catch (Throwable oops) {
            if (ApplicationConfig.DEBUG) oops.printStackTrace();
            redir.addFlashAttribute("globalErrorMessage", "misc.commit.error");
            return ControllerUtils.redirectToReturnAction();
        }
    }

    @RequestMapping("/show")
    public ModelAndView show(@RequestParam("id") int id, Model model)
    {
        Comic comic = comicService.getById(id);
        List<Comment> comments = commentService.findForListInComic(comic);
        List<Volume> volumes = volumeService.findForListInComic(comic);

        UserComic userComic = null;

        // Gets the flashed comment from the comments/create.do handler,
        // if an error occured there. The binding result will already be in the model.
        // This is a fairly unorthodox way of doing this, but it lets us get validation feedback
        // from a completely unrelated handler without coupling it to this page, etc.
        Comment comment = (Comment) model.asMap().get("comment");
        if (comment == null) comment = new Comment();

        Actor principal = findPrincipal();
        if (principal instanceof User) {
            userComic = userComicService.getByUserAndComic((User) principal, comic);

            comment.setComic(comic);
            comment.setUser((User) principal);
            comment.setCreationTime(new Date());
        }

        ModelAndView result = new ModelAndView("comics/show");
        result.addObject("comic", comic);
        result.addObject("volumes", volumes);
        result.addObject("comments", comments);
        result.addObject("userComic", userComic);
        result.addObject("comment", comment);
        result.addObject("statuses", UserComicStatus.values());

        return result;
    }

    @RequestMapping("/new")
    public ModelAndView create()
    {
        PolicyCheckUtils.checkCanEditContent(findPrincipal());
        return createEditModelAndView("comics/new", "comics/create.do", null, null, new Comic());

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(
            @Valid Comic comic,
            BindingResult binding, RedirectAttributes redir
    )
    {
        PolicyCheckUtils.checkCanEditContent(findPrincipal());

        String globalErrorMessage = null;
        if (!binding.hasErrors()) {
            try {
                comic = comicService.create(comic);
                redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
                return ControllerUtils.redirectToReturnAction();
            } catch (Throwable oops) {
                if (ApplicationConfig.DEBUG) oops.printStackTrace();
                globalErrorMessage = "misc.commit.error";
            }
        }

        return createEditModelAndView("comics/new", "comics/create.do", globalErrorMessage, binding, comic);
    }

    @RequestMapping("/edit")
    public ModelAndView edit(@RequestParam("id") int id)
    {
        PolicyCheckUtils.checkCanEditContent(findPrincipal());
        Comic comic = comicService.getByIdForEdit(id);
        return createEditModelAndView("comics/edit", "comics/update.do", null, null, comic);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView update(
            @ModelAttribute("comic") Comic comic,
            BindingResult binding, RedirectAttributes redir
    )
    {
        PolicyCheckUtils.checkCanEditContent(findPrincipal());

        comic = comicService.bindForUpdate(comic, binding);

        String globalErrorMessage = null;
        if (!binding.hasErrors()) {
            try {
                comic = comicService.update(comic);
                redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
                return ControllerUtils.redirectToReturnAction();
            } catch (Throwable oops) {
                if (ApplicationConfig.DEBUG) oops.printStackTrace();
                globalErrorMessage = "misc.commit.error";
            }
        }

        return createEditModelAndView("comics/edit", "comics/update.do", globalErrorMessage, binding, comic);
    }

    private ModelAndView createEditModelAndView(String viewName, String formAction, String globalErrorMessage, BindingResult binding, Comic comic)
    {
        ModelAndView result = ControllerUtils.createViewWithBinding(
                viewName, "comic", comic,
                binding, globalErrorMessage
        );

        result.addObject("formAction", formAction);
        result.addObject("authors", authorService.findAll());
        result.addObject("publishers", publisherService.findAll());

        return result;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ModelAndView delete(@RequestParam("id") int id, RedirectAttributes redir)
    {
        PolicyCheckUtils.checkCanEditContent(findPrincipal());

        try {
            comicService.delete(id);
            redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
            return ControllerUtils.redirectToReturnAction();
        } catch (Throwable oops) {
            if (ApplicationConfig.DEBUG) oops.printStackTrace();
            redir.addFlashAttribute("globalErrorMessage", "misc.commit.error");
            return ControllerUtils.redirectToReturnAction();
        }

    }


}
