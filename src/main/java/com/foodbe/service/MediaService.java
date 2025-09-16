package com.foodbe.service;

import com.foodbe.DTO.request.MediaUploadRequest;
import com.foodbe.DTO.response.ApiResponse;
import com.foodbe.DTO.response.MediaResponse;
import com.foodbe.constants.Constants;

import java.util.List;

public interface MediaService {
    ApiResponse<MediaResponse> upload(MediaUploadRequest req);
    ApiResponse<List<MediaResponse>> uploadList(List<MediaUploadRequest> reqList);
    ApiResponse<List<MediaResponse>> list(Constants.MediaType type, Long referenceId);
    ApiResponse<Void> makePrimary(Long mediaId);
    ApiResponse<Void> delete(Long mediaId);
}
