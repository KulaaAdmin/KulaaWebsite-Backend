package com.kula.kula_project_backend.config;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
/**
 * AzureBlobConfig is a configuration class that provides a BlobServiceClient bean for Azure Blob Storage.
 */
@Configuration
public class AzureBlobConfig {

    @Value("${azure.storage.connection-string}")
    private String connectionString;
    /**
     * Creates a BlobServiceClient bean for Azure Blob Storage.
     * @return A BlobServiceClient instance.
     */
    @Bean
    public BlobServiceClient blobServiceClient() {
        return new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }

}
