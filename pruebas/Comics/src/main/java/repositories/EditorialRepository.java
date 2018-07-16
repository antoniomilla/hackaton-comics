
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Editorial;

@Repository
public interface EditorialRepository extends JpaRepository<Editorial, Integer> {

	@Query("select e from Editorial e where e.id=?1")
	Editorial findById(int id);

}
