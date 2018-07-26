
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
@Access(AccessType.PROPERTY)
public class DirectMessage extends DomainEntity {

	private String						subject;
	private String						body;
	private Boolean						administrationNotice;
	private Collection<MessageFolder>	messageFolder;
	private Actor						sender;
	private Actor						recipient;


	public DirectMessage() {
		super();
	}
	public String getSubject() {
		return this.subject;
	}
	public void setSubject(final String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return this.body;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	public Boolean getAdministrationNotice() {
		return this.administrationNotice;
	}

	public void setAdministrationNotice(final Boolean administrationNotice) {
		this.administrationNotice = administrationNotice;
	}

	@ManyToOne(optional = false)
	public Actor getSender() {
		return this.sender;
	}

	public void setSender(final Actor sender) {
		this.sender = sender;
	}

	@ManyToOne(optional = false)
	public Actor getRecipient() {
		return this.recipient;
	}

	public void setRecipient(final Actor recipient) {
		this.recipient = recipient;
	}

	@ManyToMany
	public Collection<MessageFolder> getMessageFolder() {
		return this.messageFolder;
	}

	public void setMessageFolder(final Collection<MessageFolder> messageFolder) {
		this.messageFolder = messageFolder;
	}

}
