package com.taskmanagement.service;

import com.taskmanagement.model.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username:}") private String from;
    public EmailNotificationService(ObjectProvider<JavaMailSender> mailSender) {
        this.mailSender = mailSender.getIfAvailable();
    }

    @Async("notificationExecutor")
    public void notifyAssignment(Task task) {
        if (mailSender == null || from == null || from.isBlank()) return;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(task.getAssignee().getEmail());
        message.setSubject("Task assigned: " + task.getTitle());
        message.setText("You have been assigned a task.\n\nTitle: " + task.getTitle()
                + "\nDue date: " + task.getDueDate() + "\n\n" + task.getDescription());
        mailSender.send(message);
    }
}
