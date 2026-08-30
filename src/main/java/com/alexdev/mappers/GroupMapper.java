package com.alexdev.mappers;

import com.alexdev.dtos.request.group.GroupCreateDTO;
import com.alexdev.dtos.response.group.GroupDetailsDTO;
import com.alexdev.domain.group.Group;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    Group groupCreateDTOToGroupEntity(GroupCreateDTO groupCreateDTO);

    GroupDetailsDTO groupEntityToGroupDetailsDTO(Group group);

    List<Group> groupCreateDTOToGroupEntity(List<GroupCreateDTO> groups);

    List<GroupDetailsDTO> groupEntityListToGroupDetailsDTOList(List<Group> groups);
}
