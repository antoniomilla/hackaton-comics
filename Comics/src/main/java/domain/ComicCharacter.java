
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	private String							image;
	private String							description;
	private Publisher						publisher;
	private Collection<ComicComicCharacter>	comicComicCharacter;
	private Collection<String>				otherAliases;
	private String							firstAppareance;
	private Collection<Comment>				comments;


	public ComicCharacter() {
		super();
	}

	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	public String getAlias() {
		return this.alias;
	}

	public void setAlias(final String alias) {
		this.alias = alias;
	}

	@NotBlank
	public String getCity() {
		return this.city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	@URL
	public String getImage() {
		return this.image;
	}

	public void setImage(final String image) {
		this.image = image;
	}

	@NotBlank
	public String getFirstAppareance() {
		return this.firstAppareance;
	}

	public void setFirstAppareance(final String firstAppareance) {
		this.firstAppareance = firstAppareance;
	}

	@ElementCollection
	public Collection<String> getOtherAliases() {
		return this.otherAliases;
	}

	public void setOtherAliases(final Collection<String> otherAliases) {
		this.otherAliases = otherAliases;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@OneToMany(mappedBy = "comicCharacter", fetch = FetchType.EAGER)
	public Collection<ComicComicCharacter> getComicComicCharacter() {
		return this.comicComicCharacter;
	}

	public void setComicComicCharacter(final Collection<ComicComicCharacter> comicComicCharacter) {
		this.comicComicCharacter = comicComicCharacter;
	}

	@OneToMany(mappedBy = "comicCharacter")
	public Collection<Comment> getComments() {
		return this.comments;
	}

	public void setComments(final Collection<Comment> comments) {
		this.comments = comments;
	}

	@ManyToOne(optional = false)
	public Publisher getPublisher() {
		return this.publisher;
	}

	public void setPublisher(final Publisher publisher) {
		this.publisher = publisher;
	}

	/*
	 * public Collection<Comic> getAppearsIn() {
	 * final Collection<Comic> res = new ArrayList<>();
	 * for (final ComicComicCharacter c : this.comicComicCharacter)
	 * res.add(c.getComic());
	 * return res;
	 * 
	 * }
	 */
}
