
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Min;

@Entity
@Access(AccessType.PROPERTY)
public class Admin extends Actor {

	private int	salary;


	public Admin() {
		super();
	}

	@Min(900)
	public int getSalary() {
		return this.salary;
	}

	public void setSalary(final int salary) {
		this.salary = salary;
	}

}
