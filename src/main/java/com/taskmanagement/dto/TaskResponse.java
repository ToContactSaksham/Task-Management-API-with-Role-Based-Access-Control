package com.taskmanagement.dto;

import com.taskmanagement.model.Task;
import com.taskmanagement.model.TaskStatus;
import java.time.LocalDate;

public record TaskResponse(Long id, String title, String description, LocalDate dueDate,
                           TaskStatus status, Long assigneeId, String assigneeEmail, Long createdById) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getDueDate(),
                task.getStatus(), task.getAssignee().getId(), task.getAssignee().getEmail(), task.getCreatedBy().getId());
    }
}
