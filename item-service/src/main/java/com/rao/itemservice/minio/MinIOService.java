package com.rao.itemservice.minio;

import com.rao.itemservice.MinIOProperties;
import com.rao.itemservice.MinioClientPool;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@Service
public class MinIOService {

    private final MinioClientPool minioClientPool;

    private final MinIOProperties minIOProperties;

    @Autowired
    public MinIOService(MinIOProperties minIOProperties) {
        this.minIOProperties = minIOProperties;
        this.minioClientPool = new MinioClientPool(minIOProperties.getEndpoint(), minIOProperties.getAccessKeyId(), minIOProperties.getAccessKeySecret());
    }


    public void storePic(MultipartFile file) {
        if (file.getContentType() == null || !file.getContentType().startsWith("image/"))
//            throw new DataTypeException();
            throw new RuntimeException();

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
            throw new RuntimeException();
        } finally {
            minioClientPool.returnClient(minioClient);
        }
    }
}
