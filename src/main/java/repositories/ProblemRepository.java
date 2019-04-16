
package repositories;

import domain.Problem;
import domain.SocialProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {

    @Query("select p from Problem p where p.company.id = ?1")
    Collection<Problem> findAllByCompany(final int companyID);
}
