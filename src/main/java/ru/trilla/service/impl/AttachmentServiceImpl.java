package ru.trilla.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.trilla.dto.AttachmentDto;
import ru.trilla.entity.Attachment;
import ru.trilla.exception.TrillaException;
import ru.trilla.mapper.AttachmentMapper;
import ru.trilla.repository.AttachmentRepository;
import ru.trilla.repository.TaskRepository;
import ru.trilla.security.TrillaAuthentication;
import ru.trilla.service.AttachmentService;
import ru.trilla.service.FileLoadingService;
import ru.trilla.service.UserAccessAuthorizer;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private static final String URL_PATTERN = "/attachments/%s";
    private final AttachmentRepository repository;
    private final FileLoadingService fileLoadingService;
    private final UserAccessAuthorizer userAccessAuthorizer;
    private final TaskRepository taskRepository;
    private final AttachmentMapper mapper;

    @Override
    @Transactional
    public AttachmentDto upload(MultipartFile multipartFile, UUID taskId, TrillaAuthentication authentication) {
        final var task = taskRepository.findById(taskId).orElseThrow();
        userAccessAuthorizer.checkAccess(authentication.id(), task);

        try {
            final var url = String.format(URL_PATTERN, UUID.randomUUID());
            fileLoadingService.uploadFile(url, multipartFile.getInputStream());
            final var attachment = Attachment.builder()
                    .name(multipartFile.getName())
                    .url(url)
                    .build();
            return mapper.toDto(repository.save(attachment));
        } catch (IOException e) {
            throw new TrillaException(e);
        }
    }

    @Override
    public Resource download(UUID id, TrillaAuthentication authentication) {
        final var attachment = repository.findById(id).orElseThrow();
        userAccessAuthorizer.checkAccess(attachment.getId(), attachment.getTask());
        return new InputStreamResource(fileLoadingService.downloadFile(attachment.getUrl()));
    }

    @Override
    public void delete(UUID id, TrillaAuthentication authentication) {
        final var attachment = repository.findById(id).orElseThrow();
        userAccessAuthorizer.checkAccess(attachment.getId(), attachment.getTask());
        fileLoadingService.deleteFile(attachment.getUrl());
    }
}
