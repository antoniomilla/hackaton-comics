package services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Objects;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import domain.Actor;
import domain.MessageFolder;
import domain.MessageFolderType;
import repositories.MessageFolderRepository;
import utilities.AbstractTest;

/**
 * Tests the following use cases from the requirements document:
 * - Create message folders.
 * - Rename or delete message folders, except system folders.
 */
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MessageFolderServiceTest extends AbstractTest {
    @Autowired private MessageFolderService messageFolderService;
    @Autowired private MessageFolderRepository messageFolderRepository;

    private MessageFolder getNamedFolder(Actor actor, String name)
    {
        for (MessageFolder messageFolder : actor.getMessageFolders()) {
            if (Objects.equals(messageFolder.getName(), name)) return messageFolder;
        }
        throw new IllegalArgumentException("Folder " + name + " not found");
    }

    // Test to create a message folder.
    @Test
    public void testCreate()
    {
        authenticate("user1");

        MessageFolder folder = new MessageFolder();
        folder.setActor(getPrincipal());
        folder.setType(MessageFolderType.USER);
        folder.setName("TEST testCreate");

        folder = messageFolderService.create(folder);

        flushTransaction();

        Assert.assertTrue(getPrincipal().getMessageFolders().contains(folder));

        folder = messageFolderRepository.findOne(folder.getId());
        Assert.assertTrue(Objects.equals(folder.getActor(), getPrincipal()));
        Assert.assertTrue(Objects.equals(folder.getName(), "TEST testCreate"));
        Assert.assertTrue(Objects.equals(folder.getType(), MessageFolderType.USER));
    }

    // Test that only type=USER folders can be created.
    @Test(expected = AccessDeniedException.class)
    public void testCreateFailsIfFolderTypeNotUser()
    {
        authenticate("user1");

        MessageFolder folder = new MessageFolder();
        folder.setActor(getPrincipal());
        folder.setType(MessageFolderType.SYSTEM_INBOX);
        folder.setName("TEST testCreate");

        folder = messageFolderService.create(folder);

        flushTransaction();

        Assert.assertTrue(getPrincipal().getMessageFolders().contains(folder));
    }

    // Test that the user in the folder created must be the principal
    @Test(expected = AccessDeniedException.class)
    public void testCreateFailsIfWrongPrincipal()
    {
        authenticate("user1");

        MessageFolder folder = new MessageFolder();
        folder.setActor(getActor("admin"));
        folder.setType(MessageFolderType.USER);
        folder.setName("TEST testCreate");

        folder = messageFolderService.create(folder);

        flushTransaction();

        Assert.assertTrue(getPrincipal().getMessageFolders().contains(folder));
    }


    // Test that it fails if the data is bad.
    @Test(expected = ConstraintViolationException.class)
    public void testCreateFailsIfBadData()
    {
        authenticate("user1");

        MessageFolder folder = new MessageFolder();
        folder.setActor(getPrincipal());
        folder.setType(MessageFolderType.USER);
        folder.setName(null);

        messageFolderService.create(folder);

        flushTransaction();
    }

    // Test to update (rename) a folder.
    @Test
    public void testUpdate()
    {
        authenticate("user1");

        MessageFolder folder = getNamedFolder(getPrincipal(), "My custom folder");
        folder.setName("TEST testUpdate");

        folder = messageFolderService.update(folder);

        flushTransaction();

        Assert.assertTrue(getPrincipal().getMessageFolders().contains(folder));
    }

    // Test that it fails if the folder type is not USER.
    @Test(expected = AccessDeniedException.class)
    public void testUpdateFailsIfFolderTypeNotUser()
    {
        authenticate("user1");

        MessageFolder folder = getNamedFolder(getPrincipal(), "My custom folder");
        folder.setType(MessageFolderType.SYSTEM_INBOX);
        folder.setName("TEST testUpdate");

        messageFolderService.update(folder);

        folder = messageFolderRepository.findOne(folder.getId());
        Assert.assertTrue(Objects.equals(folder.getName(), "TEST testUpdate"));
    }

    // Test that it fails if the principal is wrong.
    @Test(expected = AccessDeniedException.class)
    public void testUpdateFailsIfWrongPrincipal()
    {
        authenticate("user2");

        MessageFolder folder = getNamedFolder(getActor("user1"), "My custom folder");
        folder.setName("TEST testUpdate");

        messageFolderService.update(folder);
    }

    // Test that it fails if the data is bad.
    @Test(expected = ConstraintViolationException.class)
    public void testUpdateFailsIfBadData()
    {
        authenticate("user1");

        MessageFolder folder = getNamedFolder(getActor("user1"), "My custom folder");
        folder.setName(null);

        messageFolderService.update(folder);

        flushTransaction();
    }

    // Test to delete a folder.
    @Test
    public void testDelete()
    {
        authenticate("user1");
        MessageFolder folder = getNamedFolder(getActor("user1"), "My custom folder");

        int id = folder.getId();
        messageFolderService.delete(id);

        flushTransaction();

        Assert.assertTrue(messageFolderRepository.findOne(id) == null);
    }

    // Test that it fails if the folder type is not USER.
    @Test
    public void testDeleteFailsIfFolderTypeNotUser()
    {
        authenticate("user1");

        for (MessageFolderType type: Arrays.asList(MessageFolderType.SYSTEM_INBOX, MessageFolderType.SYSTEM_SENT, MessageFolderType.SYSTEM_TRASH)) {
            MessageFolder folder = messageFolderService.getSystemFolderForActor(getPrincipal(), type);

            Exception caught = null;
            try {
                messageFolderService.delete(folder.getId());
            } catch (AccessDeniedException e) {
                caught = e;
            }
            Assert.assertTrue(caught != null);

            flushTransaction();
            rollbackTransaction();
            startTransaction();
        }
    }
}
