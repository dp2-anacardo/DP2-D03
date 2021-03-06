package repositories;

import domain.Curricula;
import domain.Hacker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CurriculaRepository extends JpaRepository<Curricula, Integer> {
    @Query("select (c) from Hacker h join h.curricula c where c.isCopy = false and h = ?1")
    Collection<Curricula> getCurriculaByHacker(Hacker h);
}
