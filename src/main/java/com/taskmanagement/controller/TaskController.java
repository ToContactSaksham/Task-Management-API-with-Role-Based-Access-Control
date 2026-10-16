package com.taskmanagement.controller;

import com.taskmanagement.dto.TaskRequest;
import com.taskmanagement.dto.TaskResponse;
import com.taskmanagement.model.User;
import com.taskmanagement.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks")
public class TaskController {
    private final TaskService taskService;
    public TaskController(TaskService taskService) { this.taskService = taskService; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) @Operation(summary = "Create a task")
    public TaskResponse create(@Valid @RequestBody TaskRequest request, @AuthenticationPrincipal User user) { return taskService.create(request, user); }
    @GetMapping @Operation(summary = "List tasks visible to the current user")
    public List<TaskResponse> list(@AuthenticationPrincipal User user) { return taskService.list(user); }
    @GetMapping("/{id}") public TaskResponse get(@PathVariable Long id, @AuthenticationPrincipal User user) { return taskService.get(id, user); }
    @PutMapping("/{id}") public TaskResponse update(@PathVariable Long id, @Valid @RequestBody TaskRequest request, @AuthenticationPrincipal User user) { return taskService.update(id, request, user); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id, @AuthenticationPrincipal User user) { taskService.delete(id, user); }
}
