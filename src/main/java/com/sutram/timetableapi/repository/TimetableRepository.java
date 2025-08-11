package com.sutram.timetableapi.repository;

import com.sutram.timetableapi.model.TimetableEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimetableRepository extends JpaRepository<TimetableEntry, Long> {
    
    // Check if slot is already occupied (without subject - just grade, section, day, period)
    Optional<TimetableEntry> findByGradeAndSectionAndClassDayAndPeriod(
            Integer grade, String section, String classDay, Integer period);
    
    // Check if teacher is already assigned at the same time
    Optional<TimetableEntry> findByTeacherIdAndClassDayAndPeriod(Long teacherId, String classDay, Integer period);
    
    // Find all entries for a specific teacher
    List<TimetableEntry> findByTeacherId(Long teacherId);
    
    // Find all entries for a specific grade and section
    List<TimetableEntry> findByGradeAndSection(Integer grade, String section);
    
    // Check if slot is occupied (any teacher) - without subject
    boolean existsByGradeAndSectionAndClassDayAndPeriod(Integer grade, String section, String classDay, Integer period);
    
    // Check if teacher is busy at specific time
    boolean existsByTeacherIdAndClassDayAndPeriod(Long teacherId, String classDay, Integer period);
} 