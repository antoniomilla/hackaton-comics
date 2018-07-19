
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PublisherRepository;
import domain.Publisher;

@Service
@Transactional
public class PublisherService {

	@Autowired
	private PublisherRepository	publisherRepository;


	public PublisherService() {
		super();
	}

	public Publisher create() {
		final Publisher res = new Publisher();

		return res;
	}

	public Collection<Publisher> findAll() {
		final Collection<Publisher> res = this.publisherRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public Publisher findOne(final int Id) {
		final Publisher res = this.publisherRepository.findOne(Id);
		Assert.notNull(res);

		return res;
	}

	public Publisher save(final Publisher publisher) {
		Assert.notNull(publisher);

		final Publisher res = this.publisherRepository.save(publisher);

		return res;
	}

	public void delete(final Publisher publisher) {
		Assert.notNull(publisher);
		Assert.isTrue(publisher.getId() != 0);

		this.publisherRepository.delete(publisher);
	}

}
