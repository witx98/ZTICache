package pl.witkowski.zticache.movie.application.commands;

import java.time.LocalDate;
import java.util.Set;

public record CreateMovieCommand(String title, String director, LocalDate premiere, String production,
                                 Set<String> categories) {
}
