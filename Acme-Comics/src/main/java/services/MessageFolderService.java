
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import domain.Actor;
import domain.DirectMessage;
import domain.MessageFolder;
import domain.MessageFolderType;
import exceptions.ResourceNotFoundException;
import repositories.MessageFolderRepository;
import utilities.CheckUtils;

@Service
@Transactional
public class MessageFolderService {
    @Autowired private MessageFolderRepository repository;
    @Autowired private ActorService actorService;
    @PersistenceContext private EntityManager entityManager;
    @Autowired private Validator validator;
    @Autowired private DirectMessageService directMessageService;

    @Autowired private MessageSource messageSource;

    public MessageFolder getSystemFolderForActor(Actor actor, MessageFolderType type)
    {
        Assert.isTrue(type != MessageFolderType.USER);
        MessageFolder result = repository.findSystemFolderForActor(actor, type);
        Assert.notNull(result);
        return result;
    }

    void createSystemFoldersForNewActor(Actor actor)
    {
        for (MessageFolderType type : MessageFolderType.values()) {
            if (type == MessageFolderType.USER) continue;
            MessageFolder folder = new MessageFolder(actor);
            folder.setType(type);
            repository.save(folder);
        }
    }

    public MessageFolder create(MessageFolder messageFolder)
    {
        CheckUtils.checkAuthenticated();
        CheckUtils.checkTrue(messageFolder.getType() == MessageFolderType.USER);
        CheckUtils.checkIsPrincipal(messageFolder.getActor());
        
        return repository.save(messageFolder);
    }

    public MessageFolder findByIdForIndex(int id)
    {
        CheckUtils.checkAuthenticated();

        MessageFolder result = findById(id);
        if (result != null) CheckUtils.checkIsPrincipal(result.getActor());

        return result;
    }

    public MessageFolder getByIdForEdit(int id)
    {
        CheckUtils.checkAuthenticated();

        MessageFolder result = getById(id);
        CheckUtils.checkIsPrincipal(result.getActor());
        CheckUtils.checkTrue(result.getType() == MessageFolderType.USER);
        
        return result;
    }

    private MessageFolder getById(int id)
    {
        MessageFolder result = findById(id);
        if (result == null) throw new ResourceNotFoundException();
        return result;
    }

    private MessageFolder findById(int id)
    {
        return repository.findOne(id);
    }

    public MessageFolder bindForUpdate(MessageFolder messageFolder, BindingResult binding)
    {
        // Hibernate is in the dirty habit of automatically persisting any managed entities
        // at the end of the transaction, even if it was never saved. An attacker can force
        // the system to load a managed entity using a parameter (which gets parsed by the converter which loads the entity),
        // which can later be modified by the bound model attributes before our code is even called.
        // We're not going to save this entity, so detach it just in case.
        entityManager.detach(messageFolder);

        MessageFolder oldMessageFolder = getById(messageFolder.getId());
        CheckUtils.checkSameVersion(messageFolder, oldMessageFolder);

        oldMessageFolder.setName(messageFolder.getName());

        validator.validate(oldMessageFolder, binding);
        if (binding.hasErrors()) entityManager.detach(oldMessageFolder);

        return oldMessageFolder;
    }

    public MessageFolder update(MessageFolder messageFolder)
    {
        CheckUtils.checkAuthenticated();
        CheckUtils.checkTrue(messageFolder.getType() == MessageFolderType.USER);
        CheckUtils.checkIsPrincipal(messageFolder.getActor());

        return repository.save(messageFolder);
    }

    public void delete(int id)
    {
        MessageFolder messageFolder = getByIdForEdit(id);

        for (DirectMessage directMessage : messageFolder.getMessages()) {
            directMessageService.delete(directMessage.getId());
        }

        repository.delete(id);
    }

    public List<MessageFolder> populateDisplayNames(List<MessageFolder> messageFolders)
    {
        for (MessageFolder messageFolder : messageFolders) {
            populateDisplayName(messageFolder);
        }
        return messageFolders;
    }

    public MessageFolder populateDisplayName(MessageFolder messageFolder)
    {
        if (messageFolder.getName() != null) {
            messageFolder.setNameForDisplay(messageFolder.getName());
        } else {
            messageFolder.setNameForDisplay(messageSource.getMessage("message_folders.system." + messageFolder.getType().name(), null, LocaleContextHolder.getLocale()));
        }
        return messageFolder;
    }
}
