
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	private String						level;
	private Collection<UserComic>		userComics;
	private boolean						blocked;
	private String						blockReason;
	private boolean						trusted;
	private String						description;
	private Date						lastLevelUpdateDate;
	private boolean						onlyFriendsCanSendDms;
	private Collection<User>			friends;
	private Collection<MessageFolder>	messageFolders;
	private Collection<DirectMessage>	sent;
	private Collection<DirectMessage>	receipt;
	private Collection<Comment>			userComments;
	private Collection<Volume>			userVolumes;


	public User() {
		super();

	}

	@Size(min = 1, max = 1)
	public String getLevel() {
		return this.level;
	}

	public void setLevel(final String level) {
		this.level = level;
	}

	@OneToMany(mappedBy = "user")
	public Collection<UserComic> getUserComics() {
		return this.userComics;
	}

	public void setUserComics(final Collection<UserComic> userComics) {
		this.userComics = userComics;
	}

	public boolean getBlocked() {
		return this.blocked;
	}

	public void setBlocked(final boolean blocked) {
		this.blocked = blocked;
	}

	@NotBlank
	public String getBlockReason() {
		return this.blockReason;
	}

	public void setBlockReason(final String blockReason) {
		this.blockReason = blockReason;
	}

	public boolean isTrusted() {
		return this.trusted;
	}

	public void setTrusted(final boolean trusted) {
		this.trusted = trusted;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Past
	public Date getLastLevelUpdateDate() {
		return this.lastLevelUpdateDate;
	}

	public void setLastLevelUpdateDate(final Date lastLevelUpdateDate) {
		this.lastLevelUpdateDate = lastLevelUpdateDate;
	}

	public boolean isOnlyFriendsCanSendDms() {
		return this.onlyFriendsCanSendDms;
	}

	public void setOnlyFriendsCanSendDms(final boolean onlyFriendsCanSendDms) {
		this.onlyFriendsCanSendDms = onlyFriendsCanSendDms;
	}

	/*
	 * public Collection<Comic> getComicsRead() {
	 * final Collection<Comic> res = new ArrayList<>();
	 * for (final UserComic c : this.userComics)
	 * res.add(c.getComic());
	 * return res;
	 * }
	 */

	@ManyToMany
	public Collection<User> getFriends() {
		return this.friends;
	}

	public void setFriends(final Collection<User> friends) {
		this.friends = friends;
	}

	@OneToMany(mappedBy = "owner")
	public Collection<MessageFolder> getMessageFolders() {
		return this.messageFolders;
	}

	public void setMessageFolders(final Collection<MessageFolder> folders) {
		this.messageFolders = folders;
	}

	@OneToMany(mappedBy = "sender")
	public Collection<DirectMessage> getSent() {
		return this.sent;
	}

	public void setSent(final Collection<DirectMessage> sent) {
		this.sent = sent;
	}

	@OneToMany(mappedBy = "recipient")
	public Collection<DirectMessage> getReceipt() {
		return this.receipt;
	}

	public void setReceipt(final Collection<DirectMessage> receipt) {
		this.receipt = receipt;
	}

	@OneToMany(mappedBy = "user")
	public Collection<Comment> getUserComments() {
		return this.userComments;
	}

	public void setUserComments(final Collection<Comment> comments) {
		this.userComments = comments;
	}

	@ManyToMany
	public Collection<Volume> getUserVolumes() {
		return this.userVolumes;
	}

	public void setUserVolumes(final Collection<Volume> userVolumes) {
		this.userVolumes = userVolumes;
	}

}
