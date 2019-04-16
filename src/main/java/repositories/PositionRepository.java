
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {

	@Query("select p from Position p where p.isFinal = true")
	List<Position> getPositionsAvailable();

	@Query("select p from Position p join p.company c where p.isFinal = true and c.id=?1")
	Collection<Position> getPositionsByCompany(int companyId);
}
