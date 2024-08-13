package btl.g4.mock.repository;

import btl.g4.mock.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
    @Query("SELECT c FROM Candidate c WHERE c.status != 'Banned'")
    List<Candidate> findAllCandidatesNotBanned();

    @Query("SELECT DISTINCT c FROM Candidate c JOIN InterviewSchedule i ON c.id = i.candidate.id WHERE c.status != 'Banned'")
    List<Candidate> findAllCandidateInInterviewSchedule();

    @Query("SELECT c FROM Candidate c WHERE " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.gender) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.position) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.address) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.skills) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.status) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.yearOfExperience) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.highestLevel) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.note) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "ORDER BY " +
            "CASE c.status " +
            "WHEN 'Waiting for interview' THEN 1 " +
            "WHEN 'Waiting for approval' THEN 2 " +
            "WHEN 'Waiting for response' THEN 3 " +
            "WHEN 'Open' THEN 4 " +
            "WHEN 'Passed Interview' THEN 5 " +
            "WHEN 'Approved Offer' THEN 6 " +
            "WHEN 'Rejected Offer' THEN 7 " +
            "WHEN 'Accepted offer' THEN 8 " +
            "WHEN 'Declined offer' THEN 9 " +
            "WHEN 'Cancelled offer' THEN 10 " +
            "WHEN 'Failed interview' THEN 11 " +
            "WHEN 'Cancelled interview' THEN 12 " +
            "WHEN 'Banned' THEN 13 " +
            "ELSE 14 END, " +
            "c.createdAt DESC")
    List<Candidate> search(@Param("keyword") String keyword);

    @Query("SELECT c FROM Candidate c ORDER BY " +
            "CASE c.status " +
            "WHEN 'Waiting for interview' THEN 1 " +
            "WHEN 'Waiting for approval' THEN 2 " +
            "WHEN 'Waiting for response' THEN 3 " +
            "WHEN 'Open' THEN 4 " +
            "WHEN 'Passed Interview' THEN 5 " +
            "WHEN 'Approved Offer' THEN 6 " +
            "WHEN 'Rejected Offer' THEN 7 " +
            "WHEN 'Accepted offer' THEN 8 " +
            "WHEN 'Declined offer' THEN 9 " +
            "WHEN 'Cancelled offer' THEN 10 " +
            "WHEN 'Failed interview' THEN 11 " +
            "WHEN 'Cancelled interview' THEN 12 " +
            "WHEN 'Banned' THEN 13 " +
            "ELSE 14 END, " +
            "c.createdAt DESC")
    List<Candidate> findAllOrderByStatusAndCreatedAtDesc();
}
