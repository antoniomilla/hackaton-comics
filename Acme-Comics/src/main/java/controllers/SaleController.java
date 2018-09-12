
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import domain.Actor;
import domain.Comment;
import domain.DirectMessage;
import domain.Sale;
import domain.SaleStatus;
import domain.User;
import security.Authority;
import services.ComicService;
import services.CommentService;
import services.DirectMessageService;
import services.SaleService;
import services.UserService;
import utilities.ApplicationConfig;
import utilities.CheckUtils;
import utilities.ControllerUtils;

@Controller
@RequestMapping("/sales")
public class SaleController extends AbstractController {
	@Autowired private CommentService commentService;
	@Autowired private ComicService comicService;
	@Autowired private SaleService saleService;
	@Autowired private UserService userService;
	@Autowired private DirectMessageService directMessageService;
	@Autowired private MessageSource messageSource;

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(value = "mine", required = false, defaultValue = "0") int mine)
	{
		List<Sale> sales;
		if (mine != 0) {
			CheckUtils.checkPrincipalAuthority(Authority.USER);
			sales = saleService.findByUserOrInterestedUser((User) getPrincipal());
		} else {
			sales = saleService.findByStatus(SaleStatus.SELLING);
		}
		ModelAndView result = new ModelAndView("sales/list");
		result.addObject("sales", sales);
		result.addObject("showStatus", mine != 0);

		return result;
	}

	@RequestMapping("/conversation")
	public ModelAndView conversation(@RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam("id") int id, @RequestParam("user") int userId)
	{
		CheckUtils.checkPrincipalAuthority(Authority.USER);

		Sale sale = saleService.getById(id);
		User buyer = userService.getById(userId);
		User seller = sale.getUser();
		User principal = (User) getPrincipal();

		CheckUtils.checkTrue(!buyer.equals(seller));
		CheckUtils.checkTrue(sale.getInterestedUsers().contains(buyer));
		CheckUtils.checkTrue(principal.equals(seller) || principal.equals(buyer));

		DirectMessage directMessage = new DirectMessage();
		directMessage.setSender(principal);
		if (principal.equals(seller)) {
			directMessage.setRecipient(buyer);
		} else {
			directMessage.setRecipient(seller);
		}
		directMessage.setSale(sale);
		directMessage.setSubject(messageSource.getMessage("direct_messages.saleSubjectPrefix", null, LocaleContextHolder.getLocale()).trim() + " " + sale.getName());
		directMessage.setCreationTime(new Date());

		return createConversationModelAndView("sales/conversation", "sales/conversation_send.do", null, null, directMessage, sale, seller, buyer, page);
	}

	@RequestMapping(value = "/conversation_send", method = RequestMethod.POST)
	public ModelAndView conversationSend(@RequestParam(value = "page", required = false, defaultValue = "0") int page, @ModelAttribute("directMessage") DirectMessage directMessage, BindingResult binding, RedirectAttributes redir)
	{
		CheckUtils.checkPrincipalAuthority(Authority.USER);

		Sale sale = directMessage.getSale();
		User principal = (User) getPrincipal();
		User buyer;
		User seller = sale.getUser();
		if (principal.equals(seller)) {
			buyer = (User) directMessage.getRecipient();
		} else {
			buyer = principal;
		}

		CheckUtils.checkTrue(!buyer.equals(seller));
		CheckUtils.checkTrue(sale.getInterestedUsers().contains(buyer));
		CheckUtils.checkTrue(principal.equals(seller) || principal.equals(buyer));

		String globalErrorMessage = null;

		try {
			directMessageService.send(directMessage, binding);
			redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
			redir.addAttribute("id", sale.getId());
			redir.addAttribute("user", buyer.getId());
			return ControllerUtils.redirect("/sales/conversation.do");
		} catch (ConstraintViolationException oops) {
			// Errors are in binding.
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			globalErrorMessage = "misc.commit.error";
		}

		return createConversationModelAndView("sales/conversation", "sales/conversation_send.do", globalErrorMessage, binding, directMessage, sale, seller, buyer, page);
	}

	private ModelAndView createConversationModelAndView(String viewName, String formAction, String globalErrorMessage, BindingResult binding, DirectMessage directMessage, Sale sale, User seller, User buyer, int page)
	{
		ModelAndView result = ControllerUtils.createViewWithBinding(
				viewName, "directMessage", directMessage,
				binding, globalErrorMessage
		);

		Page<DirectMessage> directMessages = directMessageService.findBySaleAndUsers(sale, seller, buyer, new PageRequest(page, getDisplayTagPageSize()));

		for (DirectMessage message : directMessages.getContent()) {
			directMessageService.read(message);
		}

		result.addObject("formAction", formAction);
		result.addObject("directMessages", directMessages);
		result.addObject("directMessage", directMessage);

		return result;
	}

	@RequestMapping("/show")
	public ModelAndView show(@RequestParam("id") int id, Model model)
	{
		Sale sale = saleService.getById(id);

		List<Comment> comments = commentService.findForListInSale(sale);

		// Gets the flashed comment from the comments/create.do handler,
		// if an error occured there. The binding result will already be in the model.
		// This is a fairly unorthodox way of doing this, but it lets us get validation feedback
		// from a completely unrelated handler without coupling it to this page, etc.
		Comment comment = (Comment) model.asMap().get("comment");
		if (comment == null) comment = new Comment();

		Actor principal = findPrincipal();
		if (principal instanceof User) {
			comment.setSale(sale);
			comment.setUser((User) principal);
			comment.setCreationTime(new Date());
		}

		ModelAndView result = new ModelAndView("sales/show");
		result.addObject("sale", sale);
		result.addObject("comment", comment);
		result.addObject("comments", comments);

		return result;
	}

	@RequestMapping("/new")
	public ModelAndView new_()
	{
		CheckUtils.checkPrincipalAuthority(Authority.USER);

		User principal = (User) getPrincipal();

		Sale sale = new Sale();
		sale.setUser(principal);
		sale.setCreationTime(new Date());
		sale.setStatus(SaleStatus.SELLING);
		
		return createEditModelAndView("sales/new", "sales/create.do", null, null, sale);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView create(@Valid @ModelAttribute("sale") Sale sale, BindingResult binding, RedirectAttributes redir)
	{
		CheckUtils.checkPrincipalAuthority(Authority.USER);

		String globalErrorMessage = null;

		try {
			if (!binding.hasErrors()) {
				sale = saleService.create(sale);
				redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
				redir.addAttribute("id", sale.getId());
				return ControllerUtils.redirect("/sales/show.do");
			}
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			globalErrorMessage = "misc.commit.error";
		}

		return createEditModelAndView("sales/new", "sales/create.do", globalErrorMessage, binding, sale);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam("id") int id)
	{
		CheckUtils.checkPrincipalAuthority(Authority.USER);
		Sale sale = saleService.getByIdForEdit(id);
		return createEditModelAndView("sales/edit", "sales/update.do", null, null, sale);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(@ModelAttribute("sale") Sale sale, BindingResult binding, RedirectAttributes redir)
	{
		CheckUtils.checkPrincipalAuthority(Authority.USER);

		String globalErrorMessage = null;

		try {
			sale = saleService.bindForUpdate(sale, binding);
			if (!binding.hasErrors()) {
				saleService.update(sale);
				redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
				return ControllerUtils.redirectToReturnAction();
			}
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			globalErrorMessage = "misc.commit.error";
		}

		return createEditModelAndView("sales/edit", "sales/update.do", globalErrorMessage, binding, sale);
	}

	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public ModelAndView cancel(@RequestParam("id") int id, RedirectAttributes redir)
	{
		CheckUtils.checkPrincipalAuthority(Authority.USER);

		try {
			saleService.cancel(saleService.getByIdForEdit(id));
			redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
			return ControllerUtils.redirectToReturnAction();
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			redir.addFlashAttribute("globalErrorMessage", "misc.commit.error");
		}

		return ControllerUtils.redirectToReturnAction();
	}

	@RequestMapping(value = "/complete", method = RequestMethod.POST)
	public ModelAndView complete(@RequestParam("id") int id, @RequestParam("user") int userSoldToId, RedirectAttributes redir)
	{
		CheckUtils.checkPrincipalAuthority(Authority.USER);

		try {
			User userSoldTo = userService.getById(userSoldToId);
			saleService.complete(saleService.getByIdForEdit(id), userSoldTo);
			redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
			return ControllerUtils.redirectToReturnAction();
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			redir.addFlashAttribute("globalErrorMessage", "misc.commit.error");
		}

		return ControllerUtils.redirectToReturnAction();
	}

	@RequestMapping(value = "/set_interested", method = RequestMethod.POST)
	public ModelAndView setInterested(@RequestParam("id") int id, @RequestParam("interested") int interested, RedirectAttributes redir)
	{
		CheckUtils.checkPrincipalAuthority(Authority.USER);

		try {
			saleService.setInterested(saleService.getById(id), interested != 0);
			redir.addFlashAttribute("globalSuccessMessage", "misc.operationCompletedSuccessfully");
			return ControllerUtils.redirectToReturnAction();
		} catch (Throwable oops) {
			if (ApplicationConfig.DEBUG) oops.printStackTrace();
			redir.addFlashAttribute("globalErrorMessage", "misc.commit.error");
		}

		return ControllerUtils.redirectToReturnAction();
	}


	private ModelAndView createEditModelAndView(String viewName, String formAction, String globalErrorMessage, BindingResult binding, Sale sale)
	{
		ModelAndView result = ControllerUtils.createViewWithBinding(
				viewName, "sale", sale,
				binding, globalErrorMessage
		);

		result.addObject("formAction", formAction);
		result.addObject("comics", comicService.findAll());

		return result;
	}
}
