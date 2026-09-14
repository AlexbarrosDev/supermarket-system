package com.alexdev.services;

import com.alexdev.domain.group.Group;
import com.alexdev.dtos.request.group.GroupCreateDTO;
import com.alexdev.dtos.request.group.GroupUpdateDTO;
import com.alexdev.dtos.response.group.GroupDetailsDTO;
import com.alexdev.exceptions.BusinessException;
import com.alexdev.exceptions.ResourceNotFoundException;
import com.alexdev.mappers.GroupMapper;
import com.alexdev.repositories.GroupRepository;
import com.alexdev.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private GroupMapper groupMapper;

    @InjectMocks
    GroupService groupService;

    @Test
    void shouldReturnGroupById() {

        // Arrange
        Group group = createGroup("Group");

        GroupDetailsDTO groupDetailsDTO = new GroupDetailsDTO(1L, "Group");

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(groupMapper.groupEntityToGroupDetailsDTO(group))
                .thenReturn(groupDetailsDTO);

        // Act
        GroupDetailsDTO result = groupService.findGroupById(1L);

        // Assert
        assertEquals(1L, result.id());
        assertEquals("Group", result.name());

        verify(groupRepository, times(1)).findById(1L);
        verify(groupMapper, times(1))
                .groupEntityToGroupDetailsDTO(group);
    }

    @Test
    void shouldThrowExceptionWhenGroupNotFound() {

        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                ()  -> groupService.findGroupById(1L));

        verify(groupRepository, times(1))
                .findById(anyLong());
        verify(groupMapper, never())
                .groupEntityToGroupDetailsDTO(any());
    }

    //
    @Test
    void shouldReturnAllGroups() {

        // Arrange
        List<Group> groups = List.of(createGroup("Group"));

        List<GroupDetailsDTO> groupDetailsDTOs = List.of(new GroupDetailsDTO(1L, "Group"));

        when(groupRepository.findAll()).thenReturn(groups);
        when(groupMapper.groupEntityListToGroupDetailsDTOList(groups))
                .thenReturn(groupDetailsDTOs);

        // Act
        List<GroupDetailsDTO> result = groupService.findAllGroups();

        // Assert
        assertEquals(groups.size(), result.size());
        assertEquals(1L, result.getFirst().id());
        assertEquals("Group", result.getFirst().name());

        verify(groupRepository, times(1)).findAll();
        verify(groupMapper, times(1))
                .groupEntityListToGroupDetailsDTOList(groups);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenGroupListIsEmpty() {

        when(groupRepository.findAll()).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class,
                () -> groupService.findAllGroups());

        verify(groupRepository, times(1)).findAll();
        verify(groupMapper, never()).groupEntityListToGroupDetailsDTOList(any());
    }

    //
    @Test
    void shouldCreateGroup() {

        // Assert
        GroupCreateDTO groupCreateDTO = new GroupCreateDTO("Group");

        Group group = createGroup("Group");

        GroupDetailsDTO groupDetails = new GroupDetailsDTO(1L, "Group");

        when(groupRepository.existsByName("Group")).thenReturn(false);
        when(groupMapper.groupCreateDTOToGroupEntity(groupCreateDTO))
                .thenReturn(group);
        when(groupRepository.save(group)).thenReturn(group);
        when(groupMapper.groupEntityToGroupDetailsDTO(group))
                .thenReturn(groupDetails);

        // Act
        GroupDetailsDTO result = groupService.createGroup(groupCreateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Group", result.name());

        verify(groupRepository).existsByName(groupCreateDTO.name());
        verify(groupRepository, times(1)).save(group);
        verify(groupMapper, times(1))
                .groupCreateDTOToGroupEntity(groupCreateDTO);
        verify(groupMapper, times(1))
                .groupEntityToGroupDetailsDTO(group);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenGroupDoesNotExist() {

        GroupCreateDTO groupCreateDTO = new GroupCreateDTO("Group");

        when(groupRepository.existsByName("Group")).thenReturn(true);

        assertThrows(BusinessException.class,
                () -> groupService.createGroup(groupCreateDTO));

        verify(groupRepository, times(1)).existsByName("Group");
        verify(groupMapper, never()).groupCreateDTOToGroupEntity(groupCreateDTO);
        verify(groupRepository, never()).save(any());
        verify(groupMapper, never()).groupEntityToGroupDetailsDTO(any());
    }

    //
    @Test
    void ShouldUpdateGroup() {

        // Arrange
        GroupUpdateDTO groupUpdateDTO = new GroupUpdateDTO("Group1");

        Group group = createGroup("Group");

        GroupDetailsDTO groupDetailsDTO = new GroupDetailsDTO(1L, "Group1");

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(groupRepository.existsByNameAndIdNot(groupUpdateDTO.name(), 1L))
                .thenReturn(false);
        when(groupMapper.groupEntityToGroupDetailsDTO(group))
                .thenReturn(groupDetailsDTO);

        // Act
        GroupDetailsDTO result = groupService.updateGroup(1L, groupUpdateDTO);

        // Assert
        assertEquals("Group1", group.getName());
        assertEquals("Group1", result.name());
        assertEquals(1L, result.id());

        verify(groupRepository).findById(1L);
        verify(groupRepository).existsByNameAndIdNot(groupUpdateDTO.name(), 1L);
        verify(groupMapper).groupEntityToGroupDetailsDTO(group);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistingGroup() {

        GroupUpdateDTO groupUpdateDTO = new GroupUpdateDTO("Group");

        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> groupService.updateGroup(1L, groupUpdateDTO));

        verify(groupRepository, times(1)).findById(1L);
        verify(groupRepository, never()).existsByNameAndIdNot(anyString(), anyLong());
        verify(groupMapper, never()).groupEntityToGroupDetailsDTO(any());
    }

    @Test
    void shouldThrowBusinessExceptionWhenGroupNameAlreadyExists() {

        Group group = createGroup("Group");

        GroupUpdateDTO groupUpdateDTO = new GroupUpdateDTO("Group1");

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(groupRepository.existsByNameAndIdNot(groupUpdateDTO.name(), 1L)).thenReturn(true);

        assertThrows(BusinessException.class,
                () -> groupService.updateGroup(1L, groupUpdateDTO));

        assertEquals("Group", group.getName());

        verify(groupRepository, times(1)).findById(1L);
        verify(groupRepository).existsByNameAndIdNot(groupUpdateDTO.name(), 1L);
        verify(groupMapper, never()).groupEntityToGroupDetailsDTO(any());
    }

    //
    @Test
    void shouldDeleteGroupById() {

        // Arrange
        Group group = createGroup("Group");

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(productRepository.existsByGroupId(1L)).thenReturn(false);

        // Act
        groupService.deleteGroupById(1L);

        // Assert
        verify(groupRepository).findById(1L);
        verify(productRepository).existsByGroupId(1L);
        verify(groupRepository).delete(group);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistingGroup() {

        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> groupService.deleteGroupById(1L));

        verify(groupRepository, times(1)).findById(1L);
        verify(productRepository, never()).existsByGroupId(anyLong());
        verify(groupRepository, never()).delete(any());
    }

    @Test
    void shouldTrowBusinessExceptionWhenGroupIdContainsProducts() {

        Group group = createGroup("Group");

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(productRepository.existsByGroupId(1L)).thenReturn(true);

        assertThrows(BusinessException.class,
                () -> groupService.deleteGroupById(1L));

        verify(groupRepository, times(1)).findById(1L);
        verify(productRepository).existsByGroupId(1L);
        verify(groupRepository, never()).delete(any());
    }

    private Group createGroup(String name) {

        Group group = new Group();
        group.setId(1L);
        group.setName(name);

        return group;
    }
}