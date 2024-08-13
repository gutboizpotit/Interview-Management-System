package btl.g4.mock.repository;

import btl.g4.mock.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
    @Query("SELECT j FROM Job j WHERE " +
            "LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.level) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.status) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.workingAddress) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.requiredSkills) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.benefits) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "ORDER BY " +
            "CASE j.status " +
            "WHEN 'Open' THEN 1 " +
            "WHEN 'Draft' THEN 2 " +
            "WHEN 'Closed' THEN 3 " +
            "ELSE 4 END, " +
            "j.createdAt DESC")
    List<Job> search(@Param("keyword") String keyword);

    @Query("SELECT j FROM Job j ORDER BY " +
            "CASE j.status " +
            "WHEN 'Open' THEN 1 " +
            "WHEN 'Draft' THEN 2 " +
            "WHEN 'Closed' THEN 3 " +
            "ELSE 4 END ASC, " +
            "j.createdAt DESC")
    List<Job> findAllOrderByCreatedAtDesc();

    Page<Job> findAll(Pageable pageable);
}
