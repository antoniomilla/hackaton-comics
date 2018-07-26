
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.DirectMessageRepository;
import domain.Actor;
import domain.DirectMessage;
import domain.MessageFolder;
import domain.MessageFolderType;

@Service
@Transactional
public class DirectMessageService {

	@Autowired
	private DirectMessageRepository	directMessageRepository;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private MessageFolderService	messageFolderService;


	public DirectMessageService() {
		super();
	}

	public DirectMessage create() {
		final DirectMessage res = new DirectMessage();
		final Actor sender = this.actorService.findByPrincipal();
		res.setSender(sender);
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

		final MessageFolder mfi = this.getInbox(directMessage.getRecipient());
		final MessageFolder mfs = this.getSent(directMessage.getSender());
		final Collection<MessageFolder> folders = new ArrayList<MessageFolder>();
		folders.add(mfi);
		folders.add(mfs);
		directMessage.setMessageFolder(folders);

		final DirectMessage res = this.directMessageRepository.save(directMessage);

		mfs.getMessages().add(res);
		mfi.getMessages().add(res);
		this.messageFolderService.save(mfi);

		return res;
	}

	private MessageFolder getInbox(final Actor a) {
		MessageFolder res = null;
		for (final MessageFolder mf : a.getMessageFolders())
			if (mf.getType().equals(MessageFolderType.SYSTEM_INBOX)) {
				res = mf;
				break;
			}
		return res;
	}

	private MessageFolder getSent(final Actor a) {
		MessageFolder res = null;
		for (final MessageFolder mf : a.getMessageFolders())
			if (mf.getType().equals(MessageFolderType.SYSTEM_SENT)) {
				res = mf;
				break;
			}
		return res;
	}

	public void delete(final DirectMessage directMessage) {
		Assert.notNull(directMessage);
		Assert.isTrue(directMessage.getId() != 0);

		this.directMessageRepository.delete(directMessage);
	}

}
