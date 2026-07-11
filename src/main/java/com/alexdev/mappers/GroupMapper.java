package com.alexdev.mappers;

import com.alexdev.dtos.request.group.GroupCreateDTO;
import com.alexdev.dtos.response.group.GroupDetailsDTO;
import com.alexdev.domain.entities.Group;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    Group groupRequestDTOToGroupEntity(GroupCreateDTO groupCreateDTO);

    GroupDetailsDTO groupEntityToGroupDetailsDTO(Group group);

    List<Group> groupRequestDTOToGroupEntity(List<GroupCreateDTO> groupsRequestDTO);

    List<GroupDetailsDTO> groupEntityListToGroupDetailsDTOList(List<Group> groups);
}
