
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

	@Query("select m from Message m where m.sender.id=?1")
	Collection<Message> findAllByActor(int actorID);

	@Query("select m from Message m join m.recipients r where r.id=?1")
	Collection<Message> findAllReceivedByActor(int actorID);

	@Query("select count(a) from Actor a join a.messages m where m.id=?1")
	Integer findMessageInPool(int messageID);

}
