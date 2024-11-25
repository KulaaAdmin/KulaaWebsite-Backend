package com.kula.kula_project_backend.service;

import com.kula.kula_project_backend.common.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface IAzureBlobService {
    ResponseResult uploadFile(MultipartFile file);

    ResponseResult uploadMultipleFiles(MultipartFile[] files);

    ResponseResult downloadFile(String fileName);

    void deleteFileFromAzure(String filename);
}
