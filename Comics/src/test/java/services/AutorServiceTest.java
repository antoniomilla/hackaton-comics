
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Author;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class AutorServiceTest {

	@Autowired
	private AuthorService	autorService;


	@Test
	public void testCreate() {
		final Author a = this.autorService.create();
		Assert.notNull(a);
	}

	@Test
	public void testFindAll() {
		final Author a = this.autorService.create();
		a.setName("nombre");
		Assert.isTrue(!this.autorService.findAll().contains(a));
		final Author saved = this.autorService.save(a);
		Assert.isTrue(this.autorService.findAll().contains(saved));
	}

	@Test
	public void testFindOne() {
		final Author a = this.autorService.create();
		a.setName("nombre");
		final Author saved = this.autorService.save(a);
		Assert.isTrue(this.autorService.findOne(saved.getId()).equals(saved));
	}

	@Test
	public void testDelete() {
		final Author a = this.autorService.create();
		a.setName("nombre");
		final Author saved = this.autorService.save(a);
		Assert.isTrue(this.autorService.findAll().contains(saved));
		this.autorService.delete(saved);
		Assert.isTrue(!this.autorService.findAll().contains(saved));
	}

}
