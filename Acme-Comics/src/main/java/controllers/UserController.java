
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import domain.Comment;
import domain.User;
import domain.UserComic;
import domain.UserComicStatus;
import exceptions.ResourceNotUniqueException;
import forms.BlockUserForm;
import forms.NewUserForm;
import security.Authority;
import security.UserAccountService;
import services.ActorService;
import services.ComicService;
import services.CommentService;
import services.MessageFolderService;
import services.UserComicService;
import services.UserService;
import utilities.ApplicationConfig;
import utilities.CheckUtils;
import utilities.ControllerUtils;
import utilities.UserAccountUtils;

@Controller
@RequestMapping("/users")
public class UserController extends AbstractController {
    @Autowired private UserService userService;
    @Autowired private UserAccountService userAccountService;
    @Autowired private MessageFolderService messageFolderService;
    @Autowired private ComicService comicService;
    @Autowired private ActorService actorService;
    @Autowired private CommentService commentService;
    @Autowired private UserComicService userComicService;


    @RequestMapping(value = "/show")
    public ModelAndView show(@RequestParam("id") int id, @RequestParam(value = "current_status", required = false) UserComicStatus currentStatus)
    {
        ModelAndView result;

        User user = userService.getByIdForShow(id);
        List<Comment> comments = commentService.findForListInUser(user);
        List<User> friends = user.getFriends();

        List<UserComicStatus> statuses = new ArrayList<>(Arrays.asList(UserComicStatus.values()));
        statuses.remove(UserComicStatus.NONE);
        if (currentStatus == null || !statuses.contains(currentStatus)) currentStatus = UserComicStatus.COMPLETED;

        List<UserComic> userComics = userComicService.findByUserAndStatus(user, currentStatus);

        result = new ModelAndView("users/show");
        result.addObject("statuses", statuses);
        result.addObject("currentStatus", currentStatus);
        result.addObject("user", user);
        result.addObject("userComics", userComics);
        result.addObject("comments", comments);
        result.addObject("friends", friends);

        return result;
    }

    @RequestMapping(value = "/new")
    public ModelAndView new_()
    {
        CheckUtils.checkUnauthenticated();

        ModelAndView result = this.createNewModelAndView(null, null, new NewUserForm());
        return result;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@Valid NewUserForm form, BindingResult binding, RedirectAttributes redir)
    {
        CheckUtils.checkUnauthenticated();

        String globalErrorMessage = null;

        if (!binding.hasErrors()) {
            try {
                User user = userService.create(form.getUsername(), form.getPassword(), form.getNickname());
                UserAccountUtils.setSessionAccount(user.getUserAccount());

                redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
                return ControllerUtils.redirect("/welcome/index.do");
            } catch (ResourceNotUniqueException ex) {
                binding.rejectValue("username", "misc.error.usernameNotUnique");
            } catch (Throwable oops) {
                if (ApplicationConfig.DEBUG) oops.printStackTrace();
                globalErrorMessage = "misc.commit.error";
            }
        }

        return createNewModelAndView(globalErrorMessage, binding, form);
    }

    public ModelAndView createNewModelAndView(String globalErrorMessage, BindingResult binding, NewUserForm form)
    {
        ModelAndView result = ControllerUtils.createViewWithBinding(
                "users/new", "form", form,
                binding, globalErrorMessage
        );

        return result;
    }

    @RequestMapping(value = "/friend", method = RequestMethod.POST)
    public ModelAndView friend(@RequestParam("id") int id, RedirectAttributes redir)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);

        userService.friend(userService.getById(id));
        redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
        return ControllerUtils.redirectToReturnAction();
    }

    @RequestMapping(value = "/unfriend", method = RequestMethod.POST)
    public ModelAndView unfriend(@RequestParam("id") int id, RedirectAttributes redir)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);

        userService.unfriend(userService.getById(id));
        redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
        return ControllerUtils.redirectToReturnAction();
    }

    @RequestMapping(value = "/trust", method = RequestMethod.POST)
    public ModelAndView trust(@RequestParam("id") int id, RedirectAttributes redir)
    {
        CheckUtils.checkPrincipalAuthority(Authority.ADMINISTRATOR);

        userService.trust(userService.getById(id));
        redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
        return ControllerUtils.redirectToReturnAction();
    }

    @RequestMapping(value = "/untrust", method = RequestMethod.POST)
    public ModelAndView untrust(@RequestParam("id") int id, RedirectAttributes redir)
    {
        CheckUtils.checkPrincipalAuthority(Authority.ADMINISTRATOR);

        userService.untrust(userService.getById(id));
        redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
        return ControllerUtils.redirectToReturnAction();
    }

    @RequestMapping(value = "/update_level", method = RequestMethod.POST)
    public ModelAndView updateLevel(@RequestParam("id") int id, RedirectAttributes redir)
    {
        CheckUtils.checkPrincipalAuthority(Authority.ADMINISTRATOR);

        userService.updateUserLevelForAdmin(userService.getById(id));
        redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
        return ControllerUtils.redirectToReturnAction();
    }

    @RequestMapping(value = "/block", method = RequestMethod.GET)
    public ModelAndView blockForm(@RequestParam("id") int id, RedirectAttributes redir)
    {
        CheckUtils.checkPrincipalAuthority(Authority.ADMINISTRATOR);
        return createBlockModelAndView(null, null, new BlockUserForm(userService.getByIdForBlock(id)));
    }

    @RequestMapping(value = "/block", method = RequestMethod.POST)
    public ModelAndView block(@Valid BlockUserForm form, BindingResult binding, RedirectAttributes redir)
    {
        CheckUtils.checkPrincipalAuthority(Authority.ADMINISTRATOR);

        String globalErrorMessage = null;

        if (!binding.hasErrors()) {
            try {
                userService.block(form.getUser(), form.getBlockReason());
                redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
                return ControllerUtils.redirectToReturnAction();
            } catch (Throwable oops) {
                if (ApplicationConfig.DEBUG) oops.printStackTrace();
                globalErrorMessage = "misc.commit.error";
            }
        }

        return createBlockModelAndView(globalErrorMessage, binding, form);
    }

    public ModelAndView createBlockModelAndView(String globalErrorMessage, BindingResult binding, BlockUserForm form)
    {
        ModelAndView result = ControllerUtils.createViewWithBinding(
                "users/block", "form", form,
                binding, globalErrorMessage
        );

        return result;
    }

    @RequestMapping(value = "/unblock", method = RequestMethod.POST)
    public ModelAndView unblock(@RequestParam("id") int id, RedirectAttributes redir)
    {
        CheckUtils.checkPrincipalAuthority(Authority.ADMINISTRATOR);

        userService.unblock(userService.getById(id));
        redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
        return ControllerUtils.redirectToReturnAction();
    }

}
