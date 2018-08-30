
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import domain.Actor;
import domain.DirectMessage;
import domain.MessageFolder;
import domain.Sale;
import domain.User;

@Repository
public interface DirectMessageRepository extends JpaRepository<DirectMessage, Integer> {
    List<DirectMessage> findByMessageFolderOrderByCreationTimeDesc(MessageFolder messageFolder);

    @Query("select dm from DirectMessage dm where dm.sale = ?1 and (dm.messageFolder.actor = ?2) and (dm.recipient = ?3 or dm.sender = ?3) order by dm.creationTime desc")
    Page<DirectMessage> findBySaleAndUsers(Sale sale, User principal, User buyer, Pageable pageRequest);
}
