
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PublishingCompanyRepository;
import domain.Publisher;

@Service
@Transactional
public class PublishingCompanyService {

	@Autowired
	private PublishingCompanyRepository	publishingCompanyRepository;


	public PublishingCompanyService() {
		super();
	}

	public Publisher create() {
		final Publisher res = new Publisher();

		return res;
	}

	public Collection<Publisher> findAll() {
		final Collection<Publisher> res = this.publishingCompanyRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public Publisher findOne(final int Id) {
		final Publisher res = this.publishingCompanyRepository.findOne(Id);
		Assert.notNull(res);

		return res;
	}

	public Publisher save(final Publisher editorial) {
		Assert.notNull(editorial);

		final Publisher res = this.publishingCompanyRepository.save(editorial);

		return res;
	}

	public void delete(final Publisher editorial) {
		Assert.notNull(editorial);
		Assert.isTrue(editorial.getId() != 0);

		this.publishingCompanyRepository.delete(editorial);
	}

}
