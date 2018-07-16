
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Min;

@Entity
@Access(AccessType.PROPERTY)
public class Administrador extends Actor {

	private int	salario;


	public Administrador() {
		super();
	}

	@Min(900)
	public int getSalario() {
		return this.salario;
	}

	public void setSalario(final int salario) {
		this.salario = salario;
	}

}
