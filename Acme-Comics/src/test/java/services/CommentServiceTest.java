package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

import javax.validation.ConstraintViolationException;

import domain.Author;
import domain.Comic;
import domain.Comment;
import domain.User;
import repositories.AuthorRepository;
import repositories.ComicRepository;
import utilities.AbstractTest;

/**
 * Tests the following use cases from the requirements document:
 * - Add comments in Comics, Volumes, Publishers, Actors, Characters and Sales.
 * - Delete any comments.
 */
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CommentServiceTest extends AbstractTest {
    @Autowired CommentService commentService;
    @Autowired ComicRepository comicRepository;
    @Autowired private AuthorRepository authorRepository;

    // Test to create a comment.
    @Test
    public void testCreate()
    {
        authenticate("user1");

        Comic comic = comicRepository.findAll().get(0);

        Comment comment = new Comment();
        comment.setUser((User) getPrincipal());
        comment.setComic(comic);
        comment.setText("TEST COMMENT 34273849327");

        comment = commentService.create(comment);

        flushTransaction();

        comic = comicRepository.findOne(comic.getId());

        Assert.isTrue(comic.getComments().contains(comment));
    }

    // Test that it fails if not authenticated.
    @Test(expected = AccessDeniedException.class)
    public void testCreateFailsIfNotAuthenticated()
    {
        unauthenticate();

        Comic comic = comicRepository.findAll().get(0);

        Comment comment = new Comment();
        comment.setUser((User) getActor("user1"));
        comment.setComic(comic);
        comment.setText("TEST COMMENT 34273849327");

        commentService.create(comment);

        flushTransaction();
    }

    // Test that it fails if the user on the comment is not the principal.
    @Test(expected = AccessDeniedException.class)
    public void testCreateFailsIfWrongPrincipal()
    {
        authenticate("user1");

        Comic comic = comicRepository.findAll().get(0);

        Comment comment = new Comment();
        comment.setUser((User) getActor("user2"));
        comment.setComic(comic);
        comment.setText("TEST COMMENT 34273849327");

        commentService.create(comment);

        flushTransaction();
    }

    // Test that it fails if not properly associated (1).
    @Test(expected = ConstraintViolationException.class)
    public void testCreateFailsIfNotProperlyAssociated1()
    {
        authenticate("user1");

        Comic comic = comicRepository.findAll().get(0);
        Author author = authorRepository.findAll().get(0);

        Comment comment = new Comment();
        comment.setUser((User) getPrincipal());
        comment.setComic(comic);
        comment.setAuthor(author);
        comment.setText("TEST COMMENT 34273849327");

        commentService.create(comment);

        flushTransaction();
    }

    // Test that it fails if not properly associated (2).
    @Test(expected = ConstraintViolationException.class)
    public void testCreateFailsIfNotProperlyAssociated2()
    {
        authenticate("user1");

        Comment comment = new Comment();
        comment.setUser((User) getPrincipal());
        comment.setText("TEST COMMENT 34273849327");

        commentService.create(comment);

        flushTransaction();
    }

    // Test to delete a comment.
    @Test
    public void testDelete()
    {
        authenticate("admin");

        Author author = authorRepository.findByName("Alan Moore");
        Comment comment = author.getComments().get(0);

        commentService.delete(comment.getId());

        flushTransaction();

        author = authorRepository.findOne(author.getId());

        Assert.isTrue(!author.getComments().contains(comment));
    }

    // Test that it fails if not admin.
    @Test(expected = AccessDeniedException.class)
    public void testDeleteFailsIfNotAdmin()
    {
        authenticate("user1");

        Author author = authorRepository.findByName("Alan Moore");
        Comment comment = author.getComments().get(0);

        commentService.delete(comment.getId());
    }

    // Test that it fails if not authenticated.
    @Test(expected = AccessDeniedException.class)
    public void testDeleteFailsIfNotAuthenticated()
    {
        unauthenticate();

        Author author = authorRepository.findByName("Alan Moore");
        Comment comment = author.getComments().get(0);

        commentService.delete(comment.getId());
    }
}
