package com.foodbe.repository;

import com.foodbe.entity.CategoriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Long> {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
    List<CategoriesEntity> findByParentId(Long parentId);
    @Query("select c.parentId from CategoriesEntity c where c.parentId in :ids")
    List<Long> findParentIdsIn(@Param("ids") List<Long> ids);
}
