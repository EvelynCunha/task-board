package com.board.taskboard.controller;

import com.board.taskboard.model.Task;
import com.board.taskboard.model.User;
import com.board.taskboard.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/Tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/health")
    public String getHealth(){
        return "Ok!";
    }

    @GetMapping("/Tasks")
    public List<Task> getAllTasks() {

        return taskService.getAllTasks();
    }

    @PostMapping("/Tasks")
    public String createTask(Task task) {
        taskService.saveTask(task);
        return "redirect:/Tasks";
    }

    @DeleteMapping("/Tasks/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/Tasks";
    }
}
