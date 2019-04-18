
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Administrator;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

    //Query 1: The average, the minimum, the maximum, and the standard deviation of the number of positions per company.

    @Query("select avg(1.0*(select count(f) from Position f where f.company.id = c.id)) from Company c")
    Double getAvgNumberOfPositions();

    Double getMinimumNumberOfPositions();



}
