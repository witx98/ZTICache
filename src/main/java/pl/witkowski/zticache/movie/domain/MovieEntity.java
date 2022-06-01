package pl.witkowski.zticache.movie.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.witkowski.zticache.jpa.model.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@Table(name = "movies")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class MovieEntity extends BaseEntity {

    @Column(unique = true)
    private String title;
    private String director;
    private String production;
    private LocalDate premiere;


    @CollectionTable(
            name = "categories",
            joinColumns = @JoinColumn(name = "movie_id")
    )
    @Column(name = "categories")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> categories = new HashSet<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}