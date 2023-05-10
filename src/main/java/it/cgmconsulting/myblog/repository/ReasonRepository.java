package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Reason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReasonRepository extends JpaRepository<Reason, String> {

    @Query(value="SELECT r FROM Reason r " +
            "INNER JOIN ReasonHistory rh ON r.id=rh.reasonHistoryId.reason.id " +
            "WHERE rh.reasonHistoryId.reason.id = :reasonId " +
            "AND rh.endDate IS NULL OR (:now BETWEEN rh.reasonHistoryId.startDate and rh.endDate)")
    Reason getValidReasonByReasonId(@Param("reasonId") String reasonId, @Param("now") LocalDate now);


}
