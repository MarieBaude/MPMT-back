package com.example.MPMT.controller;

import java.util.Optional;
import java.util.List;

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

    @GetMapping("/{id}")
    public Optional<Projects> projectService(@PathVariable Long id) {
        return projectService.getProjectsById(id);
    }

    @PostMapping("/assign-role")
    public ResponseEntity<String> assignRole(@RequestBody AssignRoleDTO dto) {
        projectService.assignRole(dto);
        return ResponseEntity.ok("Rôle assigné avec succès");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Projects>> getProjectsByUserId(@PathVariable Long userId) {
        List<Projects> projects = projectService.getProjectsByUserId(userId);
        return ResponseEntity.ok(projects);
    }
}
