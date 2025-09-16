package com.foodbe.repository;

import com.foodbe.constants.Constants;
import com.foodbe.entity.MediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<MediaEntity, Long> {
    List<MediaEntity> findByTypeAndReferenceId(Constants.MediaType type, Long referenceId);
    Optional<MediaEntity> findFirstByTypeAndReferenceIdAndIsPrimaryTrue(Constants.MediaType type, Long referenceId);
    long countByTypeAndReferenceId(Constants.MediaType type, Long referenceId);
}
