package com.taskmanagement.dto;

import com.taskmanagement.model.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TaskRequest(@NotBlank @Size(max = 160) String title,
                          @NotBlank @Size(max = 5000) String description,
                          @NotNull @FutureOrPresent LocalDate dueDate,
                          TaskStatus status,
                          Long assigneeId) {}
