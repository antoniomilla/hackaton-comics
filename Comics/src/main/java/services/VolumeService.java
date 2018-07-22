
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.VolumeRepository;
import domain.Volume;

@Service
@Transactional
public class VolumeService {

	@Autowired
	private VolumeRepository	volumeRepository;


	public VolumeService() {
		super();
	}

	public Volume create() {
		final Volume res = new Volume();

		return res;
	}

	public Collection<Volume> findAll() {
		final Collection<Volume> res = this.volumeRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public Volume findOne(final int Id) {
		final Volume res = this.volumeRepository.findOne(Id);
		Assert.notNull(res);

		return res;
	}

	public Volume save(final Volume volume) {
		Assert.notNull(volume);

		final Volume res = this.volumeRepository.save(volume);

		return res;
	}

	public void delete(final Volume volume) {
		Assert.notNull(volume);
		Assert.isTrue(volume.getId() != 0);

		this.volumeRepository.delete(volume);
	}

}
