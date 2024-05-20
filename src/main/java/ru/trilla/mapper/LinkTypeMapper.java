package ru.trilla.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.trilla.dto.DetailedLinkTypeDto;
import ru.trilla.entity.LinkType;

import java.util.List;

@Mapper
public interface LinkTypeMapper {

    @Mapping(target = "taskTypeInId", source = "in.id")
    @Mapping(target = "taskTypeFromId", source = "from.id")
    DetailedLinkTypeDto toDto(LinkType entity);

    List<DetailedLinkTypeDto> toDtoList(List<LinkType> entities);
}
