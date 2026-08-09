package com.alexdev.services;

import com.alexdev.domain.entities.Group;
import com.alexdev.dtos.request.group.GroupCreateDTO;
import com.alexdev.dtos.request.group.GroupUpdateDTO;
import com.alexdev.dtos.response.group.GroupDetailsDTO;
import com.alexdev.exceptions.BusinessException;
import com.alexdev.exceptions.ResourceNotFoundException;
import com.alexdev.mappers.GroupMapper;
import com.alexdev.repositories.GroupRepository;
import com.alexdev.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    private final GroupMapper groupMapper;

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public GroupDetailsDTO findGroupById(Long groupId) {

        Group group =  groupRepository
                    .findById(groupId)
                    .orElseThrow(()
                    -> new ResourceNotFoundException("Group not found with id " + groupId));

        return groupMapper.groupEntityToGroupDetailsDTO(group);
    }

    @Transactional(readOnly = true)
    public List<GroupDetailsDTO> findAllGroups() {

        List<Group> groups = groupRepository.findAll();

        if (groups.isEmpty()) {
            throw new ResourceNotFoundException("No groups found");
        }

        return groupMapper.groupEntityListToGroupDetailsDTOList(groups);
    }

    @Transactional
    public GroupDetailsDTO createGroup(GroupCreateDTO groupCreateDTO) {

        validateCreateNameUniqueness(groupCreateDTO.name());

        Group entity = groupMapper
                        .groupCreateDTOToGroupEntity(groupCreateDTO);

        entity = groupRepository.save(entity);

        return groupMapper.groupEntityToGroupDetailsDTO(entity);
    }

    @Transactional
    public GroupDetailsDTO updateGroup(Long id, GroupUpdateDTO groupUpdateDTO) {

        Group group = groupRepository
                .findById(id)
                .orElseThrow(()
                -> new ResourceNotFoundException("Group not found with id " + id));

        if (groupRepository.existsByNameAndIdNot(group.getName(), id)) {
            throw new BusinessException("Group already exists");
        }

        group.setName(groupUpdateDTO.name());
        return groupMapper
                .groupEntityToGroupDetailsDTO(group);
    }

    @Transactional
    public void deleteGroupById(Long groupId) {

        Group group = groupRepository
                .findById(groupId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Group not found with id " + groupId));

        if (productRepository.existsByGroupId(groupId)) {

            throw new BusinessException(
                    "Group cannot be deleted because it is associated with one or more products."
            );
        }

        groupRepository.delete(group);
    }

    private void validateCreateNameUniqueness(String name) {

        if (groupRepository.existsByName(name)) {
            throw new BusinessException("Group already exists.");
        }
    }
}
