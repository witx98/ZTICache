package witkowski.mateusz.bookseat.movie.application.commands;

import witkowski.mateusz.bookseat.movie.domain.Category;

import java.time.LocalDate;
import java.util.Set;

public record CreateMovieCommand(String title, String director, Long duration, String description,
                                 LocalDate premiere, String production, Set<Category> categories) {
}
