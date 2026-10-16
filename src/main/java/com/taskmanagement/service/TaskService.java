package com.taskmanagement.service;

import com.taskmanagement.dto.TaskRequest;
import com.taskmanagement.dto.TaskResponse;
import com.taskmanagement.exception.ResourceNotFoundException;
import com.taskmanagement.model.Role;
import com.taskmanagement.model.Task;
import com.taskmanagement.model.User;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository tasks;
    private final UserRepository users;
    private final EmailNotificationService notifications;
    public TaskService(TaskRepository tasks, UserRepository users, EmailNotificationService notifications) {
        this.tasks = tasks;
        this.users = users;
        this.notifications = notifications;
    }

    @Transactional
    public TaskResponse create(TaskRequest request, User current) {
        User assignee = resolveAssignee(request.assigneeId(), current);
        Task task = new Task();
        apply(task, request, assignee);
        task.setCreatedBy(current);
        Task saved = tasks.save(task);
        if (!saved.getAssignee().getId().equals(current.getId())) notifications.notifyAssignment(saved);
        return TaskResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> list(User current) {
        List<Task> result = current.getRole() == Role.ADMIN ? tasks.findAll() : tasks.findByAssigneeEmailIgnoreCaseOrderByDueDateAsc(current.getEmail());
        return result.stream().map(TaskResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse get(Long id, User current) {
        Task task = find(id);
        ensureVisible(task, current);
        return TaskResponse.from(task);
    }

    @Transactional
    public TaskResponse update(Long id, TaskRequest request, User current) {
        Task task = find(id);
        ensureVisible(task, current);
        User assignee = request.assigneeId() == null ? task.getAssignee() : resolveAssignee(request.assigneeId(), current);
        boolean changed = !assignee.getId().equals(task.getAssignee().getId());
        apply(task, request, assignee);
        Task saved = tasks.save(task);
        if (changed) notifications.notifyAssignment(saved);
        return TaskResponse.from(saved);
    }

    @Transactional
    public void delete(Long id, User current) {
        Task task = find(id);
        ensureVisible(task, current);
        tasks.delete(task);
    }

    private Task find(Long id) { return tasks.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found")); }
    private void ensureVisible(Task task, User current) {
        if (current.getRole() != Role.ADMIN && !task.getAssignee().getId().equals(current.getId()) && !task.getCreatedBy().getId().equals(current.getId()))
            throw new AccessDeniedException("You do not have access to this task");
    }
    private User resolveAssignee(Long id, User current) {
        if (id == null || id.equals(current.getId())) return current;
        if (current.getRole() != Role.ADMIN) throw new AccessDeniedException("Only admins can assign tasks to other users");
        return users.findById(id).orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));
    }
    private void apply(Task task, TaskRequest request, User assignee) {
        task.setTitle(request.title().trim());
        task.setDescription(request.description().trim());
        task.setDueDate(request.dueDate());
        task.setStatus(request.status() == null ? task.getStatus() : request.status());
        task.setAssignee(assignee);
    }
}
