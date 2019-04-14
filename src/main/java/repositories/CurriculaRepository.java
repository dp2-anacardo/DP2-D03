package repositories;

import domain.Curricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurriculaRepository extends JpaRepository<Curricula, Integer> {
}
