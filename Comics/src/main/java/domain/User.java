
package domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	private boolean				blocked;
	private String				blockReason;
	private boolean				trusted;
	private String				description;
	private char				level;
	private Date				lastLevelUpdateDate;
	private boolean				onlyFriendsCanSendDms;
	private Collection<Comic>	comicsRead;


	public User() {
		super();
		this.comicsRead = new HashSet<Comic>();

	}

	@Size(min = 1, max = 1)
	public char getLevel() {
		return this.level;
	}

	public void setLevel(final char nivel) {
		this.level = nivel;
	}

	@ManyToMany
	public Collection<Comic> getComicsRead() {
		return this.comicsRead;
	}

	public void setComicsRead(final Collection<Comic> comicsLeidos) {
		this.comicsRead = comicsLeidos;
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

}
