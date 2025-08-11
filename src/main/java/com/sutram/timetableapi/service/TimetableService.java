package com.sutram.timetableapi.service;

import com.sutram.timetableapi.dto.TimetableRequest;
import com.sutram.timetableapi.model.Teacher;
import com.sutram.timetableapi.model.TimetableEntry;
import com.sutram.timetableapi.repository.TeacherRepository;
import com.sutram.timetableapi.repository.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TimetableService {
    
    @Autowired
    private TimetableRepository timetableRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    public List<TimetableEntry> getAllTimetableEntries() {

        return timetableRepository.findAll();
    }
    
    public Optional<TimetableEntry> getTimetableEntryById(Long id) {

        return timetableRepository.findById(id);
    }
    
    public List<TimetableEntry> getTimetableByTeacher(Long teacherId) {
        return timetableRepository.findByTeacherId(teacherId);
    }
    
    public List<TimetableEntry> getTimetableByGradeAndSection(Integer grade, String section) {
        return timetableRepository.findByGradeAndSection(grade, section);
    }
    
    public TimetableEntry createTimetableEntry(TimetableRequest request) throws Exception {
        // Check if teacher exists
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new Exception("Teacher not found with ID: " + request.getTeacherId()));
        
        // Validate no scheduling conflicts
        validateNoConflicts(request, null);
        
        // Create new timetable entry
        TimetableEntry entry = new TimetableEntry();
        entry.setTeacher(teacher);
        entry.setGrade(request.getGrade());
        entry.setSection(request.getSection());
        entry.setSubject(request.getSubject());
        entry.setClassDay(request.getClassDay());
        entry.setPeriod(request.getPeriod());
        
        return timetableRepository.save(entry);
    }
    
    public TimetableEntry updateTimetableEntry(Long id, TimetableRequest request) throws Exception {
        // Check if timetable entry exists
        TimetableEntry existingEntry = timetableRepository.findById(id)
                .orElseThrow(() -> new Exception("Timetable entry not found with ID: " + id));
        
        // Check if teacher exists
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new Exception("Teacher not found with ID: " + request.getTeacherId()));
        
        // Validate no scheduling conflicts (excluding current entry)
        validateNoConflicts(request, id);
        
        // Update the entry
        existingEntry.setTeacher(teacher);
        existingEntry.setGrade(request.getGrade());
        existingEntry.setSection(request.getSection());
        existingEntry.setSubject(request.getSubject());
        existingEntry.setClassDay(request.getClassDay());
        existingEntry.setPeriod(request.getPeriod());
        
        return timetableRepository.save(existingEntry);
    }
    
    public void deleteTimetableEntry(Long id) throws Exception {
        if (!timetableRepository.existsById(id)) {
            throw new Exception("Timetable entry not found with ID: " + id);
        }
        timetableRepository.deleteById(id);
    }
    
    private void validateNoConflicts(TimetableRequest request, Long excludeId) throws Exception {
        // Check if slot is already occupied by another teacher
        if (timetableRepository.existsByGradeAndSectionAndClassDayAndPeriod(
                request.getGrade(), request.getSection(), request.getClassDay(), request.getPeriod())) {
            
            // If updating, check if it's the same entry
            if (excludeId != null) {
                Optional<TimetableEntry> existingEntry = timetableRepository.findByGradeAndSectionAndClassDayAndPeriod(
                        request.getGrade(), request.getSection(), request.getClassDay(), request.getPeriod());
                if (existingEntry.isPresent() && !existingEntry.get().getId().equals(excludeId)) {
                    throw new Exception("Slot is already occupied by another teacher");
                }
            } else {
                throw new Exception("Slot is already occupied by another teacher");
            }
        }
        
        // Check if teacher is already assigned to another class at the same time
        if (timetableRepository.existsByTeacherIdAndClassDayAndPeriod(
                request.getTeacherId(), request.getClassDay(), request.getPeriod())) {
            
            // If updating, check if it's the same entry
            if (excludeId != null) {
                Optional<TimetableEntry> existingEntry = timetableRepository.findByTeacherIdAndClassDayAndPeriod(
                        request.getTeacherId(), request.getClassDay(), request.getPeriod());
                if (existingEntry.isPresent() && !existingEntry.get().getId().equals(excludeId)) {
                    throw new Exception("Teacher is already assigned to another class at the same time");
                }
            } else {
                throw new Exception("Teacher is already assigned to another class at the same time");
            }
        }
    }
} 