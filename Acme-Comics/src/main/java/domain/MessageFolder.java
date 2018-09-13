
package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import validators.CustomValidator;
import validators.HasCustomValidators;
import validators.NullOrNotBlank;

@Entity
@Access(AccessType.PROPERTY)
@HasCustomValidators
@Table(indexes = {
        @Index(columnList = "actor_id, type") // MessageFolderRepository.findSystemFolderForActor
})
public class MessageFolder extends DomainEntity {
    private String name;
    private MessageFolderType type;
    private Actor actor;
    private List<DirectMessage> messages = new ArrayList<>();

    private String nameForDisplay;

    public MessageFolder() {}
    public MessageFolder(Actor actor)
    {
        this.type = MessageFolderType.USER;
        this.actor = actor;
    }

    @NullOrNotBlank
    public String getName()
    {
        return this.name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    @Enumerated(EnumType.STRING)
    @NotNull
    public MessageFolderType getType()
    {
        return this.type;
    }
    public void setType(MessageFolderType type)
    {
        this.type = type;
    }

    @NotNull
    @ManyToOne(optional = false)
    public Actor getActor()
    {
        return this.actor;
    }
    public void setActor(Actor actor)
    {
        this.actor = actor;
    }

    @NotNull
    @OneToMany(mappedBy = "messageFolder")
    public List<DirectMessage> getMessages()
    {
        return this.messages;
    }
    public void setMessages(List<DirectMessage> messages)
    {
        this.messages = messages;
    }

    @Transient
    public String getNameForDisplay()
    {
        return nameForDisplay;
    }
    public void setNameForDisplay(String nameForDisplay)
    {
        this.nameForDisplay = nameForDisplay;
    }

    @CustomValidator(applyOn = "name", message = "{org.hibernate.validator.constraints.NotBlank.message}")
    @Transient
    public boolean isValid()
    {
        return (getName() != null) == (getType() == MessageFolderType.USER);
    }
}
