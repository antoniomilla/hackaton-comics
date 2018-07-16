
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Administrador;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {

}
