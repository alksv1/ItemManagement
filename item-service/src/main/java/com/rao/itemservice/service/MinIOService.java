package com.rao.itemservice.service;

import com.rao.common.exception.InternalErrorException;
import com.rao.common.exception.ParameterErrorException;
import com.rao.itemservice.util.MinIOProperties;
import com.rao.itemservice.util.MinioClientPool;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MinIOService {

    private final MinioClientPool minioClientPool;

    private final MinIOProperties minIOProperties;

    @Autowired
    public MinIOService(MinIOProperties minIOProperties) {
        this.minIOProperties = minIOProperties;
        this.minioClientPool = new MinioClientPool(minIOProperties.getEndpoint(), minIOProperties.getAccessKeyId(), minIOProperties.getAccessKeySecret());
        try (MinioClient client = minioClientPool.borrowClient()) {
            boolean isExist = client.bucketExists(
                    BucketExistsArgs.builder().bucket(minIOProperties.getBucketName()).build()
            );
            if (!isExist) {
                client.makeBucket(
                        MakeBucketArgs.builder().bucket(minIOProperties.getBucketName()).build()
                );
                log.info("Bucket '{}' created successfully.", minIOProperties.getBucketName());
            } else {
                log.info("Bucket '{}' already exists.", minIOProperties.getBucketName());
            }
            try {
                StatObjectResponse stat = client.statObject(
                        StatObjectArgs.builder().bucket(minIOProperties.getBucketName())
                                .object("default_cover.png")
                                .build()
                );
            } catch (ErrorResponseException e) {
                ClassPathResource resource = new ClassPathResource("/pic/default_cover.png");
                InputStream inputStream = resource.getInputStream();
                try (MinioClient minioClient = minioClientPool.borrowClient()) {
                    minioClient.putObject(PutObjectArgs.builder()
                            .bucket(minIOProperties.getBucketName())
                            .object(resource.getFilename())
                            .contentType("image/png") // 设置文件的 MIME 类型
                            .stream(inputStream, resource.contentLength(), -1) // 提供输入流和文件大小
                            .build());
                } catch (Exception ex) {
                    throw new InternalErrorException(ex);
                }
            }
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }


    }


    public String storePic(MultipartFile file) {
        if (file.getContentType() == null || !file.getContentType().startsWith("image/"))
            throw new ParameterErrorException();
        try (MinioClient minioClient = minioClientPool.borrowClient()) {
            String newFileName = UUID.randomUUID().toString();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minIOProperties.getBucketName())
                    .object(newFileName)
                    .contentType(file.getContentType()) // 设置文件的 MIME 类型
                    .stream(file.getInputStream(), file.getSize(), -1) // 提供输入流和文件大小
                    .build());
            return newFileName;

        } catch (Exception e) {
            throw new InternalErrorException(e);
        }
    }

    public String getPicURL(String fileName) {
        MinioClient minioClient = null;
        try {
            minioClient = minioClientPool.borrowClient();

            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(minIOProperties.getBucketName())
                            .object(fileName)
                            .expiry(20, TimeUnit.MINUTES)
                            .build());
        } catch (Exception e) {
//            throw new InternalServerErrorException();
            throw new InternalErrorException(e);
        } finally {
            minioClientPool.returnClient(minioClient);
        }
    }

    public void removeFile(String fileName) {
        try (MinioClient client = minioClientPool.borrowClient()) {
            client.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minIOProperties.getBucketName())
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }
    }
}
