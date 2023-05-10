package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Reason;
import it.cgmconsulting.myblog.entity.ReasonHistory;
import it.cgmconsulting.myblog.entity.ReasonHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReasonHistoryRepository extends JpaRepository<ReasonHistory, ReasonHistoryId> {


    Optional<ReasonHistory> findFirstByReasonHistoryIdReasonIdOrderByReasonHistoryIdStartDateDesc(String reasonId);

    @Query(value="SELECT rh.reason_id FROM reason_history rh WHERE rh.end_date IS NULL ORDER BY rh.reason_id", nativeQuery = true)
    List<String> getValidReasons();

    List<ReasonHistory> findAllByReasonHistoryIdReasonIdOrderByReasonHistoryIdStartDateDesc(String reasonId);
}
