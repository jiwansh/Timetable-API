package com.sutram.timetableapi.controller;

import com.sutram.timetableapi.dto.ApiResponse;
import com.sutram.timetableapi.model.Teacher;
import com.sutram.timetableapi.repository.TeacherRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "*")
public class TeacherController {
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Teacher>>> getAllTeachers() {
        try {
            List<Teacher> teachers = teacherRepository.findAll();
            return ResponseEntity.ok(ApiResponse.success("Teachers retrieved successfully", teachers));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error retrieving teachers: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Teacher>> getTeacherById(@PathVariable Long id) {
        try {
            return teacherRepository.findById(id)
                    .map(teacher -> ResponseEntity.ok(ApiResponse.success("Teacher retrieved successfully", teacher)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error retrieving teacher: " + e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Teacher>> createTeacher(@Valid @RequestBody Teacher teacher) {
        try {
            if (teacherRepository.existsByEmail(teacher.getEmail())) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Teacher with this email already exists"));
            }
            Teacher savedTeacher = teacherRepository.save(teacher);
            return ResponseEntity.ok(ApiResponse.success("Teacher created successfully", savedTeacher));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error creating teacher: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Teacher>> updateTeacher(@PathVariable Long id, @Valid @RequestBody Teacher teacher) {
        try {
            return teacherRepository.findById(id)
                    .map(existingTeacher -> {
                        existingTeacher.setName(teacher.getName());
                        existingTeacher.setEmail(teacher.getEmail());
                        Teacher savedTeacher = teacherRepository.save(existingTeacher);
                        return ResponseEntity.ok(ApiResponse.success("Teacher updated successfully", savedTeacher));
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error updating teacher: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTeacher(@PathVariable Long id) {
        try {
            if (!teacherRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            teacherRepository.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("Teacher deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error deleting teacher: " + e.getMessage()));
        }
    }
} 