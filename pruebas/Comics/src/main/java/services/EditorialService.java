
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.EditorialRepository;
import domain.Editorial;

@Service
@Transactional
public class EditorialService {

	@Autowired
	private EditorialRepository	editorialRepository;


	public EditorialService() {
		super();
	}

	public Editorial create() {
		final Editorial res = new Editorial();

		return res;
	}

	public Collection<Editorial> findAll() {
		final Collection<Editorial> res = this.editorialRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public Editorial findOne(final int Id) {
		final Editorial res = this.editorialRepository.findOne(Id);
		Assert.notNull(res);

		return res;
	}

	public Editorial save(final Editorial editorial) {
		Assert.notNull(editorial);

		final Editorial res = this.editorialRepository.save(editorial);

		return res;
	}

	public void delete(final Editorial editorial) {
		Assert.notNull(editorial);
		Assert.isTrue(editorial.getId() != 0);

		this.editorialRepository.delete(editorial);
	}

}
