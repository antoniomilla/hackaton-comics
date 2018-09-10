package services;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import domain.Publisher;
import utilities.AbstractTest;

/**
 * Tests the following use cases from the requirements document:
 * - Create, edit and delete a publisher.
 */
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PublisherServiceTest extends AbstractTest {
    @Autowired private PublisherService publisherService;

    @Test
    public void testCreate() throws Exception
    {
        for (String username: Arrays.asList("user1", "admin")) {
            authenticate(username);

            Publisher publisher = new Publisher();
            publisher.setName("TEST PUBLISHER OF " + username);
            publisher.setImage("http://testimages.com/a.png");
            publisher.setFoundationDate(FastDateFormat.getInstance("dd/MM/yyyy").parse("01/01/1970"));

            // Ensure the description field can hold long texts as expected, forgetting the @Lob annotation is a common mistake.
            publisher.setDescription("VERY LONG DESCRIPTION " + StringUtils.repeat("A", 1000));
            publisher = publisherService.create(publisher);

            flushTransaction();

            // Ensure the publisher saved in the database matches the data we entered.
            Publisher publisher2 = publisherService.findById(publisher.getId());
            Assert.assertTrue(publisher != publisher2);
            Assert.assertTrue(publisher2.getName().equals("TEST PUBLISHER OF " + username));
            Assert.assertTrue(publisher2.getImage().equals("http://testimages.com/a.png"));
            Assert.assertTrue(publisher2.getFoundationDate().equals(FastDateFormat.getInstance("dd/MM/yyyy").parse("01/01/1970")));
            Assert.assertTrue(publisher2.getDescription().equals("VERY LONG DESCRIPTION " + StringUtils.repeat("A", 1000)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    public void testCreateFailsIfNotTrusted() throws Exception
    {
        authenticate("user2"); // user1 is not trusted.

        Publisher publisher = new Publisher();
        publisher.setName("TEST NAME");
        publisher.setImage("http://testimages.com/a.png");
        publisher.setFoundationDate(FastDateFormat.getInstance("dd/MM/yyyy").parse("01/01/1970"));
        publisher.setDescription("TESTDESCRIPTION");

        // Attempt to save as non trusted user.
        publisherService.create(publisher);

        // Shouldn't reach this point.
    }

    @Test(expected = AccessDeniedException.class)
    public void testCreateFailsIfUnauthenticated() throws Exception
    {
        unauthenticate();

        Publisher publisher = new Publisher();

        publisher.setName("TEST NAME");
        publisher.setImage("http://testimages.com/a.png");
        publisher.setFoundationDate(FastDateFormat.getInstance("dd/MM/yyyy").parse("01/01/1970"));
        publisher.setDescription("TESTDESCRIPTION");

        publisherService.create(publisher);

        flushTransaction();

        // Shouldn't reach this point.
    }

    @Test
    public void testUpdate() throws Exception
    {
        for (String username: Arrays.asList("user1", "admin")) {
            authenticate(username);

            // Get some existing publisher for editing.
            Publisher publisher = publisherService.getByIdForEdit(publisherService.findAll().get(0).getId());

            // Edit it.
            publisher.setName("TEST PUBLISHER OF " + username);
            publisher.setImage("http://testimages.com/a.png");
            publisher.setFoundationDate(FastDateFormat.getInstance("dd/MM/yyyy").parse("01/01/1970"));
            publisher.setDescription("TESTDESCRIPTION");

            // Update it.
            publisherService.update(publisher);

            flushTransaction();

            // Ensure the publisher saved in the database matches the data we entered.
            Publisher publisher2 = publisherService.findById(publisher.getId());
            Assert.assertTrue(publisher != publisher2);
            Assert.assertTrue(publisher2.getName().equals("TEST PUBLISHER OF " + username));
            Assert.assertTrue(publisher2.getImage().equals("http://testimages.com/a.png"));
            Assert.assertTrue(publisher2.getFoundationDate().equals(FastDateFormat.getInstance("dd/MM/yyyy").parse("01/01/1970")));
            Assert.assertTrue(publisher2.getDescription().equals("TESTDESCRIPTION"));
        }
    }

    @Test(expected = AccessDeniedException.class)
    public void testEditFailsIfNotTrusted()
    {
        authenticate("user2");

        // Get some existing publisher for editing.
        publisherService.getByIdForEdit(publisherService.findAll().get(0).getId());

        // Shouldn't reach this point.
    }

    @Test(expected = AccessDeniedException.class)
    public void testUpdateFailsIfNotTrusted() throws Exception
    {
        authenticate("user2");

        // Lets pretend the non-trusted user is directly POSTing the update without
        // using the form.
        Publisher publisher = publisherService.findAll().get(0);

        // Edit it.
        publisher.setName("TEST USER");
        publisher.setImage("http://testimages.com/a.png");
        publisher.setFoundationDate(FastDateFormat.getInstance("dd/MM/yyyy").parse("01/01/1970"));
        publisher.setDescription("TESTDESCRIPTION");

        // Update it.
        publisherService.update(publisher);

        // Shouldn't reach this point.
    }

    @Test(expected = ConstraintViolationException.class)
    public void testUpdateFailsIfBadData() throws Exception
    {
        authenticate("user1");

        Publisher publisher = publisherService.findAll().get(0);

        // Edit it.
        publisher.setName(""); // Name cannot be blank.

        // Update it.
        publisherService.update(publisher);

        // Hibernate does not validate until flush, so flush.
        flushTransaction();

        // Shouldn't reach this point.
    }

    @Test
    public void testDelete()
    {
        authenticate("user1");

        // Find some publisher.
        Publisher publisher = publisherService.findAll().get(0);

        // Delete it.
        int id = publisher.getId();
        publisherService.delete(id);

        flushTransaction();

        // Ensure it's gone.
        Assert.assertTrue(publisherService.findById(id) == null);
    }

    @Test(expected = AccessDeniedException.class)
    public void testDeleteFailsIfNotTrusted()
    {
        authenticate("user2");

        // Find some publisher.
        Publisher publisher = publisherService.findAll().get(0);

        // Delete it.
        int id = publisher.getId();
        publisherService.delete(id);

        // Shouldn't reach this point
    }

    @Test(expected = AccessDeniedException.class)
    public void testDeleteFailsIfNotAuthenticated()
    {
        unauthenticate();

        // Find some publisher.
        Publisher publisher = publisherService.findAll().get(0);

        // Delete it.
        int id = publisher.getId();
        publisherService.delete(id);

        // Shouldn't reach this point
    }
}
