
package domain;

import org.hibernate.search.annotations.Indexed;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@Access(AccessType.PROPERTY)
@Indexed
public class Administrator extends Actor {

	public Administrator() {
		super();
	}

	@Transient
	@Override
	public boolean isAdministrator()
	{
		return true;
	}
}
