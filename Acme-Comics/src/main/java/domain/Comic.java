
package domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.bridge.builtin.impl.BuiltinIterableBridge;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import cz.jirutka.validator.collection.constraints.EachNotNull;
import cz.jirutka.validator.collection.constraints.EachPattern;
import validators.CustomValidator;
import validators.HasCustomValidators;
import validators.NullOrNotBlank;

@Entity
@Access(AccessType.PROPERTY)
@Indexed
@HasCustomValidators
public class Comic extends DomainEntity {

	private String							name;
	private Publisher						publisher;
	private Author							author;
	private String							image;
	private List<ComicComicCharacter>       comicComicCharacters = new ArrayList<>();
	private List<UserComic>			        userComics = new ArrayList<>();
	private String							description;
	private List<String>				tags = new ArrayList<>();
	private List<Volume>				volumes = new ArrayList<>();
	private int volumeCount;
	private List<Comment>				comments = new ArrayList<>();
	private List<Sale> sales = new ArrayList<>();

	@NotBlank
	@Field
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotNull
	@ManyToOne(optional = false)
	public Publisher getPublisher() {
		return this.publisher;
	}
	public void setPublisher(final Publisher publisher) {
		this.publisher = publisher;
	}

	@Transient
	@Field
	public String getPublisherName()
	{
		return getPublisher().getName();
	}


	@NotNull
	@ManyToOne(optional = false)
	public Author getAuthor() {
		return this.author;
	}
	public void setAuthor(final Author author) {
		this.author = author;
	}

	@Transient
	@Field
	public String getAuthorName()
	{
		return getAuthor().getName();
	}

	@Transient
	@Field
	@FieldBridge(impl = BuiltinIterableBridge.class)
	public List<String> getCharacterMainAliases()
	{
		List<String> result = new ArrayList<>();
		for (ComicComicCharacter comicComicCharacter : comicComicCharacters) {
			result.add(comicComicCharacter.getComicCharacter().getAlias());
		}
		return result;
	}

	@URL
	@NullOrNotBlank
	public String getImage() {
		return this.image;
	}

	public void setImage(final String image) {
		this.image = image;
	}

	@NullOrNotBlank
	@Field
	@Lob
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Valid
	@EachPattern(regexp = "[^\\s]+")
	@EachNotNull
	@ElementCollection
	@Field
	@FieldBridge(impl = BuiltinIterableBridge.class)
	public List<String> getTags() {
		return this.tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	@CustomValidator(message = "{comics.error.duplicateTags}", applyOn = {"tags"})
	@Transient
	public boolean isValidTagsValid()
	{
		// Ensure that there are no duplicate tags.
		// This is not done directly by using a Set<> because that complicates
		// binding the list to the HTTP request (i.e. tags[0]=XXXX doesn't work because
		// Sets don't have indexes).
		Set<String> tagsWithoutDuplicates = new HashSet<>();
		for (String s : getTags()) {
			if (s == null) return true; // Tags cannot be null, ignore this validator then.
			tagsWithoutDuplicates.add(s.toUpperCase());
		}
		return tagsWithoutDuplicates.size() == tags.size();
	}

	@NotNull
	@OneToMany(mappedBy = "comic")
	@Cascade(CascadeType.DELETE)
	public List<ComicComicCharacter> getComicComicCharacters() {
		return this.comicComicCharacters;
	}

	public void setComicComicCharacters(List<ComicComicCharacter> comicComicCharacters) {
		this.comicComicCharacters = comicComicCharacters;
	}

	@NotNull
	@OneToMany(mappedBy = "comic")
	@Cascade(CascadeType.DELETE)
	public List<UserComic> getUserComics() {
		return this.userComics;
	}

	public void setUserComics(List<UserComic> userComics) {
		this.userComics = userComics;
	}

	@NotNull
	@OneToMany(mappedBy = "comic")
	@Cascade(CascadeType.DELETE)
	public List<Volume> getVolumes() {
		return this.volumes;
	}
	public void setVolumes(List<Volume> volumes) {
		this.volumes = volumes;
	}

	@NotNull
	@OneToMany(mappedBy = "comic")
	@Cascade(CascadeType.DELETE)
	public List<Comment> getComments() {
		return this.comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	@NotNull
	@OneToMany(mappedBy = "comic")
	public List<Sale> getSales() {
		return this.sales;
	}
	public void setSales(List<Sale> sales) {
		this.sales = sales;
	}

	@PreRemove
	public void onRemoval()
	{
		// Dissociate sales, as the only other option is cascading the delete and we don't want trusted users deleting sale records by deleting comics/authors/etc.
		for (Sale sale : getSales()) {
			sale.setComic(null);
		}

		// Automatically saved by Hibernate on transaction flush.
	}

	/** Cached volume count, for performance reasons. (to avoid pulling in the entire list when we need the count only) */
	public int getVolumeCount()
	{
		return volumeCount;
	}
	public void setVolumeCount(int volumeCount)
	{
		this.volumeCount = volumeCount;
	}

	@Transient
	@CustomValidator
	public boolean isValidVolumeCount()
	{
		return getVolumeCount() == volumes.size();
	}
}
