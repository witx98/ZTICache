package pl.witkowski.zticache.initialization.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CsvMovie {
    @CsvBindByName
    private String title;

    @CsvBindByName
    private String director;

    @CsvBindByName
    private String production;

    @CsvBindByName
    private String premiere;

    @CsvBindByName
    private String categories;

    public Set<String> parseCategories() {
        return Arrays.stream(categories.split(","))
                .collect(Collectors.toSet());
    }

    public LocalDate parsePremiere() {
        return LocalDate.parse(premiere, DateTimeFormatter.ISO_DATE);
    }
}
