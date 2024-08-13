package btl.g4.mock.repository;

import btl.g4.mock.entity.InterviewSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Integer> {
    @Query("SELECT i FROM InterviewSchedule i WHERE i.candidate.id = :candidateId")
    List<InterviewSchedule> findAllInterviewSchedulesByCandidateId(@Param("candidateId") Integer candidateId);

    @Query("SELECT s FROM InterviewSchedule s WHERE s.scheduleTimeStart BETWEEN :startOfTomorrow AND :endOfTomorrow AND s.status NOT IN ('Cancelled', 'Closed')")
    List<InterviewSchedule> findSchedulesForTomorrow(@Param("startOfTomorrow") LocalDateTime startOfTomorrow, @Param("endOfTomorrow") LocalDateTime endOfTomorrow);


}
