package ru.trilla.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.trilla.dto.AttachmentDto;
import ru.trilla.security.TrillaAuthentication;
import ru.trilla.service.AttachmentService;

import java.util.UUID;

@RestController
@RequestMapping("/attachments")
@RequiredArgsConstructor
public class AttachmentController {
    private final AttachmentService service;

    @PostMapping("/upload")
    public AttachmentDto upload(MultipartFile file, @RequestParam UUID taskId, TrillaAuthentication authentication) {
        return service.upload(file, taskId, authentication);
    }

    @GetMapping(value = "/{id}/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Resource download(@PathVariable UUID id, TrillaAuthentication authentication) {
        return service.download(id, authentication);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id, TrillaAuthentication authentication) {
        service.delete(id, authentication);
    }
}
