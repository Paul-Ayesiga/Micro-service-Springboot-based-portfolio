package com.portifolio.portfolio_service.repository;

import com.portifolio.portfolio_service.model.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    @Query("SELECT DISTINCT e FROM Experience e LEFT JOIN FETCH e.responsibilities LEFT JOIN FETCH e.technologies WHERE e.current = true ORDER BY e.startDate DESC")
    List<Experience> findByCurrentTrue();

    @Query("SELECT DISTINCT e FROM Experience e LEFT JOIN FETCH e.responsibilities LEFT JOIN FETCH e.technologies ORDER BY e.startDate DESC")
    List<Experience> findAllOrderByStartDateDesc();

    @Query("SELECT DISTINCT e FROM Experience e LEFT JOIN FETCH e.responsibilities LEFT JOIN FETCH e.technologies WHERE e.id = :id")
    Experience findByIdWithCollections(@Param("id") Long id);
}
