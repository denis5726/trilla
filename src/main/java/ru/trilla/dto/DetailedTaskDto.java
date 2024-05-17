package ru.trilla.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DetailedTaskDto(
        UUID id,
        String code,
        String summary,
        String description,
        RelatedUserDto assigneeInfo,
        RelatedUserDto creatorInfo,
        UUID projectId,
        String type,
        String status,
        List<AdditionalFieldDto> additionalFields,
        List<AttachmentDto> attachments,
        List<LinkDto> links,
        List<CommentDto> comments,
        List<TagDto> tags,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
