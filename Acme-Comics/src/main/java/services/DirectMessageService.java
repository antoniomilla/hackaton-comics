
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import domain.Actor;
import domain.Administrator;
import domain.DirectMessage;
import domain.MessageFolder;
import domain.MessageFolderType;
import domain.Sale;
import domain.SaleStatus;
import domain.User;
import exceptions.ResourceNotFoundException;
import forms.MassMailType;
import repositories.DirectMessageRepository;
import security.Authority;
import utilities.CheckUtils;

@Service
@Transactional
public class DirectMessageService {
    @Autowired private DirectMessageRepository repository;
    @Autowired private ActorService actorService;
    @Autowired private Validator validator;
    @Autowired private MessageFolderService messageFolderService;
    @PersistenceContext private EntityManager entityManager;

    public DirectMessage getById(int id)
    {
        DirectMessage result = findById(id);
        if (result == null) throw new ResourceNotFoundException();
        return result;
    }

    private DirectMessage findById(int id)
    {
        return repository.findOne(id);
    }

    public boolean send(DirectMessage directMessage, BindingResult binding)
    {
        // Hibernate is in the dirty habit of automatically persisting any managed entities
        // at the end of the transaction, even if it was never saved. An attacker can force
        // the system to load a managed entity using a parameter (which gets parsed by the converter which loads the entity),
        // which can later be modified by the bound model attributes before our code is even called.
        // We're not going to save this entity, so detach it just in case.
        entityManager.detach(directMessage);

        CheckUtils.checkAuthenticated();
        CheckUtils.checkIsPrincipal(directMessage.getSender());

        if (!directMessage.getSender().isAdministrator()) {
            CheckUtils.checkFalse(directMessage.getAdministrationNotice());

            if (directMessage.getSale() == null && directMessage.getRecipient() instanceof User && ((User) directMessage.getRecipient()).getOnlyFriendsCanSendDms()) {
                CheckUtils.checkTrue(((User) directMessage.getRecipient()).getFriends().contains((User) directMessage.getSender()));
            }
        }

        if (directMessage.getSale() != null) {
            CheckUtils.checkTrue(directMessage.getSale().getStatus() == SaleStatus.SELLING);

            CheckUtils.checkTrue(directMessage.getSender() instanceof User && directMessage.getRecipient() instanceof User);

            boolean senderSaleOwner = directMessage.getSale().getUser().equals(directMessage.getSender());
            boolean senderSaleInterestedUser = directMessage.getSale().getInterestedUsers().contains((User) directMessage.getSender());
            boolean recipientSaleOwner = directMessage.getSale().getUser().equals(directMessage.getRecipient());
            boolean recipientSaleInterestedUser = directMessage.getSale().getInterestedUsers().contains((User) directMessage.getRecipient());

            CheckUtils.checkTrue((senderSaleOwner || senderSaleInterestedUser) && (recipientSaleOwner || recipientSaleInterestedUser) && (senderSaleOwner || recipientSaleOwner));
        }

        DirectMessage dm1 = new DirectMessage(directMessage), dm2 = new DirectMessage(directMessage);
        dm1.setMessageFolder(messageFolderService.getSystemFolderForActor(directMessage.getSender(), MessageFolderType.SYSTEM_SENT));
        dm2.setMessageFolder(messageFolderService.getSystemFolderForActor(directMessage.getRecipient(), MessageFolderType.SYSTEM_INBOX));

        dm1.setCreationTime(new Date());
        dm2.setCreationTime(dm1.getCreationTime());

        validator.validate(dm1, binding);
        if (binding.hasErrors()) return false;
        validator.validate(dm2, binding);
        if (binding.hasErrors()) return false;

        repository.save(dm1);
        repository.save(dm2);

        return true;
    }

