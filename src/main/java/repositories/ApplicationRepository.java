
package repositories;

import java.util.Collection;

import domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

	@Query("select a from Application a where a.hacker.id = ?1")
	Collection<Application> getApplicationsByHacker(int hackerId);

	@Query("select a from Position p join p.company c join p.applications a where a.status != 'PENDING' and c.id = ?1")
	Collection<Application> getApplicationsByCompany(int companyId);

	@Query("select a from Position p join p.applications a where a.status != 'PENDING' and p.id = ?1")
	Collection<Application> getApplicationsByPosition(int positionId);

	@Query("select p from Position p join p.applications a where a.id =?1")
	Position getPositionByApplication(int applicationId);

}
