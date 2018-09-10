package services;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Objects;

import javax.transaction.Transactional;
import javax.validation.Constraint;
import javax.validation.ConstraintViolationException;

import domain.Actor;
import domain.MessageFolderType;
import domain.User;
import exceptions.ResourceNotUniqueException;
import utilities.AbstractTest;

/**
 * Tests the following use cases from the requirements document:
 * - Register to the system as an user.
 * - Change his user settings.
 * - Change his own account and profile information.
 * - Mark or unmark other users as friends. Friends are always allowed to send messages to the user.
 * - Set or unset an user’s trusted status.
 * - Block an user, optionally specifying a reason.
 */
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UserServiceTest extends AbstractTest {
    @Autowired private ActorService actorService;
    @Autowired private UserService userService;
    @Autowired private MessageFolderService messageFolderService;

    @Test
    public void testCreate() throws ResourceNotUniqueException
    {
        unauthenticate();
        Assert.assertTrue(actorService.findByUsername("test1") == null);
        userService.create("test1", "test1", "Test User");
        flushTransaction();

        User user = (User) actorService.findByUsername("test1");
        Assert.assertTrue(user != null);

        Assert.assertTrue(Objects.equals(user.getNickname(), "Test User"));
        Assert.assertTrue(Objects.equals(user.getLevel(), "C"));
        Assert.assertTrue(messageFolderService.getSystemFolderForActor(user, MessageFolderType.SYSTEM_INBOX) != null);
        Assert.assertTrue(messageFolderService.getSystemFolderForActor(user, MessageFolderType.SYSTEM_SENT) != null);
        Assert.assertTrue(messageFolderService.getSystemFolderForActor(user, MessageFolderType.SYSTEM_TRASH) != null);
    }

    @Test(expected = ResourceNotUniqueException.class)
    public void testCreateFailsIfNotUnique() throws ResourceNotUniqueException
    {
        unauthenticate();
        userService.create("user1", "test1", "Test User");
        flushTransaction();
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCreateFailsIfBadData1() throws ResourceNotUniqueException
    {
        unauthenticate();
        userService.create(null, "test1", "Test User");
        flushTransaction();
    }
    @Test(expected = ConstraintViolationException.class)
    public void testCreateFailsIfBadData2() throws ResourceNotUniqueException
    {
        unauthenticate();
        userService.create("test1", null, "Test User");
        flushTransaction();
    }
    @Test(expected = ConstraintViolationException.class)
    public void testCreateFailsIfBadData3() throws ResourceNotUniqueException
    {
        unauthenticate();
        userService.create("test1", "test1", null);
        flushTransaction();
    }

    @Test
    public void testUpdate()
    {
        authenticate("user1");

        User user = (User) getPrincipal();
        user.setNickname("MY NICKNAME");
        user.setDescription("MY DESCRIPTION" + StringUtils.repeat("A", 1000));
        user.setOnlyFriendsCanSendDms(true);
        user = userService.update(user);

        flushTransaction();
        user = (User) getPrincipal();

        Assert.assertTrue(Objects.equals(user.getNickname(), "MY NICKNAME"));
        Assert.assertTrue(Objects.equals(user.getDescription(), "MY DESCRIPTION" + StringUtils.repeat("A", 1000)));
        Assert.assertTrue(Objects.equals(user.getOnlyFriendsCanSendDms(), true));
    }

    @Test(expected = AccessDeniedException.class)
    public void testUpdateFailsIfNotPrincipal()
    {
        authenticate("user2");

        User user = (User) getActor("user1");
        user = userService.update(user);
    }

    @Test
    public void testTrust()
    {
        authenticate("admin");
        User user = (User) getActor("user1");
        
        userService.trust(user);
        flushTransaction();
        user = (User) getActor("user1");
        Assert.assertTrue(Objects.equals(user.getTrusted(), true));

        userService.untrust(user);
        flushTransaction();
        user = (User) getActor("user1");
        Assert.assertTrue(Objects.equals(user.getTrusted(), false));

        userService.trust(user);
        flushTransaction();
        user = (User) getActor("user1");
        Assert.assertTrue(Objects.equals(user.getTrusted(), true));
    }

    @Test(expected = AccessDeniedException.class)
    public void testTrustFailsIfNotAdmin()
    {
        authenticate("user1");
        User user = (User) getActor("user1");

        userService.trust(user);
    }

    @Test
    public void testBlock()
    {
        authenticate("admin");
        User user = (User) getActor("user1");

        userService.block(user, "YOU ARE BLOCKED FOR TEST");
        flushTransaction();
        user = (User) getActor("user1");
        Assert.assertTrue(Objects.equals(user.getBlocked(), true));
        Assert.assertTrue(Objects.equals(user.getBlockReason(), "YOU ARE BLOCKED FOR TEST"));

        userService.unblock(user);
        flushTransaction();
        user = (User) getActor("user1");
        Assert.assertTrue(Objects.equals(user.getBlocked(), false));

        userService.block(user, null);
        flushTransaction();
        user = (User) getActor("user1");
        Assert.assertTrue(Objects.equals(user.getBlocked(), true));
        Assert.assertTrue(Objects.equals(user.getBlockReason(), null));
    }

    @Test
    public void testFriend()
    {
        authenticate("user1");

        userService.friend((User) getActor("user2"));
        flushTransaction();
        Assert.assertTrue(Objects.equals(((User) getPrincipal()).getFriends().contains(getActor("user2")), true));
        userService.unfriend((User) getActor("user2"));
        flushTransaction();
        Assert.assertTrue(Objects.equals(((User) getPrincipal()).getFriends().contains(getActor("user2")), true));
        userService.friend((User) getActor("user2"));
        flushTransaction();
        Assert.assertTrue(Objects.equals(((User) getPrincipal()).getFriends().contains(getActor("user2")), true));
    }
}
