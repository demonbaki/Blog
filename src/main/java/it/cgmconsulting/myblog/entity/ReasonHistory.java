package it.cgmconsulting.myblog.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ReasonHistory {

    @EmbeddedId
    private ReasonHistoryId reasonHistoryId;

    private LocalDate endDate;

    private int severity;


    public ReasonHistory(ReasonHistoryId reasonHistoryId, int severity) {
        this.reasonHistoryId = reasonHistoryId;
        this.severity = severity;
    }
}
/*

PAROLACCE  2020-01-01 2022-12-31   5
PAROLACCE  2023-01-01 null   3


 */