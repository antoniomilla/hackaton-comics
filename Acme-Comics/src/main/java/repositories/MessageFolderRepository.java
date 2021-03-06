
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.MessageFolder;
import domain.MessageFolderType;

@Repository
public interface MessageFolderRepository extends JpaRepository<MessageFolder, Integer> {
	@Query("select mf from MessageFolder mf where mf.actor = ?1 and mf.type = ?2")
    MessageFolder findSystemFolderForActor(Actor actor, MessageFolderType type);
}
