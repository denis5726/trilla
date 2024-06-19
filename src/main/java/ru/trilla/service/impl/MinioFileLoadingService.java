package ru.trilla.service.impl;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.DeleteObject;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.trilla.exception.TrillaMinioException;
import ru.trilla.service.FileLoadingService;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinioFileLoadingService implements FileLoadingService {
    private static final String BUCKET_NAME = "default";
    private final MinioClient minioClient;

    @PostConstruct
    void init() {
        callMinio(() -> {
            if (!minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(BUCKET_NAME)
                            .build()
            )) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(BUCKET_NAME)
                                .build()
                );
            }
            return null;
        });
    }

    @Override
    public void uploadFile(String path, InputStream stream) {
        callMinio(() ->
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(BUCKET_NAME)
                                .object(path)
                                .stream(stream, 0, -1)
                                .build()
                )
        );
    }

    @Override
    public InputStream downloadFile(String path) {
        return callMinio(() ->
                minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(BUCKET_NAME)
                                .object(path)
                                .build()
                )
        );
    }

    @Override
    public void deleteFile(String path) {
        callMinio(() ->
                minioClient.removeObjects(
                        RemoveObjectsArgs.builder()
                                .bucket(BUCKET_NAME)
                                .objects(Collections.singleton(new DeleteObject(path)))
                                .build()
                )
        );
    }

    private <T> T callMinio(MinioExecutable<T> executable) {
        try {
            return executable.execute();
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            throw new TrillaMinioException("Exception while requesting file server", e);
        }
    }

    @FunctionalInterface
    private interface MinioExecutable<T> {

        T execute() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
    }
}
