
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class CustomerServiceTest {

	@Autowired
	private UserService	clienteService;


	@Test
	public void testCreate() {
		final User c = this.clienteService.create();
		Assert.notNull(c);
	}

	@Test
	public void testFindAll() {
		final User c = this.clienteService.create();

		c.setNickname("nombre");
		c.setLevel("C");
		Assert.isTrue(!this.clienteService.findAll().contains(c));
		final User saved = this.clienteService.save(c);
		Assert.isTrue(this.clienteService.findAll().contains(saved));
	}
}
