
package repositories;

import java.util.Collection;
import java.util.Date;

import domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Finder;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

	@Query("select p from Position p where (p.profile like %?1% or p.description like %?1% or p.title like %?1% or p.ticker like %?1%) and p.isFinal =TRUE")
	Collection<Position> getPositionsByKeyWord(String keyWord);

	@Query("select p from Position p where (?1 member of p.skill or ?1 member of p.technology) and p.isFinal =TRUE")
	Collection<Position> getPositionsContainsKeyWord(String keyWord);

	@Query("select p from Position p where p.deadline = ?1 and p.isFinal =TRUE")
	Collection<Position> getPositionsByDeadline(Date deadline);

	@Query("select p from Position p where (p.deadline between ?1 and ?2) and p.isFinal =TRUE")
	Collection<Position> getPositionsUntilDeadline(Date actualDate, Date deadline);

	@Query("select p from Position p where (p.salary >= ?1) and p.isFinal =TRUE")
	Collection<Position> getPositionsByMinSalary(int minSalary);

	@Query("select p from Position p where p.isFinal =TRUE")
	Collection<Position> findAllFinal();

}
