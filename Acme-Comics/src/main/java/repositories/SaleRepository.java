
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Author;
import domain.Comic;
import domain.ComicCharacter;
import domain.Publisher;
import domain.Sale;
import domain.SaleStatus;
import domain.User;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {
    List<Sale> findByStatus(SaleStatus status);

    @Query("select distinct s from Sale s left join s.interestedUsers u where s.user = ?1 or u = ?1")
    List<Sale> findByUserOrInterestedUser(User user);
}
