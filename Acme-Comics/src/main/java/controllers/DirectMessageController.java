
package controllers;

import org.displaytag.util.ParamEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import domain.Actor;
import domain.DirectMessage;
import domain.MessageFolder;
import domain.MessageFolderType;
import services.ActorService;
import services.DirectMessageService;
import services.MessageFolderService;
import services.UserService;
import utilities.ApplicationConfig;
import utilities.CheckUtils;
import utilities.ControllerUtils;
import utilities.HttpServletUtils;
import utilities.JspViewUtils;

@Controller
@RequestMapping("/direct_messages")
public class DirectMessageController extends AbstractController {

    @Autowired private DirectMessageService directMessageService;
    @Autowired private MessageFolderService messageFolderService;
    @Autowired private ActorService actorService;
    @Autowired private UserService userService;
    @PersistenceContext private EntityManager entityManager;
    @Autowired private MessageSource messageSource;

    @RequestMapping("/index")
    public ModelAndView index(@RequestParam(value = "folder", required = false) Integer messageFolderId)
    {
        CheckUtils.checkAuthenticated();

        MessageFolder messageFolder = null;
        Actor principal = getPrincipal();
        if (messageFolderId != null) {
            messageFolder = messageFolderService.findByIdForIndex(messageFolderId);
        }
        if (messageFolder == null) {
            messageFolder = messageFolderService.getSystemFolderForActor(principal, MessageFolderType.SYSTEM_INBOX);
        }

        ModelAndView result = new ModelAndView("direct_messages/index");
        result.addObject("currentMessageFolder", messageFolderService.populateDisplayName(messageFolder));
        result.addObject("messageFolders", messageFolderService.populateDisplayNames(principal.getMessageFolders()));
        result.addObject("directMessages", directMessageService.findByMessageFolderForIndex(messageFolder));

        return result;
    }

    @RequestMapping("/show")
    public ModelAndView show(@RequestParam("id") int id)
    {
        final DirectMessage directMessage = directMessageService.getById(id);

        ModelAndView result = new ModelAndView("direct_messages/show");
        messageFolderService.populateDisplayName(directMessage.getMessageFolder());
        directMessageService.read(directMessage);
        result.addObject("directMessage", directMessage);

        return result;
    }

    @RequestMapping("/new")
    public ModelAndView new_(@RequestParam("recipient") int recipientId)
    {
        CheckUtils.checkAuthenticated();

        Actor recipient = actorService.getById(recipientId);

        DirectMessage directMessage = new DirectMessage();
        directMessage.setSender(findPrincipal());
        directMessage.setRecipient(recipient);

        return createEditModelAndView("direct_messages/new", "direct_messages/send.do", null, null, directMessage);
    }

    @RequestMapping("/reply")
    public ModelAndView reply(@RequestParam("id") int id)
    {
        CheckUtils.checkAuthenticated();

        Actor principal = getPrincipal();

        DirectMessage original = directMessageService.getByIdForMoveOrReply(id);
        DirectMessage directMessage = new DirectMessage(original);

        directMessage.setSender(principal);
        directMessage.setRecipient(original.getSender());
        if (directMessage.getRecipient().equals(principal)) {
            directMessage.setRecipient(original.getRecipient());
        }
        directMessage.setAdministrationNotice(false);

        directMessage.setSubject(messageSource.getMessage("direct_messages.replySubjectPrefix", null, LocaleContextHolder.getLocale()).trim() + " " + directMessage.getSubject());
        directMessage.setBody(original.getBody().replaceAll("^", "> "));

        directMessage.setSale(original.getSale());

        return createEditModelAndView("direct_messages/new", "direct_messages/send.do", null, null, directMessage);
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public ModelAndView send(@ModelAttribute("directMessage") DirectMessage directMessage, BindingResult binding, RedirectAttributes redir)
    {
        CheckUtils.checkAuthenticated();

        String globalErrorMessage = null;

        try {
            if (directMessageService.send(directMessage, binding)) {
                redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
                return ControllerUtils.redirectToReturnAction();
            }
        } catch (Throwable oops) {
            if (ApplicationConfig.DEBUG) oops.printStackTrace();
            globalErrorMessage = "misc.commit.error";
        }

        return createEditModelAndView("direct_messages/new", "direct_messages/send.do", globalErrorMessage, binding, directMessage);
    }

    @RequestMapping("/move")
    public ModelAndView move(@RequestParam("id") int id)
    {
        CheckUtils.checkAuthenticated();

        DirectMessage directMessage = directMessageService.getByIdForMoveOrReply(id);

        return createMoveModelAndView("direct_messages/move", "direct_messages/move_to.do", null, null, directMessage);
    }

    @RequestMapping(value = "/move_to", method = RequestMethod.POST)
    public ModelAndView move(@ModelAttribute("directMessage") DirectMessage directMessage, RedirectAttributes redir)
    {
        entityManager.detach(directMessage);

        CheckUtils.checkAuthenticated();

        String globalErrorMessage = null;

        try {
            DirectMessage realDirectMessage = directMessageService.getByIdForMoveOrReply(directMessage.getId());
            directMessageService.move(realDirectMessage, directMessage.getMessageFolder());
            redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
            return ControllerUtils.redirectToReturnAction();
        } catch (Throwable oops) {
            if (ApplicationConfig.DEBUG) oops.printStackTrace();
            globalErrorMessage = "misc.commit.error";
        }

        return createMoveModelAndView("direct_messages/move", "direct_messages/move_to.do", globalErrorMessage, null, directMessage);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ModelAndView delete(@RequestParam("id") int id, RedirectAttributes redir)
    {
        CheckUtils.checkAuthenticated();

        try {
            directMessageService.delete(id);
            redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
            return ControllerUtils.redirectToReturnAction();
        } catch (Throwable oops) {
            if (ApplicationConfig.DEBUG) oops.printStackTrace();
            redir.addFlashAttribute("globalErrorMessage", "misc.commit.error");
            return ControllerUtils.redirectToReturnAction();
        }
    }

    private ModelAndView createEditModelAndView(String viewName, String formAction, String globalErrorMessage, BindingResult binding, DirectMessage directMessage)
    {
        ModelAndView result = ControllerUtils.createViewWithBinding(
                viewName, "directMessage", directMessage,
                binding, globalErrorMessage
        );

        result.addObject("formAction", formAction);

        return result;
    }

    private ModelAndView createMoveModelAndView(String viewName, String formAction, String globalErrorMessage, BindingResult binding, DirectMessage directMessage)
    {
        ModelAndView result = ControllerUtils.createViewWithBinding(
                viewName, "directMessage", directMessage,
                binding, globalErrorMessage
        );

        result.addObject("formAction", formAction);
        result.addObject("messageFolders", messageFolderService.populateDisplayNames(getPrincipal().getMessageFolders()));

        return result;
    }
}
