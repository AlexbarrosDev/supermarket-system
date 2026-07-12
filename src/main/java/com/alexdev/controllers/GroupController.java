package com.alexdev.controllers;

import com.alexdev.dtos.request.group.GroupCreateDTO;
import com.alexdev.dtos.request.group.GroupUpdateDTO;
import com.alexdev.dtos.response.group.GroupDetailsDTO;
import com.alexdev.services.GroupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
@Tag(
        name = "Groups",
        description = "Operations related to groups"
)
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<List<GroupDetailsDTO>> findAllGroups() {

        return ResponseEntity.ok(groupService.findAllGroups());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDetailsDTO> findGroupById(
            @Valid @PathVariable Long id) {

        return ResponseEntity.ok(groupService.findGroupById(id));
    }

    @PostMapping
    public ResponseEntity<GroupDetailsDTO> createGroup(
            @Valid @RequestBody GroupCreateDTO groupCreateDTO) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(groupService.createGroup(groupCreateDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupDetailsDTO> updateGroup(
            @Valid @PathVariable Long id, @Valid @RequestBody GroupUpdateDTO groupUpdateDTO)
    {

        return ResponseEntity.ok(groupService.updateGroup(id, groupUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroupById(@PathVariable Long id) {

        groupService.deleteGroupById(id);
        return ResponseEntity.noContent().build();
    }
}
