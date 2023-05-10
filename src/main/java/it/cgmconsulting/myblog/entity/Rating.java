package it.cgmconsulting.myblog.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.Check;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Check(constraints = "rate > 0 AND rate < 6")
public class Rating {


    @EmbeddedId
    @EqualsAndHashCode.Include
    private RatingId ratingId;

    private byte rate;


    public Rating(RatingId rId, byte rate) {
    }
}
