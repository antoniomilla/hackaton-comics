
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.UserAccountService;
import domain.Cliente;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ClienteServiceTest {

	@Autowired
	private ClienteService		clienteService;
	@Autowired
	private UserAccountService	userAccountService;


	@Test
	public void testCreate() {
		final Cliente c = this.clienteService.create();
		Assert.notNull(c);
	}

	@Test
	public void testFindAll() {
		final Cliente c = this.clienteService.create();

		c.setNombre("nombre");
		c.setDni("12345678K");
		c.setNivel('C');
		Assert.isTrue(!this.clienteService.findAll().contains(c));
		final Cliente saved = this.clienteService.save(c);
		Assert.isTrue(this.clienteService.findAll().contains(saved));
	}
}
