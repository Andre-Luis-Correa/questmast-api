package com.questmast.questmast.core.google.service;

import com.google.cloud.storage.*;
import com.questmast.questmast.common.exception.type.GoogleCloudException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.UUID;

@Log4j2
@Service
public class GoogleStorageService {

    private Storage storage;

    @Value("${gcp.bucket-id}")
    private String bucketId;

    @Value("${gcp.dir}")
    private String gcpDirectoryName;

    public GoogleStorageService() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    public String uploadImage(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String uniqueFileName = gcpDirectoryName + "/" + UUID.randomUUID() + "-" + fileName;

            BlobId blobId = BlobId.of(bucketId, uniqueFileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

            storage.create(blobInfo, file.getBytes());

            return uniqueFileName;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new GoogleCloudException("Não foi possível fazer o upload da imagem no Google Storage.");
        }
    }

    public String encodeImageToBase64(String imageName) {
        log.info("Erro imagem: " + imageName);
        Blob blob = storage.get(BlobId.of(bucketId, imageName));
        if (blob == null || !blob.exists()) {
            throw new GoogleCloudException("Imagem não encontrada no bucket: " + imageName);
        }

        byte[] fileContent = blob.getContent();
        String mimeType = blob.getContentType();

        return "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(fileContent);
    }

    public String convertMultipartFileToBase64(MultipartFile file) {
        try {
            byte[] fileBytes = file.getBytes();
            String mimeType = file.getContentType();
            String base64Content = Base64.getEncoder().encodeToString(fileBytes);

            return "data:" + mimeType + ";base64," + base64Content;
        } catch (Exception e) {
            throw new GoogleCloudException("Não foi possível converter a imagem para base64.");
        }
    }

    public MultipartFile convertBase64ToMultipartFile(String base64Image, String originalFileName) {
        try {
            String[] parts = base64Image.split(",");
            String imageData = parts.length > 1 ? parts[1] : parts[0];

            byte[] imageBytes = Base64.getDecoder().decode(imageData);

            String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;

            return new MockMultipartFile(uniqueFileName, uniqueFileName, "image/png", new ByteArrayInputStream(imageBytes));
        } catch (Exception e) {
            throw new GoogleCloudException("Não foi possível converter a imagem de base64 para Multipartfile.");
        }
    }

    public void removeOldImage(String imageName) {
        if (imageName != null && !imageName.isBlank()) {
            try {
                log.info("Deletando imagem: " + imageName);
                BlobId blobId = BlobId.of(bucketId, imageName);

                boolean deleted = storage.delete(blobId);

                if (!deleted) {
                    throw new GoogleCloudException("Erro ao remover a imagem antiga " + imageName);
                }
            } catch (StorageException e) {
                log.error(e.getMessage());
                throw new GoogleCloudException("Erro ao remover a imagem antiga " + imageName);
            }
        }
    }
}
