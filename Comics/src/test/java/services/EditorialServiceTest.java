
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Publisher;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class EditorialServiceTest {

	@Autowired
	private PublisherService	editorialService;


	@Test
	public void testCreate() {
		final Publisher e = this.editorialService.create();
		Assert.notNull(e);
	}

	@Test
	public void testFindAll() {
		final Publisher e = this.editorialService.create();
		e.setName("nombre");
		e.setDescription("descripcion");
		Assert.isTrue(!this.editorialService.findAll().contains(e));
		final Publisher saved = this.editorialService.save(e);
		Assert.isTrue(this.editorialService.findAll().contains(saved));
	}

	@Test
	public void testFindOne() {
		final Publisher e = this.editorialService.create();
		e.setName("nombre");
		e.setDescription("descripcion");
		final Publisher saved = this.editorialService.save(e);
		Assert.isTrue(this.editorialService.findOne(saved.getId()).equals(saved));
	}

	@Test
	public void testDelete() {
		final Publisher e = this.editorialService.create();
		e.setName("nombre");
		e.setDescription("descripcion");
		final Publisher saved = this.editorialService.save(e);
		Assert.isTrue(this.editorialService.findAll().contains(saved));
		this.editorialService.delete(saved);
		Assert.isTrue(!this.editorialService.findAll().contains(saved));
	}

}
