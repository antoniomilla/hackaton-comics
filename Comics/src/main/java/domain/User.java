
package domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	private Character				level;
	private Collection<UserComic>	userComics;
	private boolean					blocked;
	private String					blockReason;
	private boolean					trusted;
	private String					description;
	private Date					lastLevelUpdateDate;
	private boolean					onlyFriendsCanSendDms;
	private Collection<Comic>		comicsRead;


	public User() {
		super();

	}

	@Size(min = 1, max = 1)
	public char getLevel() {
		return this.level;
	}

	public void setLevel(final char level) {
		this.level = level;
	}

	@OneToMany(mappedBy = "user")
	public Collection<UserComic> getUserComics() {
		return this.userComics;
	}

	public void setUserComics(final Collection<UserComic> userComics) {
		this.userComics = userComics;
	}

	public void setLevel(final Character level) {
		this.level = level;
	}

	public void setComicsRead(final Collection<Comic> readComics) {
		this.comicsRead = readComics;
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

	public Collection<Comic> getComicsRead() {
		final Collection<Comic> res = new ArrayList<>();
		for (final UserComic c : this.userComics)
			res.add(c.getComic());
		return res;
	}

}
