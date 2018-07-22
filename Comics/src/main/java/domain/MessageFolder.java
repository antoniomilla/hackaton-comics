
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class MessageFolder extends DomainEntity {

	private String						name;
	private MessageFolderType			type;
	private String						nameForDisplay;
	private Actor						owner;
	private Collection<DirectMessage>	messages;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public MessageFolderType getType() {
		return this.type;
	}

	public void setType(final MessageFolderType type) {
		this.type = type;
	}
	@NotBlank
	public String getNameForDisplay() {
		return this.nameForDisplay;
	}

	public void setNameForDisplay(final String nameForDisplay) {
		this.nameForDisplay = nameForDisplay;
	}
	@ManyToOne(optional = false)
	public Actor getOwner() {
		return this.owner;
	}

	public void setOwner(final Actor owner) {
		this.owner = owner;
	}
	@OneToMany(mappedBy = "messageFolder")
	public Collection<DirectMessage> getMessages() {
		return this.messages;
	}

	public void setMessages(final Collection<DirectMessage> messages) {
		this.messages = messages;
	}

}