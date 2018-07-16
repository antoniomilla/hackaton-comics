
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Editorial;
import domain.Personaje;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class PersonajeServiceTest {

	@Autowired
	private PersonajeService	personajeService;
	@Autowired
	private EditorialService	editorialService;


	@Test
	public void testCreate() {
		final Personaje p = this.personajeService.create();
		Assert.notNull(p);
	}

	@Test
	public void testFindAll() {
		final Personaje p = this.personajeService.create();
		final Editorial e = this.editorialService.create();
		p.setNombre("nombre");
		p.setAlias("alias");
		e.setNombre("nombre");
		e.setDescripcion("descripcion");
		final Editorial e2 = this.editorialService.save(e);
		p.setEditorial(e2);
		Assert.isTrue(!this.personajeService.findAll().contains(p));
		final Personaje saved = this.personajeService.save(p);
		Assert.isTrue(this.personajeService.findAll().contains(saved));
	}

	@Test
	public void testFindOne() {
		final Personaje p = this.personajeService.create();
		final Editorial e = this.editorialService.create();
		p.setNombre("nombre");
		p.setAlias("alias");
		e.setNombre("nombre");
		e.setDescripcion("descripcion");
		final Editorial e2 = this.editorialService.save(e);
		p.setEditorial(e2);
		final Personaje saved = this.personajeService.save(p);
		Assert.isTrue(this.personajeService.findOne(saved.getId()).equals(saved));
	}

	@Test
	public void testDelete() {
		final Personaje p = this.personajeService.create();
		final Editorial e = this.editorialService.create();
		p.setNombre("nombre");
		p.setAlias("alias");
		e.setNombre("nombre");
		e.setDescripcion("descripcion");
		final Editorial e2 = this.editorialService.save(e);
		p.setEditorial(e2);
		final Personaje saved = this.personajeService.save(p);
		Assert.isTrue(this.personajeService.findAll().contains(saved));
		this.personajeService.delete(saved);
		Assert.isTrue(!this.personajeService.findAll().contains(saved));
	}
}
