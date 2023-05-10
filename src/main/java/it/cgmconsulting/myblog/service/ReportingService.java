package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.*;
import it.cgmconsulting.myblog.entity.common.ReportingStatus;
import it.cgmconsulting.myblog.payload.response.ReportingResponse;
import it.cgmconsulting.myblog.repository.CommentRepository;
import it.cgmconsulting.myblog.repository.ReasonHistoryRepository;
import it.cgmconsulting.myblog.repository.ReasonRepository;
import it.cgmconsulting.myblog.repository.ReportingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ReportingService {
    private final ReportingRepository reportingRepository;
    private final ReasonRepository reasonRepository;
    private final ReasonHistoryRepository reasonHistoryRepository;
    private final CommentRepository commentRepository;
    public ReportingService(ReportingRepository reportingRepository, ReasonRepository reasonRepository, ReasonHistoryRepository reasonHistoryRepository, CommentRepository commentRepository) {
        this.reportingRepository = reportingRepository;
        this.reasonRepository = reasonRepository;
        this.reasonHistoryRepository = reasonHistoryRepository;
        this.commentRepository = commentRepository;
    }

    /* ********************************** REPORTING ************************************** */

    public void save(Reporting rep){
        reportingRepository.save(rep);
    }

    public boolean existsById(ReportingId reportingId){
        return reportingRepository.existsById(reportingId);
    }

    public List<ReportingResponse> getReportings(ReportingStatus status, long moderatorId){
        return reportingRepository.getReportings(status, moderatorId);
    }


    /* ********************************** REASON ************************************** */

    public Reason getValidReasonByReasonId(String reasonId, LocalDate now){
        return reasonRepository.getValidReasonByReasonId(reasonId, now);
    }

    public void save(Reason r){
        reasonRepository.save(r);
    }

    public boolean existsById(String reasonId){
        return reasonRepository.existsById(reasonId);
    }


    /* ********************************** REASON_HISTORY ************************************** */

    public Optional<ReasonHistory> findFirstByReasonHistoryIdReasonIdOrderByReasonHistoryIdStartDateDesc(String reasonId){
        return reasonHistoryRepository.findFirstByReasonHistoryIdReasonIdOrderByReasonHistoryIdStartDateDesc(reasonId);
    }

    public void save(ReasonHistory rh){
        reasonHistoryRepository.save(rh);
    }

    public List<String> getValidReasons(){
        return reasonHistoryRepository.getValidReasons();
    }


    public Optional<Reporting> findByReportingIdCommentId(long commentId) {
        return reportingRepository.findByReportingIdCommentId(commentId);
    }


    public List<Reporting> findAllByModeratorIdAndReportingIdCommentIdInAndStatus(long from, Set<Long> ids, ReportingStatus reportingStatus) {
        return reportingRepository.findAllByModeratorIdAndReportingIdCommentIdInAndStatus(from, ids, reportingStatus);
    }
}
