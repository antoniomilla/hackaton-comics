package services;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.lucene.util.encoding.FourFlagsIntDecoder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import domain.Author;
import domain.Comic;
import domain.Publisher;
import repositories.AuthorRepository;
import repositories.PublisherRepository;
import utilities.AbstractTest;

/**
 * Tests the following use cases from the requirements document:
 * - Create, edit and delete a comic series.
 */
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ComicServiceTest extends AbstractTest {
    @Autowired private ComicService comicService;
    @Autowired private PublisherRepository publisherRepository;
    @Autowired private AuthorRepository authorRepository;

    // Test to create a comic.
    @Test
    public void testCreate() throws Exception
    {
        authenticate("user1");

        Publisher publisher = publisherRepository.findAll().get(0);
        Author author = authorRepository.findAll().get(0);

        Comic comic = new Comic();
        comic.setName("My comic");
        comic.setImage("http://example.com/image.png");
        comic.setTags(Arrays.asList("tag1", "tag2", "tag3"));
        comic.setPublisher(publisher);
        comic.setAuthor(author);

        // Ensure the description field can hold long texts as expected, forgetting the @Lob annotation is a common mistake.
        comic.setDescription("VERY LONG DESCRIPTION " + StringUtils.repeat("A", 1000));
        comic = comicService.create(comic);

        flushTransaction();

        // Ensure the author saved in the database matches the data we entered.
        Comic comic2 = comicService.findById(comic.getId());
        Assert.assertTrue(comic != comic2);
        Assert.assertTrue(comic2.getName().equals("My comic"));
        Assert.assertTrue(comic2.getImage().equals("http://example.com/image.png"));
        Assert.assertTrue(new HashSet<>(comic2.getTags()).equals(new HashSet<>(Arrays.asList("tag1", "tag2", "tag3"))));
        Assert.assertTrue(comic2.getPublisher().equals(publisher));
        Assert.assertTrue(comic2.getAuthor().equals(author));
        Assert.assertTrue(comic2.getDescription().equals("VERY LONG DESCRIPTION " + StringUtils.repeat("A", 1000)));
    }

    // Test to create a comic with a lot of different cases.
    @Test
    public void testCreateBig() throws Exception
    {
        authenticate("user1");

        Publisher publisher = publisherRepository.findAll().get(0);
        Author author = authorRepository.findAll().get(0);

        String longDescription = "VERY LONG DESCRIPTION " + StringUtils.repeat("A", 1000);

        Object[][] data = {
                new Object[] {"My comic", "http://example.com/image.png",  new String[] {"tag1",          "tag2",          "tag3"}, longDescription, publisher, author, null}, // OK
                new Object[] {"",         "http://example.com/image.png",  new String[] {"tag1",          "tag2",          "tag3"}, longDescription, publisher, author, ConstraintViolationException.class}, // BAD: Empty name
                new Object[] {null,       "http://example.com/image.png",  new String[] {"tag1",          "tag2",          "tag3"}, longDescription, publisher, author, ConstraintViolationException.class}, // BAD: Null name
                new Object[] {"My comic", "",                              new String[] {"tag1",          "tag2",          "tag3"}, longDescription, publisher, author, ConstraintViolationException.class}, // BAD: Empty picture URL (must be null if none given)
                new Object[] {"My comic", null,                            new String[] {"tag1",          "tag2",          "tag3"}, longDescription, publisher, author, null}, // OK
                new Object[] {"My comic", "notaurl",                       new String[] {"tag1",          "tag2",          "tag3"}, longDescription, publisher, author, ConstraintViolationException.class}, // BAD: Not a valid URL
                new Object[] {"My comic", "http://example.com/image.png",  new String[] {"tag2",          "tag2",          "tag3"}, longDescription, publisher, author, ConstraintViolationException.class}, // BAD: Duplicate tags
                new Object[] {"My comic", "http://example.com/image.png",  new String[] {"lots of words", "tag2",          "tag3"}, longDescription, publisher, author, ConstraintViolationException.class}, // BAD: Spaces in tags
                new Object[] {"My comic", "http://example.com/image.png",  new String[] {"",              "tag2",          "tag3"}, longDescription, publisher, author, ConstraintViolationException.class}, // BAD: Blank tags
                new Object[] {"My comic", "http://example.com/image.png",  new String[] {null,            "tag2",          "tag3"}, longDescription, publisher, author, ConstraintViolationException.class}, // BAD: Null tags
                new Object[] {"My comic", "https://example.com/image.png", new String[] {                                        }, longDescription, publisher, author, null}, // OK
                new Object[] {"My comic", "https://example.com/image.png", new String[] {                                        }, "",              publisher, author, ConstraintViolationException.class}, // BAD: Blank description (must be null if none given)
                new Object[] {"My comic", "https://example.com/image.png", new String[] {                                        }, null,            publisher, author, null}, // OK
                new Object[] {"My comic", "http://example.com/image.png",  new String[] {"tag1",          "tag2",          "tag3"}, longDescription, null,      author, ConstraintViolationException.class}, // BAD: Publisher required
                new Object[] {"My comic", "http://example.com/image.png",  new String[] {"tag1",          "tag2",          "tag3"}, longDescription, publisher, null,   ConstraintViolationException.class}, // BAD: Author required
        };


        for (int i = 0; i < data.length; i++) {
            Object[] row = data[i];

            Comic comic = new Comic();
            comic.setName((String) row[0]);
            comic.setImage((String) row[1]);

            //noinspection unchecked
            comic.setTags(new ArrayList<>(Arrays.asList((String[]) row[2])));
            comic.setDescription((String) row[3]);
            comic.setPublisher((Publisher) row[4]);
            comic.setAuthor((Author) row[5]);

            boolean ok = false;
            try {
                comic = comicService.create(comic);
                flushTransaction();
                ok = true;
            } catch(Exception caught) {
                Assert.assertTrue("["+i+"] Expecting " + String.valueOf(row[6]) + ", caught " + caught.toString(), caught.getClass().equals(row[6]));
                entityManager.clear();
                flushTransaction();
            }

            if (ok) {
                Assert.assertTrue(row[6] == null);

                // Ensure the author saved in the database matches the data we entered.
                Comic comic2 = comicService.findById(comic.getId());
                Assert.assertTrue(comic != comic2);
                Assert.assertTrue(Objects.equals(comic2.getName(), row[0]));
                Assert.assertTrue(Objects.equals(comic2.getImage(), row[1]));
                Assert.assertTrue(Objects.equals(new HashSet<>(comic2.getTags()), new HashSet<>(Arrays.asList((String[]) row[2]))));
                Assert.assertTrue(Objects.equals(comic2.getDescription(), row[3]));
                Assert.assertTrue(Objects.equals(comic2.getPublisher(), row[4]));
                Assert.assertTrue(Objects.equals(comic2.getAuthor(), row[5]));
            }

            rollbackTransaction();
            startTransaction();
        }
    }

    // Test that it fails if not trusted.
    @Test(expected = AccessDeniedException.class)
    public void testCreateFailsIfNotTrusted() throws Exception
    {
        authenticate("user2"); // user1 is not trusted.

        Publisher publisher = publisherRepository.findAll().get(0);
        Author author = authorRepository.findAll().get(0);

        Comic comic = new Comic();
        comic.setName("My comic");
        comic.setImage("http://example.com/image.png");
        comic.setTags(Arrays.asList("tag1", "tag2", "tag3"));
        comic.setPublisher(publisher);
        comic.setAuthor(author);
        comic.setDescription("VERY LONG DESCRIPTION " + StringUtils.repeat("A", 1000));

        // Attempt to save as non trusted user.
        comic = comicService.create(comic);

        // Shouldn't reach this point.
    }

    // Test that it fails if not authenticated.
    @Test(expected = AccessDeniedException.class)
    public void testCreateFailsIfUnauthenticated() throws Exception
    {
        unauthenticate();

        Publisher publisher = publisherRepository.findAll().get(0);
        Author author = authorRepository.findAll().get(0);

        Comic comic = new Comic();
        comic.setName("My comic");
        comic.setImage("http://example.com/image.png");
        comic.setTags(Arrays.asList("tag1", "tag2", "tag3"));
        comic.setPublisher(publisher);
        comic.setAuthor(author);
        comic.setDescription("VERY LONG DESCRIPTION " + StringUtils.repeat("A", 1000));

        comic = comicService.create(comic);

        flushTransaction();

        // Shouldn't reach this point.
    }

    // Test to update a comic.
    @Test
    public void testUpdate() throws Exception
    {
        authenticate("user1");

        Publisher publisher = publisherRepository.findAll().get(0);
        Author author = authorRepository.findAll().get(0);

        Comic comic = comicService.getByIdForEdit(comicService.findAll().get(0).getId());
        comic.setName("My comic");
        comic.setImage("http://example.com/image.png");
        comic.setTags(new ArrayList<>(Arrays.asList("tag1", "tag2", "tag3")));
        comic.setPublisher(publisher);
        comic.setAuthor(author);

        // Ensure the description field can hold long texts as expected, forgetting the @Lob annotation is a common mistake.
        comic.setDescription("VERY LONG DESCRIPTION " + StringUtils.repeat("A", 1000));
        comic = comicService.update(comic);

        flushTransaction();

        // Ensure the author saved in the database matches the data we entered.
        Comic comic2 = comicService.findById(comic.getId());
        Assert.assertTrue(comic != comic2);
        Assert.assertTrue(comic2.getName().equals("My comic"));
        Assert.assertTrue(comic2.getImage().equals("http://example.com/image.png"));
        Assert.assertTrue(new HashSet<>(comic2.getTags()).equals(new HashSet<>(Arrays.asList("tag1", "tag2", "tag3"))));
        Assert.assertTrue(comic2.getPublisher().equals(publisher));
        Assert.assertTrue(comic2.getAuthor().equals(author));
    }

    // Test that the edit form doesn't work if not trusted.
    @Test(expected = AccessDeniedException.class)
    public void testEditFailsIfNotTrusted()
    {
        authenticate("user2");

        comicService.getByIdForEdit(comicService.findAll().get(0).getId());

        // Shouldn't reach this point
    }

    // Test that commiting an edit fails if not trusted.
    @Test(expected = AccessDeniedException.class)
    public void testUpdateFailsIfNotTrusted() throws Exception
    {
        authenticate("user2");

        Publisher publisher = publisherRepository.findAll().get(0);
        Author author = authorRepository.findAll().get(0);

        Comic comic = comicService.findAll().get(0);
        comic.setName("My comic");
        comic.setImage("http://example.com/image.png");
        comic.setTags(Arrays.asList("tag1", "tag2", "tag3"));
        comic.setPublisher(publisher);
        comic.setAuthor(author);
        comic.setDescription("VERY LONG DESCRIPTION " + StringUtils.repeat("A", 1000));

        comic = comicService.update(comic);

        flushTransaction();

        // Shouldn't reach thi spoint.
    }

    // Test that updating a comic fails if the data is bad.
    @Test(expected = ConstraintViolationException.class)
    public void testUpdateFailsIfBadData() throws Exception
    {
        authenticate("user1");

        Comic comic = comicService.findAll().get(0);
        comic.setImage("badimage");

        comic = comicService.update(comic);

        flushTransaction();

        // Shouldn't reach thi spoint.
    }

    // Test to delete a comic.
    @Test
    public void testDelete()
    {
        authenticate("user1");

        Comic comic = comicService.findAll().get(0);
        int id = comic.getId();

        comicService.delete(id);

        flushTransaction();

        Assert.assertTrue(comicService.findById(id) == null);
    }

    // Test that it fails if not trusted.
    @Test(expected = AccessDeniedException.class)
    public void testDeleteFailsIfNotTrusted()
    {
        authenticate("user2");

        Comic comic = comicService.findAll().get(0);
        int id = comic.getId();

        comicService.delete(id);
    }

    // Test that it fails if not authenticated.
    @Test(expected = AccessDeniedException.class)
    public void testDeleteFailsIfNotAuthenticated()
    {
        unauthenticate();

        Comic comic = comicService.findAll().get(0);
        int id = comic.getId();

        comicService.delete(id);
    }
}
