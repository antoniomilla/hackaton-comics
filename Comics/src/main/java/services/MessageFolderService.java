
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MessageFolderRepository;
import domain.Actor;
import domain.DirectMessage;
import domain.MessageFolder;
import domain.User;

@Service
@Transactional
public class MessageFolderService {

	@Autowired
	private MessageFolderRepository	messageFolderRepository;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private DirectMessageService	directMessageService;


	public MessageFolderService() {
		super();
	}

	public MessageFolder create() {
		final MessageFolder res = new MessageFolder();
		final Actor owner = this.actorService.findByPrincipal();
		res.setOwner(owner);
		final Collection<DirectMessage> dms = new ArrayList<DirectMessage>();
		res.setMessages(dms);

		return res;
	}

	public MessageFolder create(final User u) {
		final MessageFolder res = new MessageFolder();
		final Actor owner = u;
		res.setOwner(owner);
		final Collection<DirectMessage> dms = new ArrayList<DirectMessage>();
		res.setMessages(dms);

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
		for (final DirectMessage dm : messageFolder.getMessages())
			this.directMessageService.delete(dm);

		this.messageFolderRepository.delete(messageFolder);
	}

}
