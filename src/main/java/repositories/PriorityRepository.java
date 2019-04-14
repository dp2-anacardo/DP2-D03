
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Priority;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, Integer> {

	@Query("select count(m) from Message m where m.priority.id = ?1")
	Integer getPriorityCount(int id);
	@Query("select p from Priority p join p.name m where(KEY(m) = 'ES' and 'ALTA' in (VALUE(m)))")
	Priority getHighPriority();

}
