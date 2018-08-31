package forms;

import domain.DirectMessage;
import validators.UseConstraintsFrom;

public class MassMailForm {
    MassMailType type;
    String subject;
    String body;
    boolean administrationNotice;

    public MassMailType getType()
    {
        return type;
    }

    public void setType(MassMailType type)
    {
        this.type = type;
    }

    @UseConstraintsFrom(klazz = DirectMessage.class, property = "subject")
    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    @UseConstraintsFrom(klazz = DirectMessage.class, property = "body")
    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    @UseConstraintsFrom(klazz = DirectMessage.class, property = "administrationNotice")
    public boolean getAdministrationNotice()
    {
        return administrationNotice;
    }

    public void setAdministrationNotice(boolean administrationNotice)
    {
        this.administrationNotice = administrationNotice;
    }
}
