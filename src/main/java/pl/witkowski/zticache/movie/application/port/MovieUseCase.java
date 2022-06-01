package pl.witkowski.zticache.movie.application.port;

import pl.witkowski.zticache.movie.application.commands.CreateMovieCommand;
import pl.witkowski.zticache.movie.domain.MovieEntity;

import java.util.List;
import java.util.Optional;

public interface MovieUseCase {

    List<MovieEntity> getAll();

    Optional<MovieEntity> getByTitle(String title);

    Long createMovie(CreateMovieCommand command);
}
