
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class ComicCharacter extends DomainEntity {

	private String							name;
	private String							alias;
	private String							city;
	private Publisher						publishingCompany;
	private String							image;
	private Collection<ComicComicCharacter>	comicComicCharacter;


	public ComicCharacter() {
		super();
	}

	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String nombre) {
		this.name = nombre;
	}

	@NotBlank
	public String getAlias() {
		return this.alias;
	}

	public void setAlias(final String alias) {
		this.alias = alias;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	@ManyToOne(optional = false)
	public Publisher getPublishingCompany() {
		return this.publishingCompany;
	}

	public void setPublishingCompany(final Publisher editorial) {
		this.publishingCompany = editorial;
	}

	@URL
	public String getImage() {
		return this.image;
	}

	public void setImage(final String imagen) {
		this.image = imagen;
	}

	@OneToMany(mappedBy = "comicCharacter")
	public Collection<ComicComicCharacter> getComicComicCharacter() {
		return this.comicComicCharacter;
	}

	public void setComicComicCharacter(final Collection<ComicComicCharacter> comicComicCharacter) {
		this.comicComicCharacter = comicComicCharacter;
	}

}
