package com.foodbe.controller;

import com.foodbe.DTO.request.MediaUploadRequest;
import com.foodbe.DTO.response.ApiResponse;
import com.foodbe.DTO.response.MediaResponse;
import com.foodbe.constants.Constants;
import com.foodbe.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @PostMapping("/upload")
    public ApiResponse<MediaResponse> upload(@RequestBody MediaUploadRequest req) {
        return mediaService.upload(req);
    }

    @PostMapping("/upload/list")
    public ApiResponse<List<MediaResponse>> uploadList(@RequestBody List<MediaUploadRequest> reqList) {
        return mediaService.uploadList(reqList);
    }

    @GetMapping
    public ApiResponse<List<MediaResponse>> list(@RequestParam Constants.MediaType type, @RequestParam Long referenceId) {
        return mediaService.list(type, referenceId);
    }

    @PostMapping("/{id}/primary")
    public ApiResponse<Void> makePrimary(@PathVariable Long id) {
        return mediaService.makePrimary(id);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        return mediaService.delete(id);
    }
}
