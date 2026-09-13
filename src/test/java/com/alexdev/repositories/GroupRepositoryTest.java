package com.alexdev.repositories;

import com.alexdev.domain.group.Group;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class GroupRepositoryTest { // finalizamos aqui, próximo passo é implementar ClientRepositoryTest

    @Autowired
    GroupRepository groupRepository;

    @Test
    void shouldReturnTrueWhenGroupExistsByName() {

        // Arrange
        Group group = createGroup();

        groupRepository.save(group);

        // Act
        boolean result = groupRepository.existsByName("Group1");

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenGroupNameDoesNotExist() {

        // Act
        boolean result = groupRepository.existsByName("Group");

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWheGroupExistsByNameAndIdIsTheSame() {

        Group group = createGroup();

        Group savedGroup = groupRepository.save(group);

        boolean result = groupRepository
                .existsByNameAndIdNot("Group1", savedGroup.getId());

        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenGroupNameExistsAndIdDifferent() {

        Group group = createGroup();

        Long anotherId = Long.MAX_VALUE;

        groupRepository.save(group);

        boolean result = groupRepository
                .existsByNameAndIdNot("Group1", anotherId);

        assertTrue(result);
    }

    private Group createGroup() {

        Group group = new Group();
        group.setName("Group1");
        return group;
    }
}