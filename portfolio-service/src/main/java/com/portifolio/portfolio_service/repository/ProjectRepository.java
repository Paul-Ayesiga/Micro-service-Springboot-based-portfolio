package com.portifolio.portfolio_service.repository;

import com.portifolio.portfolio_service.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.technologies LEFT JOIN FETCH p.categories WHERE p.featured = true")
    List<Project> findByFeaturedTrue();

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.technologies LEFT JOIN FETCH p.categories JOIN p.categories c WHERE c = :category")
    List<Project> findByCategory(@Param("category") String category);

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.technologies LEFT JOIN FETCH p.categories JOIN p.technologies t WHERE t = :technology")
    List<Project> findByTechnology(@Param("technology") String technology);

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.technologies LEFT JOIN FETCH p.categories")
    List<Project> findAllWithCollections();

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.technologies LEFT JOIN FETCH p.categories WHERE p.id = :id")
    Project findByIdWithCollections(@Param("id") Long id);
}
