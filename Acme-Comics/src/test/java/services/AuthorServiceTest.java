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

import domain.Author;
import sun.security.util.AuthResources;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AuthorServiceTest extends AbstractTest {
    @Autowired private AuthorService authorService;

    @Test
    public void testCreate() throws Exception
    {
        for (String username: Arrays.asList("user1", "admin")) {
            authenticate(username);

            Author author = new Author();
            author.setName("TEST AUTHOR OF " + username);
            author.setImage("http://testimages.com/a.png");
            author.setBirthPlace("America");
            author.setBirthDate(FastDateFormat.getInstance("dd/MM/yyyy").parse("01/01/1970"));

            // Ensure the description field can hold long texts as expected, forgetting the @Lob annotation is a common mistake.
            author.setDescription("VERY LONG DESCRIPTION " + StringUtils.repeat("A", 1000));
            author = authorService.create(author);

            flushTransaction();

            // Ensure the author saved in the database matches the data we entered.
            Author author2 = authorService.findById(author.getId());
            Assert.assertTrue(author != author2);
            Assert.assertTrue(author2.getName().equals("TEST AUTHOR OF " + username));
            Assert.assertTrue(author2.getImage().equals("http://testimages.com/a.png"));
            Assert.assertTrue(author2.getBirthPlace().equals("America"));
            Assert.assertTrue(author2.getBirthDate().equals(FastDateFormat.getInstance("dd/MM/yyyy").parse("01/01/1970")));
            Assert.assertTrue(author2.getDescription().equals("VERY LONG DESCRIPTION " + StringUtils.repeat("A", 1000)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    public void testCreateFailsIfNotTrusted() throws Exception
    {
        authenticate("user2"); // user1 is not trusted.

        Author author = new Author();
        author.setName("TEST NAME");
        author.setImage("http://testimages.com/a.png");
        author.setBirthPlace("America");
        author.setBirthDate(FastDateFormat.getInstance("dd/MM/yyyy").parse("01/01/1970"));
        author.setDescription("TESTDESCRIPTION");

        // Attempt to save as non trusted user.
        authorService.create(author);

        // Shouldn't reach this point.
    }

    @Test(expected = AccessDeniedException.class)
    public void testCreateFailsIfUnauthenticated() throws Exception
    {
        unauthenticate();

        Author author = new Author();

        author.setName("TEST NAME");
        author.setImage("http://testimages.com/a.png");
        author.setBirthPlace("America");
        author.setBirthDate(FastDateFormat.getInstance("dd/MM/yyyy").parse("01/01/1970"));
        author.setDescription("TESTDESCRIPTION");

        authorService.create(author);

        flushTransaction();

        // Shouldn't reach this point.
    }

    @Test
    public void testUpdate() throws Exception
    {
        for (String username: Arrays.asList("user1", "admin")) {
            authenticate(username);

            // Get some existing author for editing.
            Author author = authorService.getByIdForEdit(authorService.findAll().get(0).getId());

            // Edit it.
            author.setName("TEST AUTHOR OF " + username);
            author.setImage("http://testimages.com/a.png");
            author.setBirthPlace("America");
            author.setBirthDate(FastDateFormat.getInstance("dd/MM/yyyy").parse("01/01/1970"));
            author.setDescription("TESTDESCRIPTION");

            // Update it.
            authorService.update(author);

            flushTransaction();

            // Ensure the author saved in the database matches the data we entered.
            Author author2 = authorService.findById(author.getId());
            Assert.assertTrue(author != author2);
            Assert.assertTrue(author2.getName().equals("TEST AUTHOR OF " + username));
            Assert.assertTrue(author2.getImage().equals("http://testimages.com/a.png"));
            Assert.assertTrue(author2.getBirthPlace().equals("America"));
            Assert.assertTrue(author2.getBirthDate().equals(FastDateFormat.getInstance("dd/MM/yyyy").parse("01/01/1970")));
            Assert.assertTrue(author2.getDescription().equals("TESTDESCRIPTION"));
        }
    }

    @Test(expected = AccessDeniedException.class)
    public void testEditFailsIfNotTrusted()
    {
        authenticate("user2");

        // Get some existing author for editing.
        authorService.getByIdForEdit(authorService.findAll().get(0).getId());

        // Shouldn't reach this point.
    }

    @Test(expected = AccessDeniedException.class)
    public void testUpdateFailsIfNotTrusted() throws Exception
    {
        authenticate("user2");

        // Lets pretend the non-trusted user is directly POSTing the update without
        // using the form.
        Author author = authorService.findAll().get(0);

        // Edit it.
        author.setName("TEST USER");
        author.setImage("http://testimages.com/a.png");
        author.setBirthPlace("America");
        author.setBirthDate(FastDateFormat.getInstance("dd/MM/yyyy").parse("01/01/1970"));
        author.setDescription("TESTDESCRIPTION");

        // Update it.
        authorService.update(author);

        // Shouldn't reach this point.
    }

    @Test(expected = ConstraintViolationException.class)
    public void testUpdateFailsIfBadData() throws Exception
    {
        authenticate("user1");

        Author author = authorService.findAll().get(0);

        // Edit it.
        author.setName(""); // Name cannot be blank.

        // Update it.
        authorService.update(author);

        // Hibernate does not validate until flush, so flush.
        flushTransaction();

        // Shouldn't reach this point.
    }

    @Test
    public void testDelete()
    {
        authenticate("user1");

        // Find some author.
        Author author = authorService.findAll().get(0);

        // Delete it.
        int id = author.getId();
        authorService.delete(id);

        flushTransaction();

        // Ensure it's gone.
        Assert.assertTrue(authorService.findById(id) == null);
    }

    @Test(expected = AccessDeniedException.class)
    public void testDeleteFailsIfNotTrusted()
    {
        authenticate("user2");

        // Find some author.
        Author author = authorService.findAll().get(0);

        // Delete it.
        int id = author.getId();
        authorService.delete(id);

        // Shouldn't reach this point
    }

    @Test(expected = AccessDeniedException.class)
    public void testDeleteFailsIfNotAuthenticated()
    {
        unauthenticate();

        // Find some author.
        Author author = authorService.findAll().get(0);

        // Delete it.
        int id = author.getId();
        authorService.delete(id);

        // Shouldn't reach this point
    }
}
