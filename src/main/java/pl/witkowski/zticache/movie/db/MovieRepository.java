package pl.witkowski.zticache.movie.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.witkowski.zticache.movie.domain.MovieEntity;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {

    Optional<MovieEntity> findByTitleIgnoreCase(String title);
}