package com.sutram.timetableapi.controller;

import com.sutram.timetableapi.dto.ApiResponse;
import com.sutram.timetableapi.dto.TimetableRequest;
import com.sutram.timetableapi.model.TimetableEntry;
import com.sutram.timetableapi.service.TimetableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timetable")
@CrossOrigin(origins = "*")
public class TimetableController {
    
    @Autowired
    private TimetableService timetableService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<TimetableEntry>>> getAllTimetableEntries() {
        try {
            List<TimetableEntry> entries = timetableService.getAllTimetableEntries();
            return ResponseEntity.ok(ApiResponse.success("Timetable entries retrieved successfully", entries));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error retrieving timetable entries: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TimetableEntry>> getTimetableEntryById(@PathVariable Long id) {
        try {
            return timetableService.getTimetableEntryById(id)
                    .map(entry -> ResponseEntity.ok(ApiResponse.success("Timetable entry retrieved successfully", entry)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error retrieving timetable entry: " + e.getMessage()));
        }
    }
    
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<ApiResponse<List<TimetableEntry>>> getTimetableByTeacher(@PathVariable Long teacherId) {
        try {
            List<TimetableEntry> entries = timetableService.getTimetableByTeacher(teacherId);
            return ResponseEntity.ok(ApiResponse.success("Teacher timetable retrieved successfully", entries));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error retrieving teacher timetable: " + e.getMessage()));
        }
    }
    
    @GetMapping("/grade/{grade}/section/{section}")
    public ResponseEntity<ApiResponse<List<TimetableEntry>>> getTimetableByGradeAndSection(
            @PathVariable Integer grade, @PathVariable String section) {
        try {
            List<TimetableEntry> entries = timetableService.getTimetableByGradeAndSection(grade, section);
            return ResponseEntity.ok(ApiResponse.success("Grade timetable retrieved successfully", entries));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error retrieving grade timetable: " + e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<TimetableEntry>> createTimetableEntry(@Valid @RequestBody TimetableRequest request) {
        try {
            TimetableEntry entry = timetableService.createTimetableEntry(request);
            return ResponseEntity.ok(ApiResponse.success("Timetable entry created successfully", entry));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error creating timetable entry: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TimetableEntry>> updateTimetableEntry(
            @PathVariable Long id, @Valid @RequestBody TimetableRequest request) {
        try {
            TimetableEntry entry = timetableService.updateTimetableEntry(id, request);
            return ResponseEntity.ok(ApiResponse.success("Timetable entry updated successfully", entry));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error updating timetable entry: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTimetableEntry(@PathVariable Long id) {
        try {
            timetableService.deleteTimetableEntry(id);
            return ResponseEntity.ok(ApiResponse.success("Timetable entry deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error deleting timetable entry: " + e.getMessage()));
        }
    }
} 