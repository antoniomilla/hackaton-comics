
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
	private ComicCharacterService	personajeService;
	@Autowired
	private PublisherService	editorialService;


	@Test
	public void testCreate() {
		final ComicCharacter p = this.personajeService.create();
		Assert.notNull(p);
	}

	@Test
	public void testFindAll() {
		final ComicCharacter p = this.personajeService.create();
		final Publisher e = this.editorialService.create();
		p.setName("nombre");
		p.setAlias("alias");
		e.setName("nombre");
		e.setDescription("descripcion");
		final Publisher e2 = this.editorialService.save(e);
		p.setPublishingCompany(e2);
		Assert.isTrue(!this.personajeService.findAll().contains(p));
		final ComicCharacter saved = this.personajeService.save(p);
		Assert.isTrue(this.personajeService.findAll().contains(saved));
	}

	@Test
	public void testFindOne() {
		final ComicCharacter p = this.personajeService.create();
		final Publisher e = this.editorialService.create();
		p.setName("nombre");
		p.setAlias("alias");
		e.setName("nombre");
		e.setDescription("descripcion");
		final Publisher e2 = this.editorialService.save(e);
		p.setPublishingCompany(e2);
		final ComicCharacter saved = this.personajeService.save(p);
		Assert.isTrue(this.personajeService.findOne(saved.getId()).equals(saved));
	}

	@Test
	public void testDelete() {
		final ComicCharacter p = this.personajeService.create();
		final Publisher e = this.editorialService.create();
		p.setName("nombre");
		p.setAlias("alias");
		e.setName("nombre");
		e.setDescription("descripcion");
		final Publisher e2 = this.editorialService.save(e);
		p.setPublishingCompany(e2);
		final ComicCharacter saved = this.personajeService.save(p);
		Assert.isTrue(this.personajeService.findAll().contains(saved));
		this.personajeService.delete(saved);
		Assert.isTrue(!this.personajeService.findAll().contains(saved));
	}
}
