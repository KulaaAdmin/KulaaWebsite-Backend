package com.kula.kula_project_backend.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.service.IAzureBlobService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.UUID;




/**
 * Service implementation for Azure Blob Storage operations.
 * This service is used to upload and download files to and from Azure Blob Storage.
 * The service uses the Azure Blob Storage SDK for Java.
 * The service uses the BlobServiceClient to interact with the Azure Blob Storage service.
 * The service uses the BlobContainerClient to interact with a specific container.
 * The service uses the BlobClient to interact with a specific blob.
 */
@Service
public class AzureBlobServiceImpl implements IAzureBlobService {
    // BlobServiceClient is the main interface for interacting with the Azure Blob Storage service.
    @Autowired
    private BlobServiceClient blobServiceClient;
    // The container name is used to create a container client
    @Value("${azure.blob.container-name}")
    private String containerName;
    // The endpoint is used to create a blob client
    @Value("${azure.blob.container-endpoint}")
    private String endPoint;

    /**
     * Uploads a file to Azure Blob Storage.
     * @param file The file to be uploaded.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult uploadFile(MultipartFile file) {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileExtension;
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        try (InputStream inputStream = file.getInputStream()) {
            blobClient.upload(inputStream, file.getSize(), true);
            return new ResponseResult(200, "success", fileName);
        } catch (Exception e) {
            return new ResponseResult(400, "fail");
        }

    }
    /**
     * Uploads multiple files to Azure Blob Storage.
     * @param files The array of files to be uploaded.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult uploadMultipleFiles(MultipartFile[] files) {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        List<String> tempBlobNames = new ArrayList<>();

        try {
            // Upload each file to a temporary blob
            for (MultipartFile file : files) {
                String newFileName = UUID.randomUUID().toString() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                String tempBlobName = "temp_" + newFileName;
                BlobClient blobClient = containerClient.getBlobClient(tempBlobName);
                try (InputStream inputStream = file.getInputStream()) {
                    blobClient.upload(inputStream, file.getSize(), true);
                    tempBlobNames.add(tempBlobName);  // Track uploaded temp blobs for cleanup or moving
                }
            }
            List<String> finalBlobNames = new ArrayList<>();
            // Move blobs from temporary to final destination

            for (String tempBlobName : tempBlobNames) {
                String finalBlobName = tempBlobName.substring(5);  // Remove "temp_" prefix
                // Original approach was to return the image source url:
                // finalBlobNames.add(endPoint + "/" + finalBlobName);
                // Modified to be consistency with upload single file:
                //      return ["filename.extension"]
                finalBlobNames.add(finalBlobName);
                BlobClient tempBlobClient = containerClient.getBlobClient(tempBlobName);
                BlobClient finalBlobClient = containerClient.getBlobClient(finalBlobName);
                finalBlobClient.beginCopy(tempBlobClient.getBlobUrl(), null); // Copy to final location
                tempBlobClient.delete(); // Delete temporary blob after copying

            }
            return new ResponseResult(200, "success", finalBlobNames);
        } catch (Exception e) {
            // Cleanup any temporary blobs in case of failure
            for (String tempBlobName : tempBlobNames) {
                try {
                    containerClient.getBlobClient(tempBlobName).delete();
                } catch (Exception cleanupException) {
                    // Log cleanup failure
                }
            }
            return new ResponseResult(400, "fail", e.getMessage());
        }


    }

    /**
     * Downloads a file from Azure Blob Storage.
     * @param fileName The name of the file to be downloaded.
     * @return ResponseResult object containing status, message and the downloaded file data.
     */
    @Override
    public ResponseResult downloadFile(String fileName) {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            blobClient.download(outputStream);
            byte[] data = outputStream.toByteArray();
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));
            MediaType mediaType = getMediaTypeForFileExtension(fileExtension);
            Map<String, Object> dataJson = new HashMap<String, Object>();
            dataJson.put("data", data);
            dataJson.put("mediaType", mediaType);
            return new ResponseResult(200, "success", dataJson);
        } catch (Exception e) {
            return new ResponseResult(400, "fail");
        }
    }

    /**
     * Delete file (mainly image) stored on azure
     * @param filename: single filename string.
     */
    public void deleteFileFromAzure(String filename){
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(filename);
        if (blobClient.exists()) {
            blobClient.delete();
            System.out.println("Blob " + filename + " deleted successfully.");
        }
    }

    /**
     * Determines the MediaType for a given file extension.
     * @param extension The file extension.
     * @return The MediaType corresponding to the file extension.
     */
    private MediaType getMediaTypeForFileExtension(String extension) {
        switch(extension) {
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            default:
                return MediaType.APPLICATION_OCTET_STREAM; // Default, could be improved to handle other types.
        }
    }

}
