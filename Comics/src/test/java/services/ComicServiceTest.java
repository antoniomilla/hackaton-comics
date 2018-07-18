
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Author;
import domain.Comic;
import domain.Publisher;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ComicServiceTest {

	@Autowired
	private ComicService		comicService;
	/*
	 * @Autowired
	 * private PersonajeService personajeService;
	 */
	@Autowired
	private AutorService		autorService;
	@Autowired
	private PublishingCompanyService	editorialService;


	@Test
	public void testCreate() {
		final Comic c = this.comicService.create();
		Assert.notNull(c);
	}

	@Test
	public void testFindAll() {
		final Comic c = this.comicService.create();
		final Author a = this.autorService.create();
		final Publisher e = this.editorialService.create();
		//final Personaje p = this.personajeService.create();
		a.setName("nombreAutor");
		e.setName("nombreEditorial");
		e.setDescription("descripcionEditorial");
		final Author a2 = this.autorService.save(a);
		final Publisher e2 = this.editorialService.save(e);
		//final Personaje p2 = this.personajeService.save(p);

		c.setName("nombreComic");
		c.setNumPages(150);
		c.setPublishingCompany(e2);
		c.setAutor(a2);
		//this.comicService.getPersonajes().add(p2);

		Assert.isTrue(!this.comicService.findAll().contains(c));
		final Comic saved = this.comicService.save(c);
		Assert.isTrue(this.comicService.findAll().contains(saved));

	}

	@Test
	public void testFindOne() {
		final Comic c = this.comicService.create();
		final Author a = this.autorService.create();
		final Publisher e = this.editorialService.create();
		//final Personaje p = this.personajeService.create();
		a.setName("nombreAutor");
		e.setName("nombreEditorial");
		e.setDescription("descripcionEditorial");
		final Author a2 = this.autorService.save(a);
		final Publisher e2 = this.editorialService.save(e);
		//final Personaje p2 = this.personajeService.save(p);

		c.setName("nombreComic");
		c.setNumPages(150);
		c.setPublishingCompany(e2);
		c.setAutor(a2);
		//this.comicService.getPersonajes().add(p2);

		final Comic saved = this.comicService.save(c);
		Assert.isTrue(this.comicService.findOne(saved.getId()).equals(saved));
	}

	@Test
	public void testDelete() {
		final Comic c = this.comicService.create();
		final Author a = this.autorService.create();
		final Publisher e = this.editorialService.create();
		//final Personaje p = this.personajeService.create();
		a.setName("nombreAutor");
		e.setName("nombreEditorial");
		e.setDescription("descripcionEditorial");
		final Author a2 = this.autorService.save(a);
		final Publisher e2 = this.editorialService.save(e);
		//final Personaje p2 = this.personajeService.save(p);

		c.setName("nombreComic");
		c.setNumPages(150);
		c.setPublishingCompany(e2);
		c.setAutor(a2);
		//this.comicService.getPersonajes().add(p2);

		final Comic saved = this.comicService.save(c);
		Assert.isTrue(this.comicService.findAll().contains(saved));
		this.comicService.delete(saved);
		Assert.isTrue(!this.comicService.findAll().contains(saved));
	}

}
