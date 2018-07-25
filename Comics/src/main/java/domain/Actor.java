
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import security.UserAccount;

@Entity
@Access(AccessType.PROPERTY)
public abstract class Actor extends DomainEntity {

	// Constructors -----------------------------------------------------------

	public Actor() {
		super();
	}


	// Attributes -------------------------------------------------------------

	private String						nickname;
	private Date						creationTime;
	private UserAccount					userAccount;
	private Collection<MessageFolder>	messageFolders;
	private Collection<DirectMessage>	sent;
	private Collection<DirectMessage>	receipt;


	@NotBlank
	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(final String nickname) {
		this.nickname = nickname;
	}

	@NotNull
	@Valid
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(final UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	@Past
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	public Date getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(final Date creationTime) {
		this.creationTime = creationTime;
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

}
