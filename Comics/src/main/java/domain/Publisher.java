
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Publisher extends DomainEntity {

	private String				name;
	private Date				foundationDate;
	private String				description;
	private Collection<Comic>	comics;
	private String				image;
	private Collection<Comment>	comments;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@OneToMany(mappedBy = "publisher")
	public Collection<Comic> getComics() {
		return this.comics;
	}

	public void setComics(final Collection<Comic> comics) {
		this.comics = comics;
	}

	@NotBlank
	@URL
	public String getImage() {
		return this.image;
	}

	public void setImage(final String image) {
		this.image = image;
	}

	@OneToMany(mappedBy = "publisher")
	public Collection<Comment> getComments() {
		return this.comments;
	}

	public void setComments(final Collection<Comment> comments) {
		this.comments = comments;
	}

	@Past
	public Date getFoundationDate() {
		return this.foundationDate;
	}

	public void setFoundationDate(final Date foundationDate) {
		this.foundationDate = foundationDate;
	}

	@OneToMany(mappedBy = "publisher")
	public Collection<Comment> getComments() {
		return this.comments;
	}

	public void setComments(final Collection<Comment> comment) {
		this.comments = comment;
	}

}
