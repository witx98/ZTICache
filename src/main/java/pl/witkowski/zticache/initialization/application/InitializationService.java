package pl.witkowski.zticache.initialization.application;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.witkowski.zticache.initialization.application.port.InitializationUseCase;
import pl.witkowski.zticache.initialization.domain.CsvMovie;
import pl.witkowski.zticache.movie.application.commands.CreateMovieCommand;
import pl.witkowski.zticache.movie.application.port.MovieUseCase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


@Service
@RequiredArgsConstructor
@Slf4j
public class InitializationService implements InitializationUseCase {

    private final MovieUseCase movieService;


    @Override
    @Transactional
    public void initialize() {
        initMoviesData();
    }


    private void initMoviesData() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("static/movie.csv").getInputStream()))) {
            CsvToBean<CsvMovie> build = new CsvToBeanBuilder<CsvMovie>(reader)
                    .withType(CsvMovie.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            build.stream().forEach(this::initMovies);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse CSV file", e);
        }
    }

    private void initMovies(CsvMovie csvMovie) {
        CreateMovieCommand command = new CreateMovieCommand(
                csvMovie.getTitle(),
                csvMovie.getDirector(),
                csvMovie.parsePremiere(),
                csvMovie.getProduction(),
                csvMovie.parseCategories()
        );

        movieService.createMovie(command);
    }

}

