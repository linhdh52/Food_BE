package com.foodbe.service.impl;

import com.foodbe.DTO.ProductDTO;
import com.foodbe.DTO.response.ApiResponse;
import com.foodbe.DTO.response.MediaResponse;
import com.foodbe.constants.Constants;
import com.foodbe.entity.CategoriesEntity;
import com.foodbe.entity.MediaEntity;
import com.foodbe.entity.ProductEntity;
import com.foodbe.repository.CategoriesRepository;
import com.foodbe.repository.MediaRepository;
import com.foodbe.repository.ProductRepository;
import com.foodbe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String MEDIA_TYPE_PRODUCT = "PRODUCT";
    private static final String TAG_DELIM = ",";

    private final ProductRepository productRepository;

    @Autowired(required = false)
    private MediaRepository mediaRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ====================== GET ALL ======================
    @Override
    public ApiResponse<List<ProductDTO>> getAllProducts() {
        try {
            List<ProductEntity> entities = productRepository.findAll();
            List<ProductDTO> dtos = entities.stream().map(this::toDTO).collect(Collectors.toList());
            return ApiResponse.buildSuccessResponse(dtos);
        } catch (Exception ex) {
            return ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, "Lấy danh sách sản phẩm thất bại: " + ex.getMessage());
        }
    }

    // ====================== CREATE ======================
    @Transactional
    @Override
    public ApiResponse<ProductDTO> createProduct(ProductDTO productDTO) {
        try {
            ProductEntity e = new ProductEntity();
            applyFromDTO(e, productDTO);
            e = productRepository.save(e);

            // merge ảnh nếu FE có gửi
            if (productDTO.getImages() != null) {
                mergeImages(e.getId(), productDTO.getImages());
            }

            return ApiResponse.buildSuccessResponse(toDTO(e));
        } catch (Exception ex) {
            return ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, "Tạo sản phẩm thất bại: " + ex.getMessage());
        }
    }

    // ====================== UPDATE ======================
    @Transactional
    @Override
    public ApiResponse<ProductDTO> updateProduct(ProductDTO productDTO) {
        if (productDTO.getId() == null) {
            return ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, "Thiếu id sản phẩm khi cập nhật");
        }
        try {
            ProductEntity e = productRepository.findById(productDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm id " + productDTO.getId()));

            applyFromDTO(e, productDTO);
            e = productRepository.save(e);

            if (productDTO.getImages() != null) {
                mergeImages(e.getId(), productDTO.getImages());
            }

            return ApiResponse.buildSuccessResponse(toDTO(e));
        } catch (Exception ex) {
            return ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, "Cập nhật sản phẩm thất bại: " + ex.getMessage());
        }
    }

    // ====================== DELETE ======================
    @Transactional
    @Override
    public ApiResponse<String> deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            return ApiResponse.buildErrorResponse(
                    HttpStatus.NOT_FOUND,
                    "Không tìm thấy sản phẩm id " + id
            );
        }

        // Xóa ảnh đi kèm (nếu có)
        if (mediaRepository != null) {
            mediaRepository.deleteByTypeAndReferenceId(MEDIA_TYPE_PRODUCT, id);
        }

        productRepository.deleteById(id);
        return ApiResponse.buildSuccessResponse("Xóa sản phẩm thành công, id = " + id);
    }

    // ====================== CONVERT ENTITY -> DTO ======================
    private ProductDTO toDTO(ProductEntity e) {
        ProductDTO dto = new ProductDTO();
        dto.setId(e.getId());

        if (e.getCategory() != null) {
            dto.setCategoryId(e.getCategory().getId());
            dto.setCategoryName(e.getCategory().getName());
        }

        dto.setName(e.getName());
        dto.setDescription(e.getDescription());
        dto.setPrice(e.getPrice());
        dto.setPriceAfterPromotion(e.getPrice());
        dto.setPromotionDiscount(java.math.BigDecimal.ZERO);
        dto.setStock(e.getStock());
        dto.setUnit(e.getUnit());
        dto.setSku(e.getSku());
        dto.setActive(e.getActive());
        dto.setCreateDate(e.getCreateDate());
        dto.setUpdateDate(e.getUpdateDate());

        if (mediaRepository != null) {
            try {
                List<MediaEntity> all = mediaRepository.findByTypeAndReferenceId(Constants.MediaType.PRODUCT, e.getId());
                all.stream().filter(MediaEntity::isPrimary)
                        .findFirst().ifPresent(primary -> dto.setPrimaryImage(primary.getBase64Data()));

                List<MediaResponse> imgs = new ArrayList<>();
                for (MediaEntity m : all) {
                    MediaResponse r = new MediaResponse();
                    r.setId(m.getId());
                    r.setFileName(m.getFileName());
                    r.setBase64Data(m.getBase64Data());
                    r.setPrimary(m.isPrimary());
                    r.setType(m.getType());
                    imgs.add(r);
                }
                dto.setImages(imgs);
            } catch (Throwable ignored) {
            }
        }

        dto.setTags(splitTags(e.getTags()));

        return dto;
    }

    // ====================== CONVERT DTO -> ENTITY ======================
    private void applyFromDTO(ProductEntity e, ProductDTO dto) {
        if (dto.getCategoryId() != null) {
            CategoriesEntity c = categoriesRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại id " + dto.getCategoryId()));
            e.setCategory(c);
        } else {
            e.setCategory(null);
        }

        e.setName(dto.getName());
        e.setDescription(dto.getDescription());
        e.setPrice(dto.getPrice());
        e.setStock(dto.getStock());
        e.setUnit(dto.getUnit());
        e.setSku(dto.getSku());
        e.setActive(dto.getActive() != null ? dto.getActive() : Boolean.TRUE);

        e.setTags(joinTags(dto.getTags()));
    }

    private void mergeImages(Long productId, List<MediaResponse> images) {
        if (mediaRepository == null) return;

        List<MediaEntity> existing = mediaRepository.findByTypeAndReferenceId(Constants.MediaType.PRODUCT, productId);
        Map<Long, MediaEntity> existingById = new HashMap<>();
        for (MediaEntity m : existing) {
            if (m.getId() != null) existingById.put(m.getId(), m);
        }

        Set<Long> keepIds = new HashSet<>();
        List<Long> requestedPrimaryIds = new ArrayList<>();

        if (images != null) {
            for (MediaResponse r : images) {
                Long rid = r.getId();
                if (rid != null && existingById.containsKey(rid)) {
                    // UPDATE
                    MediaEntity m = existingById.get(rid);
                    if (r.getFileName() != null) m.setFileName(r.getFileName());
                    if (r.getBase64Data() != null && !r.getBase64Data().trim().isEmpty()) {
                        m.setBase64Data(r.getBase64Data());
                    }
                    if (r.isPrimary()) {
                        requestedPrimaryIds.add(m.getId());
                    }
                    keepIds.add(m.getId());
                    mediaRepository.save(m);
                } else {
                    // CREATE
                    if (r.getBase64Data() == null || r.getBase64Data().trim().isEmpty()) continue;
                    MediaEntity m = new MediaEntity();
                    m.setType(Constants.MediaType.PRODUCT);
                    m.setReferenceId(productId);
                    m.setFileName(r.getFileName());
                    m.setBase64Data(r.getBase64Data());
                    mediaRepository.save(m);
                    keepIds.add(m.getId());
                    if (r.isPrimary()) {
                        requestedPrimaryIds.add(m.getId());
                    }
                    existing.add(m);
                    existingById.put(m.getId(), m);
                }
            }
        }

        for (MediaEntity old : existing) {
            if (old.getId() != null && !keepIds.contains(old.getId())) {
                mediaRepository.deleteById(old.getId());
            }
        }

        ensureSinglePrimary(existingById, requestedPrimaryIds);
    }

    // ====================== ENSURE SINGLE PRIMARY ======================
    private void ensureSinglePrimary(Map<Long, MediaEntity> existingById, List<Long> requestedPrimaryIds) {
        List<MediaEntity> all = new ArrayList<>(existingById.values());
        if (all.isEmpty()) return;

        MediaEntity chosenPrimary = null;

        if (requestedPrimaryIds != null && !requestedPrimaryIds.isEmpty()) {
            for (Long id : requestedPrimaryIds) {
                MediaEntity m = existingById.get(id);
                if (m != null) {
                    chosenPrimary = m;
                    break;
                }
            }
        } else {
            for (MediaEntity m : all) {
                if (m.isPrimary()) {
                    chosenPrimary = m;
                    break;
                }
            }
            if (chosenPrimary == null) chosenPrimary = all.get(0);
        }

        for (MediaEntity m : all) {
            boolean isPrimary = (m.getId() != null && chosenPrimary.getId() != null
                    && m.getId().equals(chosenPrimary.getId()));
            m.setPrimary(isPrimary);
            mediaRepository.save(m);
        }
    }

    private List<String> splitTags(String tagsStr) {
        if (tagsStr == null || tagsStr.trim().isEmpty()) return new ArrayList<>();
        String[] arr = tagsStr.split("\\s*" + java.util.regex.Pattern.quote(TAG_DELIM) + "\\s*");
        List<String> list = new ArrayList<>();
        for (String t : arr) {
            if (t == null) continue;
            String v = t.trim();
            if (!v.isEmpty()) list.add(v);
        }
        return list;
    }

    private String joinTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) return null;
        List<String> cleaned = tags.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());
        return cleaned.isEmpty() ? null : String.join(TAG_DELIM, cleaned);
    }
}
