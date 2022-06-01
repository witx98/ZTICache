package witkowski.mateusz.bookseat.initialization.application;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import witkowski.mateusz.bookseat.cinema.application.commands.CreateCinemaCommand;
import witkowski.mateusz.bookseat.cinema.application.port.CinemaUseCase;
import witkowski.mateusz.bookseat.cinema.domain.CinemaEntity;
import witkowski.mateusz.bookseat.initialization.application.port.InitializationUseCase;
import witkowski.mateusz.bookseat.initialization.domain.CsvCinema;
import witkowski.mateusz.bookseat.initialization.domain.CsvMovie;
import witkowski.mateusz.bookseat.initialization.domain.CsvRoom;
import witkowski.mateusz.bookseat.initialization.domain.CsvScreening;
import witkowski.mateusz.bookseat.movie.application.commands.CreateMovieCommand;
import witkowski.mateusz.bookseat.movie.application.port.MovieUseCase;
import witkowski.mateusz.bookseat.movie.domain.MovieEntity;
import witkowski.mateusz.bookseat.room.application.commands.CreateRoomCommand;
import witkowski.mateusz.bookseat.room.application.port.RoomUseCase;
import witkowski.mateusz.bookseat.room.domain.RoomEntity;
import witkowski.mateusz.bookseat.screening.application.commands.CreateScreeningCommand;
import witkowski.mateusz.bookseat.screening.application.port.ScreeningUseCase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.TreeSet;


@Service
@RequiredArgsConstructor
@Slf4j
public class InitializationService implements InitializationUseCase {

    private final CinemaUseCase cinemaService;
    private final RoomUseCase roomService;
    private final MovieUseCase movieService;
    private final ScreeningUseCase screeningService;


    @Override
    @Transactional
    public void initialize() {
        initCinemasData();
        initRoomsData();
        initMoviesData();
        initScreeningData();
    }

    private void initScreeningData() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("static/screening.csv").getInputStream()))) {
            CsvToBean<CsvScreening> build = new CsvToBeanBuilder<CsvScreening>(reader)
                    .withType(CsvScreening.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            build.stream().forEach(this::initScreenings);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse CSV file", e);
        }
    }

    private void initScreenings(CsvScreening csvScreening) {
        MovieEntity movie = movieService.getByTitle(csvScreening.getMovieTitle())
                .orElseThrow(() -> new RuntimeException("Movie not found!"));

        CinemaEntity cinema = cinemaService.getByName(csvScreening.getCinemaName())
                .orElseThrow(() -> new RuntimeException("Cinema not found!"));

        RoomEntity room = roomService.getByCinemaAndName(cinema.getId(), csvScreening.getRoomName())
                .orElseThrow(() -> new RuntimeException("Room not found!"));


        CreateScreeningCommand command = new CreateScreeningCommand(
                csvScreening.getPrice(),
                room.getId(),
                movie.getId(),
                LocalDateTime.parse(csvScreening.getStartsAt()),
                LocalDateTime.parse(csvScreening.getStartsAt()).plus(movie.getDuration())
        );

        screeningService.createScreening(command).handle(
                screeningId -> {
                    log.info("Created screening id: " + screeningId);
                    return screeningId;
                },
                error -> {
                    log.error(error);
                    throw new RuntimeException(error);
                }
        );
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
                csvMovie.getDuration(),
                csvMovie.getDescription(),
                csvMovie.parsePremiere(),
                csvMovie.getProduction(),
                csvMovie.parseCategories()
        );

        movieService.createMovie(command).handle(
                movieId -> {
                    log.info("Created movie id: " + movieId);
                    return movieId;
                },
                error -> {
                    log.error(error);
                    throw new RuntimeException(error);
                }
        );

    }

    private void initRoomsData() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("static/room.csv").getInputStream()))) {
            CsvToBean<CsvRoom> build = new CsvToBeanBuilder<CsvRoom>(reader)
                    .withType(CsvRoom.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            build.stream().forEach(this::initRooms);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse CSV file", e);
        }
    }

    private void initRooms(CsvRoom csvRoom) {
        CinemaEntity cinema = cinemaService.getByName(csvRoom.getCinemaName())
                .orElseThrow(() -> new IllegalArgumentException("Cinema with name: " + csvRoom.getCinemaName() + " not found."));

        TreeSet<Integer> seats = new TreeSet<>();
        for (int i = 0; i < csvRoom.getCapacity(); i++) {
            seats.add(i + 1);
        }

        CreateRoomCommand command = new CreateRoomCommand(
                csvRoom.getRoomName(),
                csvRoom.getCapacity(),
                seats,
                cinema.getId()
        );
        roomService.createRoom(command).handle(
                roomId -> {
                    log.info("Created room id: " + roomId);
                    return roomId;
                },
                error -> {
                    log.error(error);
                    throw new RuntimeException();
                }
        );
    }

    private void initCinemasData() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("static/cinema.csv").getInputStream()))) {
            CsvToBean<CsvCinema> build = new CsvToBeanBuilder<CsvCinema>(reader)
                    .withType(CsvCinema.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            build.stream().forEach(this::initCinemas);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse CSV file", e);
        }
    }

    private void initCinemas(CsvCinema csvCinema) {
        CreateCinemaCommand command = new CreateCinemaCommand(
                csvCinema.getName(),
                csvCinema.getCity(),
                csvCinema.getStreet(),
                csvCinema.getZipCode()
        );
        cinemaService.createCinema(command).handle(
                cinemaId -> {
                    log.info("Created cinema id: " + cinemaId);
                    return cinemaId;
                },
                error -> {
                    log.error(error);
                    throw new RuntimeException(error);
                }
        );
    }
}

