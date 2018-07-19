
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.Range;

@Entity
@Access(AccessType.PROPERTY)
public class UserComic extends DomainEntity {

	private Boolean			started;
	private Integer			score;
	private UserComicStatus	status;
	private User			user;
	private Comic			comic;


	public Boolean getStarted() {
		return this.started;
	}

	public void setStarted(final Boolean started) {
		this.started = started;
	}
	@Range(min = 0, max = 10)
	public Integer getScore() {
		return this.score;
	}

	public void setScore(final Integer score) {
		this.score = score;
	}
	public UserComicStatus getStatus() {
		return this.status;
	}

	public void setStatus(final UserComicStatus status) {
		this.status = status;
	}

	@ManyToOne(optional = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	@ManyToOne(optional = false)
	public Comic getComic() {
		return this.comic;
	}

	public void setComic(final Comic comic) {
		this.comic = comic;
	}

}
