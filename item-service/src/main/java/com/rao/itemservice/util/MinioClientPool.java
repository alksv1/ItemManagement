package com.rao.itemservice.util;

import io.minio.MinioClient;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class MinioClientPool {

    private final GenericObjectPool<MinioClient> pool;

    public MinioClientPool(String endpoint, String accessKey, String secretKey) {
        MinioClientFactory minioClientFactory = new MinioClientFactory(endpoint, accessKey, secretKey);
        pool = new GenericObjectPool<>(minioClientFactory);
    }

    // 借用 MinioClient
    public MinioClient borrowClient() throws Exception {
        return pool.borrowObject();
    }

    // 归还 MinioClient
    public void returnClient(MinioClient client) {
        if (client != null) {
            pool.returnObject(client);
        }
    }

    // 关闭池中的所有客户端
    public void close() {
        try {
            pool.close();
        } catch (Exception e) {
            e.printStackTrace(); // 处理关闭异常
        }
    }

    // 获取池的状态信息
    public String getPoolStats() {
        return String.format("Active: %d, Idle: %d, Total: %d",
                pool.getNumActive(), pool.getNumIdle(), pool.getMaxTotal());
    }
}

class MinioClientFactory extends BasePooledObjectFactory<MinioClient> {
    private final String endpoint;
    private final String accessKey;
    private final String secretKey;

    public MinioClientFactory(String endpoint, String accessKey, String secretKey) {
        this.endpoint = endpoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    @Override
    public MinioClient create() throws Exception {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Override
    public PooledObject<MinioClient> wrap(MinioClient client) {
        return new DefaultPooledObject<>(client);
    }

    @Override
    public void destroyObject(PooledObject<MinioClient> p) throws Exception {
//        p.getObject().close();
        super.destroyObject(p);
    }
}