
package domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import validators.PastOrPresent;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
        @Index(columnList = "messageFolder_id, creationTime"), // DirectMessageRepository.findByMessageFolderOrderByCreationTimeDesc
        @Index(columnList = "sale_id, creationTime"), // DirectMessageRepository.findBySaleAndUsers
})
public class DirectMessage extends DomainEntity {
    private String subject;
    private String body;
    private boolean administrationNotice;
    private MessageFolder messageFolder;
    private Actor sender;
    private Actor recipient;
    private Date creationTime;
    private boolean readByUser;
    private Sale sale;

    public DirectMessage() {}
    public DirectMessage(DirectMessage other)
    {
        this.subject = other.subject;
        this.body = other.body;
        this.administrationNotice = other.administrationNotice;
        this.messageFolder = other.messageFolder;
        this.sender = other.sender;
        this.recipient = other.recipient;
        this.readByUser = other.readByUser;
        this.sale = other.sale;
    }

    @NotBlank
    public String getSubject()
    {
        return this.subject;
    }
    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    @NotBlank
    @Lob
    public String getBody()
    {
        return this.body;
    }
    public void setBody(String body)
    {
        this.body = body;
    }

    public boolean getAdministrationNotice()
    {
        return this.administrationNotice;
    }
    public void setAdministrationNotice(boolean administrationNotice)
    {
        this.administrationNotice = administrationNotice;
    }

    @NotNull
    @PastOrPresent
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    public Date getCreationTime()
    {
        return creationTime;
    }

    public void setCreationTime(Date creationTime)
    {
        this.creationTime = creationTime;
    }

    @NotNull
    @ManyToOne(optional = false)
    public Actor getSender()
    {
        return this.sender;
    }
    public void setSender(Actor sender)
    {
        this.sender = sender;
    }

    @NotNull
    @ManyToOne(optional = false)
    public Actor getRecipient()
    {
        return this.recipient;
    }
    public void setRecipient(Actor recipient)
    {
        this.recipient = recipient;
    }

    @NotNull
    @ManyToOne(optional = false)
    public MessageFolder getMessageFolder()
    {
        return this.messageFolder;
    }
    public void setMessageFolder(MessageFolder messageFolder)
    {
        this.messageFolder = messageFolder;
    }

    public boolean getReadByUser()
    {
        return readByUser;
    }
    public void setReadByUser(boolean readByUser)
    {
        this.readByUser = readByUser;
    }

    @ManyToOne
    public Sale getSale()
    {
        return sale;
    }
    public void setSale(Sale sale)
    {
        this.sale = sale;
    }
}
