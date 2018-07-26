
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	private String					level;
	private Collection<UserComic>	userComics;
	private boolean					blocked;
	private String					blockReason;
	private boolean					trusted;
	private String					description;
	private Date					lastLevelUpdateDate;
	private boolean					onlyFriendsCanSendDms;
	private Collection<User>		friends;
	private Collection<Comment>		userComments;
	private Collection<Volume>		userVolumes;


	public User() {
		super();

	}

	@Size(min = 1, max = 1)
	public String getLevel() {
		if (this.userComics.size() < 2)
			this.level = "C";
		else if (this.userComics.size() >= 2 && this.userComics.size() < 5)
			this.level = "B";
		else if (this.userComics.size() >= 5 && this.userComics.size() < 10)
			this.level = "A";
		else
			this.level = "S";
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
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
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