    public boolean sendMassMail(MassMailType type, DirectMessage directMessage, BindingResult binding)
    {
        CheckUtils.checkPrincipalAuthority(Authority.ADMINISTRATOR);

        Administrator principal = (Administrator) actorService.getPrincipal();
        Date creationTime = new Date();
        for (Actor actor : actorService.findAll()) {
            boolean shouldSend = false;
            if (type == MassMailType.ALL) shouldSend = true;
            if (actor instanceof User) {
                if (type == MassMailType.ALL_USERS) shouldSend = true;
                if (((User) actor).getTrusted()) {
                    if (type == MassMailType.TRUSTED_USERS) shouldSend = true;
                    if (type == MassMailType.TRUSTED_USERS_AND_ADMINISTRATORS) shouldSend = true;
                } else {
                    if (type == MassMailType.REGULAR_USERS) shouldSend = true;
                }
            }
            if (actor instanceof Administrator) {
                if (type == MassMailType.TRUSTED_USERS_AND_ADMINISTRATORS) shouldSend = true;
                if (type == MassMailType.ADMINISTRATORS) shouldSend = true;
            }

            if (!shouldSend) continue;

            DirectMessage dm = new DirectMessage(directMessage);
            dm.setSender(principal);
            dm.setRecipient(actor);
            dm.setMessageFolder(messageFolderService.getSystemFolderForActor(dm.getRecipient(), MessageFolderType.SYSTEM_INBOX));
            dm.setCreationTime(creationTime);
            validator.validate(dm, binding);
            if (binding.hasErrors()) return false;
            repository.save(dm);
        }

        DirectMessage dm = new DirectMessage(directMessage);
        dm.setSender(principal);
        dm.setRecipient(principal);
        dm.setMessageFolder(messageFolderService.getSystemFolderForActor(principal, MessageFolderType.SYSTEM_SENT));
        dm.setCreationTime(creationTime);
        validator.validate(dm, binding);
        if (binding.hasErrors()) return false;
        repository.save(dm);

        return true;
    }

    public DirectMessage move(DirectMessage directMessage, MessageFolder messageFolder)
    {
        CheckUtils.checkAuthenticated();
        CheckUtils.checkIsPrincipal(directMessage.getMessageFolder().getActor());
        CheckUtils.checkIsPrincipal(messageFolder.getActor());
        directMessage.setMessageFolder(messageFolder);
        return repository.save(directMessage);
    }

    public void delete(int id)
    {
        CheckUtils.checkAuthenticated();
        MessageFolder trashFolder = messageFolderService.getSystemFolderForActor(actorService.getPrincipal(), MessageFolderType.SYSTEM_TRASH);
        DirectMessage directMessage = getByIdForMoveOrReply(id);

        move(directMessage, trashFolder);
    }

    public DirectMessage getByIdForMoveOrReply(int id)
    {
        CheckUtils.checkAuthenticated();

        DirectMessage directMessage = getById(id);
        CheckUtils.checkIsPrincipal(directMessage.getMessageFolder().getActor());

        return directMessage;
    }

    public List<DirectMessage> findByMessageFolderForIndex(MessageFolder messageFolder)
    {
        CheckUtils.checkAuthenticated();
        CheckUtils.checkIsPrincipal(messageFolder.getActor());

        return repository.findByMessageFolderOrderByCreationTimeDesc(messageFolder);
    }

    public DirectMessage read(DirectMessage directMessage)
    {
        CheckUtils.checkAuthenticated();
        CheckUtils.checkIsPrincipal(directMessage.getMessageFolder().getActor());

        if (!directMessage.getReadByUser()) {
            directMessage.setReadByUser(true);
            return repository.save(directMessage);
        } else {
            return directMessage;
        }
    }

    public Page<DirectMessage> findBySaleAndUsers(Sale sale, User seller, User buyer, PageRequest pageRequest)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        Actor principal = actorService.getPrincipal();
        CheckUtils.checkTrue(principal.equals(seller) || principal.equals(buyer));
        CheckUtils.checkTrue(sale.getUser().equals(seller));
        CheckUtils.checkTrue(sale.getInterestedUsers().contains(buyer));

        return repository.findBySaleAndUsers(sale, (User) principal, buyer, pageRequest);
    }
}
