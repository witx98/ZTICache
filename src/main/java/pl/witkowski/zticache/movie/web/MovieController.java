package pl.witkowski.zticache.movie.web;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.witkowski.zticache.movie.application.port.MovieUseCase;
import pl.witkowski.zticache.movie.domain.MovieEntity;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/movies")
@CacheConfig(cacheNames={"movies"})
public class MovieController {

    private final MovieUseCase movieService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MovieEntity> getMovies() {
        log.info("invocation of method getMovies");
        return movieService.getAll();
    }

    @GetMapping("/cacheable")
    @ResponseStatus(HttpStatus.OK)
    @Cacheable
    public List<MovieEntity> getCacheableMovies() {
        log.info("invocation of method getCacheableMovies");
        return movieService.getAll();
    }

    @GetMapping("/cachePut")
    @ResponseStatus(HttpStatus.OK)
    @CachePut
    public List<MovieEntity> getCachePutMovies() {
        log.info("invocation of method getCachePutMovies");
        return movieService.getAll();
    }

    @GetMapping("/cachePut/limit")
    @ResponseStatus(HttpStatus.OK)
    @CachePut(unless="#result.size() > 2")
    public List<MovieEntity> getCachePutLimitMovies() {
        log.info("invocation of method getCachePutLimitMovies");
        return movieService.getAll();
    }

    @GetMapping("/cachePut/{title}")
    @ResponseStatus(HttpStatus.OK)
    @CachePut(condition = "#title =='MATRIX'")
    public Optional<MovieEntity> getMovie(@PathVariable String title) {
        log.info("invocation of method getMovie");
        return movieService.getByTitle(title);
    }

    @GetMapping("/cacheEvict")
    @ResponseStatus(HttpStatus.OK)
    @CacheEvict
    public List<MovieEntity> getCacheEvictMovies() {
        log.info("invocation of method getCacheEvictMovies");
        return movieService.getAll();
    }
}
