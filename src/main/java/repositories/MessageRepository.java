
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

	@Query("select a.messagesReceived from Actor a where a.id=?1")
	Collection<Message> findAllReceivedInPoolByActor(int actorID);

	@Query("select a.messagesSent from Actor a where a.id=?1")
	Collection<Message> findAllSentInPoolByActor(int actorID);

	@Query("select m from Message m where m.sender.id=?1")
	Collection<Message> findAllSentByActor(int actorID);

}
