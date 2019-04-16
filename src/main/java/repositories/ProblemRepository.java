
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Problem;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {

	@Query("select p from Problem p where p.company.id = ?1")
	Collection<Problem> findAllByCompany(final int companyID);

	@Query("select p from Problem p where p.isFinal = true and p.company.id = ?1")
	List<Problem> getProblemsFinalByCompany(final int companyId);
}
