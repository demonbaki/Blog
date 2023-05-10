package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.*;
import it.cgmconsulting.myblog.entity.common.ReportingStatus;
import it.cgmconsulting.myblog.payload.response.PostBoxesResponse;
import it.cgmconsulting.myblog.payload.response.ReportingResponse;
import it.cgmconsulting.myblog.request.ReasonRemoveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReportingRepository extends JpaRepository<Reporting, ReportingId> {


        @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.ReportingResponse(" +
                "rep.reportingId.comment.id, " +
                "rep.reporter.username, " +
                "rep.reportingId.comment.author.username, " +
                "rep.reason.id, " +
                "rep.status, " +
                "rep.updatedAt" +
                ") FROM Reporting rep " +
                "WHERE rep.status = :status " +
                "AND (rep.moderator.id = :moderatorId OR rep.moderator.id IS NULL) " +
                "ORDER BY rep.updatedAt DESC")
        List<ReportingResponse> getReportings(@Param("status") ReportingStatus status, @Param("moderatorId") long moderatorId);

        Optional<Reporting> findByReportingIdCommentId(long commentId);


        List<Reporting> findAllByModeratorIdAndReportingIdCommentIdInAndStatus(long from, Set<Long> ids, ReportingStatus reportingStatus);
}

