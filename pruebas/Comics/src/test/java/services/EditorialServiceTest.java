
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Editorial;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class EditorialServiceTest {

	@Autowired
	private EditorialService	editorialService;


	@Test
	public void testCreate() {
		final Editorial e = this.editorialService.create();
		Assert.notNull(e);
	}

	@Test
	public void testFindAll() {
		final Editorial e = this.editorialService.create();
		e.setNombre("nombre");
		e.setDescripcion("descripcion");
		Assert.isTrue(!this.editorialService.findAll().contains(e));
		final Editorial saved = this.editorialService.save(e);
		Assert.isTrue(this.editorialService.findAll().contains(saved));
	}

	@Test
	public void testFindOne() {
		final Editorial e = this.editorialService.create();
		e.setNombre("nombre");
		e.setDescripcion("descripcion");
		final Editorial saved = this.editorialService.save(e);
		Assert.isTrue(this.editorialService.findOne(saved.getId()).equals(saved));
	}

	@Test
	public void testDelete() {
		final Editorial e = this.editorialService.create();
		e.setNombre("nombre");
		e.setDescripcion("descripcion");
		final Editorial saved = this.editorialService.save(e);
		Assert.isTrue(this.editorialService.findAll().contains(saved));
		this.editorialService.delete(saved);
		Assert.isTrue(!this.editorialService.findAll().contains(saved));
	}

}
