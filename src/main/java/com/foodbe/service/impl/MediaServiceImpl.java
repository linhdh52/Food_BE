package com.foodbe.service.impl;

import com.foodbe.DTO.request.MediaUploadRequest;
import com.foodbe.DTO.response.ApiResponse;
import com.foodbe.DTO.response.MediaResponse;
import com.foodbe.constants.Constants;
import com.foodbe.entity.MediaEntity;
import com.foodbe.repository.MediaRepository;
import com.foodbe.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MediaServiceImpl implements MediaService {

    @Autowired
    private MediaRepository mediaRepository;

    private MediaResponse toResponse(MediaEntity e) {
        return new MediaResponse(
                e.getId(),
                e.getFileName(),
                e.getType(),
                e.getReferenceId(),
                e.isPrimary(),
                e.getCreatedAt(),
                e.getBase64Data()
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<MediaResponse> upload(MediaUploadRequest req) {
        MediaEntity entity = new MediaEntity();
        entity.setType(req.getType());
        entity.setReferenceId(req.getReferenceId());
        entity.setFileName(req.getFileName());
        entity.setBase64Data(req.getBase64Data());
        entity.setPrimary(req.isPrimary());

        MediaEntity saved = mediaRepository.save(entity);

        if (req.isPrimary()) {
            mediaRepository.findByTypeAndReferenceId(req.getType(), req.getReferenceId())
                    .stream()
                    .filter(m -> !m.getId().equals(saved.getId()) && m.isPrimary())
                    .forEach(m -> {
                        m.setPrimary(false);
                        mediaRepository.save(m);
                    });
        }

        return ApiResponse.buildSuccessResponse(toResponse(saved));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<List<MediaResponse>> uploadList(List<MediaUploadRequest> reqList) {
        List<MediaResponse> responses = reqList.stream()
                .map(req -> {
                    MediaEntity entity = new MediaEntity();
                    entity.setType(req.getType());
                    entity.setReferenceId(req.getReferenceId());
                    entity.setFileName(req.getFileName());
                    entity.setBase64Data(req.getBase64Data());
                    entity.setPrimary(req.isPrimary());
                    MediaEntity saved = mediaRepository.save(entity);

                    if (req.isPrimary()) {
                        mediaRepository.findByTypeAndReferenceId(req.getType(), req.getReferenceId())
                                .stream()
                                .filter(m -> !m.getId().equals(saved.getId()) && m.isPrimary())
                                .forEach(m -> {
                                    m.setPrimary(false);
                                    mediaRepository.save(m);
                                });
                    }
                    return toResponse(saved);
                })
                .collect(Collectors.toList());

        return ApiResponse.buildSuccessResponse(responses);
    }

    @Override
    public ApiResponse<List<MediaResponse>> list(Constants.MediaType type, Long referenceId) {
        List<MediaResponse> result = mediaRepository.findByTypeAndReferenceId(type, referenceId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.buildSuccessResponse(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<Void> makePrimary(Long mediaId) {
        MediaEntity e = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy media với id: " + mediaId));

        mediaRepository.findByTypeAndReferenceId(e.getType(), e.getReferenceId())
                .forEach(m -> {
                    boolean newVal = m.getId().equals(mediaId);
                    if (m.isPrimary() != newVal) {
                        m.setPrimary(newVal);
                        mediaRepository.save(m);
                    }
                });
        return ApiResponse.buildSuccessResponse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<Void> delete(Long mediaId) {
        mediaRepository.findById(mediaId).ifPresent(mediaRepository::delete);
        return ApiResponse.buildSuccessResponse(null);
    }
}
