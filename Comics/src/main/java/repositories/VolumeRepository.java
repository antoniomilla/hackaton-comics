
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Author;
import domain.Volume;

@Repository
public interface VolumeRepository extends JpaRepository<Volume, Integer> {

	@Query("select a from Volume a where a.id=?1")
	Author findById(int id);

}
