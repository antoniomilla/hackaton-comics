
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.UserRepository;
import security.LoginService;
import security.UserAccount;
import domain.Comic;
import domain.MessageFolder;
import domain.MessageFolderType;
import domain.User;

@Service
@Transactional
public class UserService {

	//Repositorios

	@Autowired
	private UserRepository			userRepository;

	@Autowired
	private ComicService			comicService;

	@Autowired
	private MessageFolderService	messageFolder;


	public UserService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public User create() {
		final User res = new User();
		res.setBlocked(false);
		res.setBlockReason("None");
		res.setLevel("C");
		res.setCreationTime(new Date());
		res.setMessageFolders(this.defaultFolders(res));
		System.out.print(res.getMessageFolders());
		return res;
	}

	public Collection<User> findAll() {
		final Collection<User> res = this.userRepository.findAll();
		Assert.notNull(res);

		return res;
	}
	private Collection<MessageFolder> defaultFolders(final User u) {
		final MessageFolder inbox = this.messageFolder.create(u);
		inbox.setType(MessageFolderType.SYSTEM_INBOX);
		inbox.setName("inbox");
		inbox.setOwner(u);

		final MessageFolder trash = this.messageFolder.create(u);
		trash.setType(MessageFolderType.SYSTEM_TRASH);
		trash.setName("Trash");
		trash.setOwner(u);

		final MessageFolder sent = this.messageFolder.create(u);
		sent.setType(MessageFolderType.SYSTEM_SENT);
		sent.setName("Sent");
		sent.setOwner(u);

		Collection<MessageFolder> folders;
		folders = new ArrayList<MessageFolder>();
		folders.add(inbox);
		folders.add(trash);
		folders.add(sent);
		return folders;

	}
	public User findOne(final int Id) {
		final User res = this.userRepository.findOne(Id);
		Assert.notNull(res);

		return res;
	}

	public User save(final User user) {
		Assert.notNull(user);
		final User res = this.userRepository.save(user);

		return res;
	}

	public void delete(final User user) {
		Assert.notNull(user);
		Assert.isTrue(user.getId() != 0);

		this.userRepository.delete(user);
	}

	public User findByPrincipal() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final User res = this.findByUserAccount(userAccount);
		Assert.notNull(res);
		return res;
	}

	public User findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		final User res = this.userRepository.findByUserAccountId(userAccount.getId());

		return res;
	}

	public void setRead(final int comicId) {
		Assert.isTrue(comicId != 0);

		final User user = this.findByPrincipal();
		Assert.notNull(user);
		final Comic comic = this.comicService.findOne(comicId);
		Assert.notNull(comic);
		//TODO

		this.userRepository.save(user);
	}

	public void setUnread(final int comicId) {
		Assert.isTrue(comicId != 0);
		final User user = this.findByPrincipal();
		Assert.notNull(user);
		final Comic comic = this.comicService.findOne(comicId);
		Assert.notNull(comic);

		//TODO
		this.userRepository.save(user);
	}

	public void friend(final int userId) {
		Assert.isTrue(userId != 0);

		final User user = this.findByPrincipal();
		Assert.notNull(user);
		final User friend = this.userRepository.findOne(userId);
		Assert.notNull(friend);
		user.getFriends().add(friend);

		this.userRepository.save(user);
	}

	public void unfriend(final int userId) {
		Assert.isTrue(userId != 0);

		final User user = this.findByPrincipal();
		Assert.notNull(user);
		final User friend = this.userRepository.findOne(userId);
		Assert.notNull(friend);
		user.getFriends().remove(friend);

		this.userRepository.save(user);
	}
}
