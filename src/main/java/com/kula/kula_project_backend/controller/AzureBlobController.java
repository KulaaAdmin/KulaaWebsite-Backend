package com.kula.kula_project_backend.controller;

import com.kula.kula_project_backend.common.Constant;
import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.service.impl.AzureBlobServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;
/**
 * Controller for handling file operations with Azure Blob Storage.
 * This controller is used to upload and download files from Azure Blob Storage.
 * The controller uses the AzureBlobServiceImpl to interact with Azure Blob Storage.
 * The controller uses the ResponseResult to return the result of the operation.
 * The controller uses the MultipartFile to handle file uploads.
 * The controller uses the UUID to generate unique file names.
 */
@RestController
@RequestMapping("/files")
public class AzureBlobController {

    @Autowired
    private AzureBlobServiceImpl blobStorageService;
    /**
     * Uploads a file to Azure Blob Storage.
     * @param file The file to be uploaded.
     * @return ResponseResult object containing status and message of the operation.
     */
    @PostMapping("/upload")
    public ResponseResult uploadFile(@RequestBody MultipartFile file) {
        return blobStorageService.uploadFile(file);
    }
    /**
     * Uploads multiple files to Azure Blob Storage.
     * @param files The array of files to be uploaded.
     * @return ResponseResult object containing status and message of the operation.
     */
    @PostMapping("/uploadMultiple")
    public ResponseResult uploadMultipleFiles(@RequestBody MultipartFile[] files) {
        return blobStorageService.uploadMultipleFiles(files);
    }
    /**
     * Downloads a file from Azure Blob Storage.
     * @param fileName The name of the file to be downloaded.
     * @return ResponseEntity containing the downloaded file data.
     */
    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        ResponseResult responseResult = blobStorageService.downloadFile(fileName);
        if (responseResult.getCode() == 200 && responseResult.getData() != null) {
            Map<String, Object> map = (Map<String, Object>) responseResult.getData();
            byte[] data = (byte[]) map.get("data");
            MediaType mediaType = MediaType.parseMediaType(map.get("mediaType").toString());
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(data);
        } else {
            return ResponseEntity.status(HttpStatus.valueOf(responseResult.getCode())).body(null);
        }
    }


    // This image uploading method is just temporary, it will be replaced by the AzureBlobController methods
    /**
     * Uploads an image to the server.
     * @param request The HttpServletRequest object.
     * @param file The image file to be uploaded.
     * @return ResponseResult object containing status and message of the operation.
     */
    @PostMapping("/updateImageToServer")
    public ResponseResult updateImageToServer(HttpServletRequest request, @RequestParam("file") MultipartFile file){
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        String newFileName = fileName;
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        if (!fileDirectory.exists()) {
            fileDirectory.mkdirs();
        }
        try{
            file.transferTo(destFile);
            return new ResponseResult(200, "success", getURI(new URI(request.getRequestURL().toString())).toString() + "/images/" + newFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseResult(400, "fail");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Retrieves the URI of the server.
     * @param uri The URI of the request.
     * @return The URI of the server.
     */
    private URI getURI(URI uri) {
        URI effectiveURI;
        try{
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
        } catch (Exception e) {
            effectiveURI = null;
        }
        return effectiveURI;

    }
    /**
     * Retrieves the URI of an image from the server.
     * @param request The HttpServletRequest object.
     * @param fileName The name of the image file.
     * @return ResponseResult object containing status, message and the image URI.
     */
    @GetMapping("/getImageURIFromServer/{fileName}")
    public ResponseResult getImageURIFromServer(HttpServletRequest request, @PathVariable String fileName) {
        try{
            return new ResponseResult(200, "success", getURI(new URI(request.getRequestURL().toString())).toString() + "/images/" + fileName);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

}
