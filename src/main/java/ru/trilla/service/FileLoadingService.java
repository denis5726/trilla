package ru.trilla.service;

import java.io.InputStream;

public interface FileLoadingService {

    void uploadFile(String path, InputStream stream);

    InputStream downloadFile(String path);

    void deleteFile(String path);
}
