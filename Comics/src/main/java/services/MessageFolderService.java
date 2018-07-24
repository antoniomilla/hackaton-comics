
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MessageFolderRepository;
import domain.MessageFolder;

@Service
@Transactional
public class MessageFolderService {

	@Autowired
	private MessageFolderRepository	messageFolderRepository;


	public MessageFolderService() {
		super();
	}

	public MessageFolder create() {
		final MessageFolder res = new MessageFolder();

		return res;
	}

	public Collection<MessageFolder> findAll() {
		final Collection<MessageFolder> res = this.messageFolderRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public MessageFolder findOne(final int Id) {
		final MessageFolder res = this.messageFolderRepository.findOne(Id);
		Assert.notNull(res);

		return res;
	}

	public MessageFolder save(final MessageFolder messageFolder) {
		Assert.notNull(messageFolder);

		final MessageFolder res = this.messageFolderRepository.save(messageFolder);

		return res;
	}

	public void delete(final MessageFolder messageFolder) {
		Assert.notNull(messageFolder);
		Assert.isTrue(messageFolder.getId() != 0);

		this.messageFolderRepository.delete(messageFolder);
	}

}
