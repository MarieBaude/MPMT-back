package com.example.MPMT.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.MPMT.model.Projects;
import com.example.MPMT.dto.ProjectCreationDTO;
import com.example.MPMT.dto.AssignRoleDTO;
import com.example.MPMT.service.ProjectsService;


@RestController
@RequestMapping("/api/projects")
public class ProjectsController {

    private final ProjectsService projectService;

    public ProjectsController(ProjectsService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/create")
    public ResponseEntity<Projects> createProject(@RequestBody ProjectCreationDTO dto) {
        Projects project = projectService.createProject(dto);
        return ResponseEntity.ok(project);
    }

    @PostMapping("/assign-role")
    public ResponseEntity<String> assignRole(@RequestBody AssignRoleDTO dto) {
        projectService.assignRole(dto);
        return ResponseEntity.ok("Rôle assigné avec succès");
    }
}
