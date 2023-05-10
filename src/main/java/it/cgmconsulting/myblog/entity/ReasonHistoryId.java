package it.cgmconsulting.myblog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class ReasonHistoryId implements Serializable {

    @ManyToOne
    @JoinColumn(name= "reason_id", nullable = false)
    @EqualsAndHashCode.Include
    private Reason reason;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private LocalDate startDate;

}
