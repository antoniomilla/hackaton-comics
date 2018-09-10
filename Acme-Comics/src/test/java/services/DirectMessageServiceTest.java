package services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import domain.DirectMessage;
import domain.MessageFolder;
import domain.MessageFolderType;
import forms.MassMailType;
import utilities.AbstractTest;

/**
 * Tests the following use cases from the requirements document:
 * - Send messages to other actors.
 * - List, display and delete (move to trash, no actual deletion is involved) his own messages.
 * - Move messages between folders.
 * - Talk with the seller about the sale.
 * - As administrator, send messages to any user, even if the user rejects messages from everyone but their friends.
 * - Massively send a message to all users, all trusted users, all trusted users and administrators, or all administrators.
 * - When sending or massively sending a message, mark it as an Administration Notice, which is visually emphasized in the message list.
 */
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DirectMessageServiceTest extends AbstractTest {
    @Autowired private DirectMessageService directMessageService;
    @Autowired private MessageFolderService messageFolderService;

    private boolean folderContainsMessage(MessageFolder folder, DirectMessage dm)
    {
        for (DirectMessage dm2 : folder.getMessages()) {
            boolean found = true;
            found &= Objects.equals(dm.getSubject(), dm2.getSubject());
            found &= Objects.equals(dm.getBody(), dm2.getBody());
            found &= Objects.equals(dm.getAdministrationNotice(), dm2.getAdministrationNotice());
            if (found) return true;
        }

        return false;
    }

    // Test to send a message as an user.
    @Test
    public void testSendUser()
    {
        authenticate("user1");

        DirectMessage dm = new DirectMessage();
        dm.setSender(getPrincipal());
        dm.setRecipient(getActor("user2"));
        dm.setSubject("TEST SUBJECT 34593475823");
        dm.setBody("Test body");
        dm.setCreationTime(new Date());

        directMessageService.send(dm, new BeanPropertyBindingResult(dm, "directMessage"));

        flushTransaction();

        Assert.assertTrue(folderContainsMessage(messageFolderService.getSystemFolderForActor(getPrincipal(), MessageFolderType.SYSTEM_SENT), dm));
        Assert.assertTrue(folderContainsMessage(messageFolderService.getSystemFolderForActor(getActor("user2"), MessageFolderType.SYSTEM_INBOX), dm));
    }

    // Test to send a message as an admin.
    @Test
    public void testSendAdmin()
    {
        authenticate("admin");

        DirectMessage dm = new DirectMessage();
        dm.setSender(getPrincipal());
        dm.setRecipient(getActor("user2"));
        dm.setSubject("TEST SUBJECT 34593475823");
        dm.setBody("Test body");
        dm.setCreationTime(new Date());
        dm.setAdministrationNotice(true);

        directMessageService.send(dm, new BeanPropertyBindingResult(dm, "directMessage"));

        flushTransaction();

        Assert.assertTrue(folderContainsMessage(messageFolderService.getSystemFolderForActor(getPrincipal(), MessageFolderType.SYSTEM_SENT), dm));
        Assert.assertTrue(folderContainsMessage(messageFolderService.getSystemFolderForActor(getActor("user2"), MessageFolderType.SYSTEM_INBOX), dm));
    }

    // Test that it fails if the sender is not the principal.
    @Test(expected = AccessDeniedException.class)
    public void testSendFailsIfWrongPrincipal()
    {
        authenticate("user1");

        DirectMessage dm = new DirectMessage();
        dm.setSender(getActor("user3"));
        dm.setRecipient(getActor("user2"));
        dm.setSubject("TEST SUBJECT 34593475823");
        dm.setBody("Test body");
        dm.setCreationTime(new Date());

        directMessageService.send(dm, new BeanPropertyBindingResult(dm, "directMessage"));
    }

    // Test that it fails if the data is bad.
    @Test(expected = ConstraintViolationException.class)
    public void testSendFailsIfBadData()
    {
        authenticate("user1");

        DirectMessage dm = new DirectMessage();
        dm.setSender(getPrincipal());
        dm.setRecipient(getActor("user2"));
        dm.setSubject(null);
        dm.setBody(null);
        dm.setCreationTime(new Date());

        directMessageService.send(dm, new BeanPropertyBindingResult(dm, "directMessage"));
    }

    // Test that it fails if an user tries to send an admin notice.
    @Test(expected = AccessDeniedException.class)
    public void testSendFailsIfUserSendsAdminNotice()
    {
        authenticate("user1");

        DirectMessage dm = new DirectMessage();
        dm.setSender(getPrincipal());
        dm.setRecipient(getActor("user2"));
        dm.setSubject("a");
        dm.setBody("a");
        dm.setCreationTime(new Date());
        dm.setAdministrationNotice(true);

        directMessageService.send(dm, new BeanPropertyBindingResult(dm, "directMessage"));
    }

    // Test to move a message between folders.
    @Test
    public void testMove()
    {
        authenticate("user1");

        MessageFolder inbox = messageFolderService.getSystemFolderForActor(getPrincipal(), MessageFolderType.SYSTEM_INBOX);
        MessageFolder sent = messageFolderService.getSystemFolderForActor(getPrincipal(), MessageFolderType.SYSTEM_SENT);

        DirectMessage dm = inbox.getMessages().get(0);
        int id = dm.getId();

        directMessageService.move(dm, sent);

        flushTransaction();

        Assert.assertTrue(directMessageService.getById(id).getMessageFolder().getType().equals(MessageFolderType.SYSTEM_SENT));
    }

    // Test that only the owner can move a message between folders.
    @Test(expected = AccessDeniedException.class)
    public void testMoveFailsIfWrongPrincipal()
    {
        authenticate("user1");

        MessageFolder inbox = messageFolderService.getSystemFolderForActor(getActor("user2"), MessageFolderType.SYSTEM_INBOX);
        MessageFolder sent = messageFolderService.getSystemFolderForActor(getActor("user2"), MessageFolderType.SYSTEM_SENT);

        directMessageService.move(inbox.getMessages().get(0), sent);
    }

    // Test to "delete" a message. (move to trash folder)
    @Test
    public void testDelete()
    {
        authenticate("user1");

        MessageFolder inbox = messageFolderService.getSystemFolderForActor(getPrincipal(), MessageFolderType.SYSTEM_INBOX);
        int id = inbox.getMessages().get(0).getId();
        directMessageService.delete(id);

        flushTransaction();

        Assert.assertTrue(directMessageService.getById(id).getMessageFolder().getType().equals(MessageFolderType.SYSTEM_TRASH));
    }

    // Test that the principal can delete only his messages.
    @Test(expected = AccessDeniedException.class)
    public void testDeleteFailsIfWrongPrincipal()
    {
        authenticate("user1");

        MessageFolder inbox = messageFolderService.getSystemFolderForActor(getActor("user2"), MessageFolderType.SYSTEM_INBOX);
        int id = inbox.getMessages().get(0).getId();
        directMessageService.delete(id);
    }

    // Test to send a message massively to all users.
    @Test
    public void testMassMail()
    {
        authenticate("admin");

        for (MassMailType type : MassMailType.values()) {
            DirectMessage dm = new DirectMessage();
            dm.setSender(getPrincipal());
            dm.setSubject("MASS MAIL TEST 54347374 " + type.name());
            dm.setBody("Test body");
            dm.setCreationTime(new Date());
            dm.setAdministrationNotice(true);

            directMessageService.sendMassMail(type, dm, new BeanPropertyBindingResult(dm, "directMessage"));

            flushTransaction();

            Assert.assertTrue(folderContainsMessage(messageFolderService.getSystemFolderForActor(getPrincipal(), MessageFolderType.SYSTEM_SENT), dm));

            boolean trustedUser = Arrays.asList(MassMailType.TRUSTED_USERS_AND_ADMINISTRATORS, MassMailType.TRUSTED_USERS, MassMailType.ALL_USERS, MassMailType.ALL).contains(type);
            boolean regularUser = Arrays.asList(MassMailType.REGULAR_USERS, MassMailType.ALL_USERS, MassMailType.ALL).contains(type);
            boolean administrator = Arrays.asList(MassMailType.TRUSTED_USERS_AND_ADMINISTRATORS, MassMailType.ADMINISTRATORS, MassMailType.ALL).contains(type);

            Assert.assertTrue(trustedUser == folderContainsMessage(messageFolderService.getSystemFolderForActor(getActor("user1"), MessageFolderType.SYSTEM_INBOX), dm));
            Assert.assertTrue(regularUser == folderContainsMessage(messageFolderService.getSystemFolderForActor(getActor("user2"), MessageFolderType.SYSTEM_INBOX), dm));
            Assert.assertTrue(administrator == folderContainsMessage(messageFolderService.getSystemFolderForActor(getActor("admin2"), MessageFolderType.SYSTEM_INBOX), dm));
        }

    }

    // Test that only admins can do that.
    @Test(expected = AccessDeniedException.class)
    public void testMassMailFailsIfNotAdmin()
    {
        authenticate("user1");

        DirectMessage dm = new DirectMessage();
        dm.setSender(getPrincipal());
        dm.setRecipient(getActor("user2"));
        dm.setSubject("MASS MAIL TEST 543473745");
        dm.setBody("Test body");
        dm.setCreationTime(new Date());
        dm.setAdministrationNotice(true);

        directMessageService.sendMassMail(MassMailType.ALL, dm, new BeanPropertyBindingResult(dm, "directMessage"));
    }
}
