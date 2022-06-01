package pl.witkowski.zticache.movie.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.witkowski.zticache.movie.application.commands.CreateMovieCommand;
import pl.witkowski.zticache.movie.application.port.MovieUseCase;
import pl.witkowski.zticache.movie.db.MovieRepository;
import pl.witkowski.zticache.movie.domain.MovieEntity;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MovieService implements MovieUseCase {

    private final MovieRepository repository;

    @Override
    public List<MovieEntity> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<MovieEntity> getByTitle(String title) {
        return repository.findByTitleIgnoreCase(title);
    }

    @Override
    @Transactional
    public Long createMovie(CreateMovieCommand command) {
        return repository.save(buildMovieEntity(command)).getId();
    }

    private MovieEntity buildMovieEntity(CreateMovieCommand command) {
        return MovieEntity.builder()
                .title(command.title())
                .categories(command.categories())
                .director(command.director())
                .production(command.production())
                .premiere(command.premiere())
                .build();
    }
}
