package btl.g4.mock.repository;

import btl.g4.mock.entity.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Integer> {

    @Query("SELECT o FROM Offer o ORDER BY o.id DESC")
    Page<Offer> findAllDesc(Pageable pageable);

    @Query("SELECT o FROM Offer o WHERE o.candidate.name LIKE %:keyword% ORDER BY o.id DESC")
    Page<Offer> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT o FROM Offer o WHERE o.department = :department ORDER BY o.id DESC")
    Page<Offer> findByDepartment(@Param("department") String department, Pageable pageable);

    @Query("SELECT o FROM Offer o WHERE o.status = :status ORDER BY o.id DESC")
    Page<Offer> findByStatus(@Param("status") String status, Pageable pageable);

    @Query("SELECT o FROM Offer o WHERE o.candidate.name LIKE %:keyword% AND o.department = :department ORDER BY o.id DESC")
    Page<Offer> findByKeywordAndDepartment(@Param("keyword") String keyword, @Param("department") String department, Pageable pageable);

    @Query("SELECT o FROM Offer o WHERE o.candidate.name LIKE %:keyword% AND o.status = :status ORDER BY o.id DESC")
    Page<Offer> findByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") String status, Pageable pageable);

    @Query("SELECT o FROM Offer o WHERE o.department = :department AND o.status = :status ORDER BY o.id DESC")
    Page<Offer> findByDepartmentAndStatus(@Param("department") String department, @Param("status") String status, Pageable pageable);

    @Query("SELECT o FROM Offer o WHERE o.candidate.name LIKE %:keyword% AND o.department = :department AND o.status = :status ORDER BY o.id DESC")
    Page<Offer> findByKeywordAndDepartmentAndStatus(@Param("keyword") String keyword, @Param("department") String department, @Param("status") String status, Pageable pageable);

    @Query("SELECT o FROM Offer o WHERE Date(o.createdAt) BETWEEN :from AND :to")
    List<Offer> findOffersBetweenDates(@Param("from") Date from, @Param("to") Date to);

    @Query("SELECT o FROM Offer o WHERE o.status = 'Waiting for Approval'")
    List<Offer> findOffersWaiting();
}
