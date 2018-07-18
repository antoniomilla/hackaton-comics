
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Publisher;

@Repository
public interface PublishingCompanyRepository extends JpaRepository<Publisher, Integer> {

	@Query("select e from PublishingCompany e where e.id=?1")
	Publisher findById(int id);

}
