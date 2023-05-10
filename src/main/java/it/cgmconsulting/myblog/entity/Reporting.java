package it.cgmconsulting.myblog.entity;
import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import it.cgmconsulting.myblog.entity.common.ReportingStatus;
import jakarta.persistence.*;
import jdk.javadoc.doclet.Reporter;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Reporting extends CreationUpdate {

    @EqualsAndHashCode.Include
    @EmbeddedId
    private ReportingId reportingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reporter_id", nullable = false)
    private User reporter;

    @ManyToOne
    @JoinColumn(name="reason_id", nullable = false)
    private Reason reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 10)
    private ReportingStatus status = ReportingStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="moderator", nullable = false)
    private User moderator;

    public Reporting(ReportingId reportingId, User reporter, Reason reason) {
        this.reportingId = reportingId;
        this.reporter = reporter;
        this.reason = reason;
    }
}
