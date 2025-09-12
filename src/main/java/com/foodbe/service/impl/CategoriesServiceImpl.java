package com.foodbe.service.impl;

import com.foodbe.DTO.CategoriesDTO;
import com.foodbe.DTO.response.ApiResponse;
import com.foodbe.entity.CategoriesEntity;
import com.foodbe.repository.CategoriesRepository;
import com.foodbe.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoriesRepository categoriesRepository;

    @Autowired
    public CategoriesServiceImpl(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    private CategoriesDTO convertToDTO(CategoriesEntity entity) {
        return new CategoriesDTO(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getDescription(),
                entity.getParentId(),
                entity.isActive(),
                entity.getLevel(),
                entity.getCreateDate(),
                entity.getUpdateDate()
        );
    }

    private CategoriesEntity convertToEntity(CategoriesDTO dto) {
        CategoriesEntity entity = new CategoriesEntity();
        entity.setName(dto.getName());
        entity.setSlug(dto.getSlug());
        entity.setDescription(dto.getDescription());
        entity.setParentId(dto.getParentId());
        entity.setActive(dto.isActive());
        entity.setLevel(dto.getLevel());
        entity.setCreateDate(dto.getCreateDate());
        entity.setUpdateDate(dto.getUpdateDate());
        return entity;
    }

    @Override
    public ApiResponse<List<CategoriesDTO>> getAllCategories() {
        List<CategoriesEntity> entities = categoriesRepository.findAll();
        List<CategoriesDTO> dtoList = entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        List<Long> ids = dtoList.stream().map(CategoriesDTO::getId).collect(Collectors.toList());
        Set<Long> parentIdsWithChild = new HashSet<>(categoriesRepository.findParentIdsIn(ids));
        dtoList.forEach(dto -> dto.setHasChildren(parentIdsWithChild.contains(dto.getId())));
        return ApiResponse.buildSuccessResponse(dtoList);
    }

    @Override
    @Transactional
    public ApiResponse<CategoriesDTO> createCategory(CategoriesDTO categoryDTO) {
        if (categoriesRepository.existsByName(categoryDTO.getName())) {
            return ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, "Tên danh mục đã tồn tại");
        }
        if (categoriesRepository.existsBySlug(categoryDTO.getSlug())) {
            return ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, "Đường dẫn đã tồn tại");
        }

        CategoriesEntity entity = convertToEntity(categoryDTO);
        entity.setCreateDate(ZonedDateTime.now());
        entity.setUpdateDate(ZonedDateTime.now());
        CategoriesEntity saved = categoriesRepository.save(entity);
        return ApiResponse.buildSuccessResponse(convertToDTO(saved));
    }

    @Override
    @Transactional
    public ApiResponse<CategoriesDTO> updateCategory(CategoriesDTO categoryDTO) {
        CategoriesEntity existing = categoriesRepository.findById(categoryDTO.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với id " + categoryDTO.getId()));

        existing.setName(categoryDTO.getName());
        existing.setSlug(categoryDTO.getSlug());
        existing.setDescription(categoryDTO.getDescription());
        existing.setParentId(categoryDTO.getParentId());
        existing.setActive(categoryDTO.isActive());
        existing.setLevel(categoryDTO.getLevel());
        existing.setUpdateDate(ZonedDateTime.now());

        CategoriesEntity updated = categoriesRepository.save(existing);
        return ApiResponse.buildSuccessResponse(convertToDTO(updated));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<String> deleteCategory(Long id) {
        categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với id " + id));

        Set<Long> toDelete = collectDescendantIdsInclusive(id);

        categoriesRepository.deleteAllByIdInBatch(toDelete);

        return ApiResponse.buildSuccessResponse("Xoá danh mục thành công", "Ids: " + toDelete);
    }

    @Override
    public ApiResponse<CategoriesDTO> getCategoryById(Long id) {
        CategoriesEntity category = categoriesRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với id " + id));
        return ApiResponse.buildSuccessResponse(convertToDTO(category));
    }

    private Set<Long> collectDescendantIdsInclusive(Long rootId) {
        Set<Long> all = new LinkedHashSet<>();
        List<Long> layer = new ArrayList<>();
        all.add(rootId);
        layer.add(rootId);
        while (!layer.isEmpty()) {
            List<CategoriesEntity> children = categoriesRepository.findByParentIdIn(layer);
            layer = children.stream()
                    .map(CategoriesEntity::getId)
                    .filter(id -> !all.contains(id))
                    .collect(Collectors.toList());
            all.addAll(layer);
        }
        return all;
    }
}
