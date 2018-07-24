
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.DirectMessageRepository;
import domain.DirectMessage;

@Service
@Transactional
public class DirectMessageService {

	@Autowired
	private DirectMessageRepository	directMessageRepository;


	public DirectMessageService() {
		super();
	}

	public DirectMessage create() {
		final DirectMessage res = new DirectMessage();

		return res;
	}

	public Collection<DirectMessage> findAll() {
		final Collection<DirectMessage> res = this.directMessageRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public DirectMessage findOne(final int Id) {
		final DirectMessage res = this.directMessageRepository.findOne(Id);
		Assert.notNull(res);

		return res;
	}

	public DirectMessage save(final DirectMessage directMessage) {
		Assert.notNull(directMessage);

		final DirectMessage res = this.directMessageRepository.save(directMessage);

		return res;
	}

	public void delete(final DirectMessage directMessage) {
		Assert.notNull(directMessage);
		Assert.isTrue(directMessage.getId() != 0);

		this.directMessageRepository.delete(directMessage);
	}

}
