
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
    //TODO FALLO
//	@Query("select p from Position p where (p.profile like %?1% or p.description like %?1% or p.title like %?1%" + "or p.ticker like %?1% or ?1 member of p.skill or ?1 member of p.technology ) " + "and p.isFinal =TRUE and p.status='ACCEPTED'")
//	Collection<Position> getPositionsByKeyWord(String keyWord);
//
//	@Query("select p from Position p where (p.deadline like ?1) and p.isFinal =TRUE and p.status='ACCEPTED'")
//	Collection<Position> getPositionsByDeadline(Date deadline);
//
//	@Query("select p from Position p where (p.deadline between ?1 and ?2) and p.isFinal =TRUE and p.status='ACCEPTED'")
//	Collection<Position> getPositionsUntilDeadline(Date actualDate, Date deadline);
//
//	@Query("select p from Position p where (p.salary >= ?1) and p.isFinal =TRUE and p.status='ACCEPTED'")
//	Collection<Position> getPositionsByMinSalary(int minSalary);
//
//	@Query("select p from Position p where p.isFinal =TRUE and p.status='ACCEPTED'")
//	Collection<Position> findAllFinal();

}
