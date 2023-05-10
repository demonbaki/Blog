package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Authority;
import it.cgmconsulting.myblog.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    boolean existsByUsernameOrEmail(String username, String email);

    Optional<User> findByConfirmCode(String confirmCode);

    Optional<User> findByUsernameOrEmail(String username, String email);

    @Modifying // serve per INSERT e UPDATE
    @Transactional
    @Query(value="UPDATE User u SET u.password = :newPassword, u.updatedAt = :now WHERE u.id = :id")
    void updatePassword(@Param("id") long id, @Param("newPassword") String newPassword, @Param("now") LocalDateTime now);

    @Modifying
    @Transactional
    @Query(value="UPDATE User u SET u.username = :newUsername, u.updatedAt= :now WHERE u.id= :id")
    void updateUsername(@Param("id") long id, @Param("newUsername") String newUsername, @Param("now")LocalDateTime now);
    boolean existsByUsernameAndIdNot(String username,Long id);

    @Query(value="SELECT COUNT(u) FROM User u " +
            "INNER JOIN u.authorities a ON a.authorityName = 'ROLE_MODERATOR' " +
            "WHERE u.id = :to"
    )
    int getModeratorsToReassignement(@Param("to") long to);

}
