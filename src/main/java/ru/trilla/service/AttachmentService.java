package ru.trilla.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.trilla.dto.AttachmentDto;
import ru.trilla.security.TrillaAuthentication;

import java.util.UUID;

public interface AttachmentService {

    AttachmentDto upload(MultipartFile multipartFile, UUID taskId, TrillaAuthentication authentication);

    Resource download(UUID id, TrillaAuthentication authentication);

    void delete(UUID id, TrillaAuthentication authentication);
}
