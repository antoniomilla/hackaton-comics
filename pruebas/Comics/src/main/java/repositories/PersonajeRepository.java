
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Personaje;

@Repository
public interface PersonajeRepository extends JpaRepository<Personaje, Integer> {

	@Query("select p from Personaje p where p.id=?1")
	Personaje findById(int id);

}
