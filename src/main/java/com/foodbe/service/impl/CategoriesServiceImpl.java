package com.foodbe.service.impl;

import com.foodbe.DTO.CategoriesDTO;
import com.foodbe.DTO.response.ApiResponse;
import com.foodbe.entity.CategoriesEntity;
import com.foodbe.repository.CategoriesRepository;
import com.foodbe.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        existing.setUpdateDate(ZonedDateTime.now());

        CategoriesEntity updated = categoriesRepository.save(existing);
        return ApiResponse.buildSuccessResponse(convertToDTO(updated));
    }

    @Override
    public ApiResponse<String> deleteCategory(Long id) {
        CategoriesEntity category = categoriesRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với id " + id));
        List<CategoriesEntity> listChildren = categoriesRepository.findByParentId(category.getParentId());
        if (!listChildren.isEmpty()) {
            return ApiResponse.buildSuccessResponse("Tồn tại danh mục con");
        }
        categoriesRepository.deleteById(id);
        return ApiResponse.buildSuccessResponse("Xóa danh mục thành công", "ID: " + id);
    }

    @Override
    public ApiResponse<CategoriesDTO> getCategoryById(Long id) {
        CategoriesEntity category = categoriesRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với id " + id));
        return ApiResponse.buildSuccessResponse(convertToDTO(category));
    }

    @Override
    public ApiResponse<List<CategoriesDTO>> getRootCategories() {
        List<CategoriesDTO> list = categoriesRepository.findAll()
                .stream()
                .filter(c -> c.getParentId() == null)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ApiResponse.buildSuccessResponse(list);
    }

    @Override
    public ApiResponse<List<CategoriesDTO>> getCategoryTree() {
        List<CategoriesEntity> all = categoriesRepository.findAll();
        List<CategoriesDTO> roots = all.stream()
                .filter(c -> c.getParentId() == null)
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        for (CategoriesDTO root : roots) {
            root.setChildren(getChildren(root.getId(), all));
        }

        return ApiResponse.buildSuccessResponse(roots);
    }

    private List<CategoriesDTO> getChildren(Long parentId, List<CategoriesEntity> all) {
        return all.stream()
                .filter(c -> parentId.equals(c.getParentId()))
                .map(this::convertToDTO)
                .peek(dto -> dto.setChildren(getChildren(dto.getId(), all)))
                .collect(Collectors.toList());
    }
}
