package com.example.MPMT.service;

import com.github.javafaker.Faker;
import com.example.MPMT.model.*;
import com.example.MPMT.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DataGeneratorService {

    private final UsersRepository userRepository;
    private final ProjectsRepository projectRepository;
    private final ProjectRoleRepository projectRoleRepository;
    private final TaskRepository taskRepository;
    private final HistoryRepository historyRepository;
    private final Faker faker = new Faker(new Locale("fr"));

    public DataGeneratorService(UsersRepository userRepository, ProjectsRepository projectRepository,
            ProjectRoleRepository projectRoleRepository, TaskRepository taskRepository,
            HistoryRepository historyRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.projectRoleRepository = projectRoleRepository;
        this.taskRepository = taskRepository;
        this.historyRepository = historyRepository;
    }

    // Générer un username
    private String generateUsername() {
        return faker.name().firstName().toLowerCase() + "." + faker.name().lastName().toLowerCase();
    }

    // Générer un e-mail basé sur le username
    private String generateEmail(String username) {
        return username + "@example.com";
    }

    public void generateFakeData() {

        // Générer des utilisateurs
        for (int i = 0; i < 10; i++) {
            Users users = new Users();
            String username = generateUsername();
            users.setUsername(username);
            users.setEmail(generateEmail(username));
            users.setPassword(faker.internet().password());
            userRepository.save(users);
        }

        // Générer des projets
        List<Users> users = userRepository.findAll();
        for (int i = 0; i < 5; i++) {
            Projects project = new Projects();
            project.setName(faker.app().name());
            project.setDescription(faker.lorem().paragraph());
            project.setCreatedAt(LocalDateTime.now());

            // Sélectionner un utilisateur aléatoire comme créateur du projet
            Users createdBy = users.get(faker.random().nextInt(users.size()));
            project.setCreatedBy(createdBy);

            // Sauvegarder le projet
            Projects savedProject = projectRepository.save(project);

            // Ajouter l'utilisateur en tant qu'ADMIN par défaut
            ProjectRole projectRole = new ProjectRole(savedProject, createdBy, ProjectRole.Role.ADMIN);
            projectRoleRepository.save(projectRole);
        }

        // Générer des tâches
        List<Projects> projects = projectRepository.findAll();
        for (int i = 0; i < 20; i++) {
            Task task = new Task();
            task.setName(faker.lorem().sentence());
            task.setDescription(faker.lorem().paragraph());
            task.setPriority(Task.Priority.values()[faker.random().nextInt(Task.Priority.values().length)]);
            task.setStatus(Task.Status.values()[faker.random().nextInt(Task.Status.values().length)]);
            task.setEndDate(faker.date().future(30, TimeUnit.DAYS));
            task.setCreatedAt(new Date());

            // Sélectionner un projet aléatoire
            Projects project = projects.get(faker.random().nextInt(projects.size()));
            task.setProjects(project);

            // Récupérer les membres du projet
            List<ProjectRole> projectRoles = projectRoleRepository.findByProject(project);
            List<Users> projectMembers = projectRoles.stream()
                    .map(ProjectRole::getUser)
                    .collect(Collectors.toList());

            // Assigner un membre du projet à la tâche
            if (!projectMembers.isEmpty()) {
                Users assignee = projectMembers.get(faker.random().nextInt(projectMembers.size()));
                task.setAssignee(assignee);
            }

            // Sélectionner un créateur parmi les membres du projet
            if (!projectMembers.isEmpty()) {
                Users createdBy = projectMembers.get(faker.random().nextInt(projectMembers.size()));
                task.setCreatedBy(createdBy);
            }

            taskRepository.save(task);
        }

        // Générer des historiques
        List<Task> tasks = taskRepository.findAll();
        for (int i = 0; i < 50; i++) {
            History history = new History();
            Task task = tasks.get(faker.random().nextInt(tasks.size()));

            // Choisir un champ à modifier (status ou priority)
            String changedField = faker.options().option("status", "priority");

            // Générer des valeurs pour oldValue et newValue
            if ("status".equals(changedField)) {
                history.setChangedField("status");
                history.setOldValue(faker.options().option(Task.Status.class).toString());
                history.setNewValue(faker.options().option(Task.Status.class).toString());
            } else if ("priority".equals(changedField)) {
                history.setChangedField("priority");
                history.setOldValue(faker.options().option(Task.Priority.class).toString());
                history.setNewValue(faker.options().option(Task.Priority.class).toString());
            }

            history.setUpdatedAt(LocalDateTime.now());
            history.setTask(task);
            history.setUsers(task.getAssignee());
            historyRepository.save(history);
        }
    }
}