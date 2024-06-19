package ru.trilla.mapper;

import org.mapstruct.Mapper;
import ru.trilla.dto.AttachmentDto;
import ru.trilla.entity.Attachment;

@Mapper
public interface AttachmentMapper {

    AttachmentDto toDto(Attachment attachment);
}
