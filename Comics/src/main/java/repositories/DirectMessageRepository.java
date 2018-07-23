
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.DirectMessage;

@Repository
public interface DirectMessageRepository extends JpaRepository<DirectMessage, Integer> {

	@Query("select d from DirectMessage d where d.id=?1")
	DirectMessage findById(int id);
}
