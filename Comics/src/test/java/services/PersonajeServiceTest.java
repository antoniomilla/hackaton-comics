
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.ComicCharacter;
import domain.Publisher;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class PersonajeServiceTest {

	@Autowired
	private ComicCharacterService	comicCharacterService;
	@Autowired
	private PublisherService		publisherService;


	@Test
	public void testCreate() {
		final ComicCharacter p = this.comicCharacterService.create();
		Assert.notNull(p);
	}

	@Test
	public void testFindAll() {
		final ComicCharacter p = this.comicCharacterService.create();
		final Publisher e = this.publisherService.create();
		p.setName("nombre");
		p.setAlias("alias");
		e.setName("nombre");
		e.setDescription("descripcion");
		final Publisher e2 = this.publisherService.save(e);
		p.setPublisher(e2);
		Assert.isTrue(!this.comicCharacterService.findAll().contains(p));
		final ComicCharacter saved = this.comicCharacterService.save(p);
		Assert.isTrue(this.comicCharacterService.findAll().contains(saved));
	}

	@Test
	public void testFindOne() {
		final ComicCharacter p = this.comicCharacterService.create();
		final Publisher e = this.publisherService.create();
		p.setName("nombre");
		p.setAlias("alias");
		e.setName("nombre");
		e.setDescription("descripcion");
		final Publisher e2 = this.publisherService.save(e);
		p.setPublisher(e2);
		final ComicCharacter saved = this.comicCharacterService.save(p);
		Assert.isTrue(this.comicCharacterService.findOne(saved.getId()).equals(saved));
	}

	@Test
	public void testDelete() {
		final ComicCharacter p = this.comicCharacterService.create();
		final Publisher e = this.publisherService.create();
		p.setName("nombre");
		p.setAlias("alias");
		e.setName("nombre");
		e.setDescription("descripcion");
		final Publisher e2 = this.publisherService.save(e);
		p.setPublisher(e2);
		final ComicCharacter saved = this.comicCharacterService.save(p);
		Assert.isTrue(this.comicCharacterService.findAll().contains(saved));
		this.comicCharacterService.delete(saved);
		Assert.isTrue(!this.comicCharacterService.findAll().contains(saved));
	}
}
