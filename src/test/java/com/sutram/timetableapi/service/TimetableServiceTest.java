package com.sutram.timetableapi.service;

import com.sutram.timetableapi.dto.TimetableRequest;
import com.sutram.timetableapi.model.Teacher;
import com.sutram.timetableapi.model.TimetableEntry;
import com.sutram.timetableapi.repository.TeacherRepository;
import com.sutram.timetableapi.repository.TimetableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimetableServiceTest {
    
    @Mock
    private TimetableRepository timetableRepository;
    
    @Mock
    private TeacherRepository teacherRepository;
    
    @InjectMocks
    private TimetableService timetableService;
    
    private Teacher teacher1;
    private Teacher teacher2;
    private TimetableRequest validRequest;
    private TimetableEntry existingEntry;
    
    @BeforeEach
    void setUp() {
        teacher1 = new Teacher(1L, "Bijay Panda", "bijayaprasana.job@gmail.com");
        teacher2 = new Teacher(2L, "Jiwanshu Kumar", "javajiwanshu@sutramsolutions.com");
        
        validRequest = new TimetableRequest();
        validRequest.setTeacherId(1L);
        validRequest.setGrade(7);
        validRequest.setSection("A");
        validRequest.setSubject("Mathematics");
        validRequest.setClassDay("Monday");
        validRequest.setPeriod(1);
        
        existingEntry = new TimetableEntry();
        existingEntry.setId(1L);
        existingEntry.setTeacher(teacher1);
        existingEntry.setGrade(7);
        existingEntry.setSection("A");
        existingEntry.setSubject("Mathematics");
        existingEntry.setClassDay("Monday");
        existingEntry.setPeriod(1);
    }
    
    @Test
    void testCreateTimetableEntry_Success() throws Exception {
        // Arrange
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher1));
        when(timetableRepository.existsByGradeAndSectionAndClassDayAndPeriod(7, "A", "Monday", 1)).thenReturn(false);
        when(timetableRepository.existsByTeacherIdAndClassDayAndPeriod(1L, "Monday", 1)).thenReturn(false);
        when(timetableRepository.save(any(TimetableEntry.class))).thenReturn(existingEntry);
        
        // Act
        TimetableEntry result = timetableService.createTimetableEntry(validRequest);
        
        // Assert
        assertNotNull(result);
        assertEquals(teacher1, result.getTeacher());
        assertEquals(7, result.getGrade());
        assertEquals("A", result.getSection());
        assertEquals("Mathematics", result.getSubject());
        assertEquals("Monday", result.getClassDay());
        assertEquals(1, result.getPeriod());
        
        verify(timetableRepository).save(any(TimetableEntry.class));
    }
    
    @Test
    void testCreateTimetableEntry_TeacherNotFound() {
        // Arrange
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            timetableService.createTimetableEntry(validRequest);
        });
        
        assertEquals("Teacher not found with ID: 1", exception.getMessage());
        verify(timetableRepository, never()).save(any(TimetableEntry.class));
    }
    
    @Test
    void testCreateTimetableEntry_SlotAlreadyOccupied() {
        // Arrange
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher1));
        when(timetableRepository.existsByGradeAndSectionAndClassDayAndPeriod(7, "A", "Monday", 1)).thenReturn(true);
        
        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            timetableService.createTimetableEntry(validRequest);
        });
        
        assertEquals("Slot is already occupied by another teacher", exception.getMessage());
        verify(timetableRepository, never()).save(any(TimetableEntry.class));
    }
    
    @Test
    void testCreateTimetableEntry_TeacherAlreadyBusy() {
        // Arrange
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher1));
        when(timetableRepository.existsByGradeAndSectionAndClassDayAndPeriod(7, "A", "Monday", 1)).thenReturn(false);
        when(timetableRepository.existsByTeacherIdAndClassDayAndPeriod(1L, "Monday", 1)).thenReturn(true);
        
        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            timetableService.createTimetableEntry(validRequest);
        });
        
        assertEquals("Teacher is already assigned to another class at the same time", exception.getMessage());
        verify(timetableRepository, never()).save(any(TimetableEntry.class));
    }
    
    @Test
    void testUpdateTimetableEntry_Success() throws Exception {
        // Arrange
        when(timetableRepository.findById(1L)).thenReturn(Optional.of(existingEntry));
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher1));
        when(timetableRepository.existsByGradeAndSectionAndClassDayAndPeriod(7, "A", "Monday", 1)).thenReturn(false);
        when(timetableRepository.existsByTeacherIdAndClassDayAndPeriod(1L, "Monday", 1)).thenReturn(false);
        when(timetableRepository.save(any(TimetableEntry.class))).thenReturn(existingEntry);
        
        // Act
        TimetableEntry result = timetableService.updateTimetableEntry(1L, validRequest);
        
        // Assert
        assertNotNull(result);
        verify(timetableRepository).save(any(TimetableEntry.class));
    }
    
    @Test
    void testUpdateTimetableEntry_EntryNotFound() {
        // Arrange
        when(timetableRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            timetableService.updateTimetableEntry(1L, validRequest);
        });
        
        assertEquals("Timetable entry not found with ID: 1", exception.getMessage());
        verify(timetableRepository, never()).save(any(TimetableEntry.class));
    }
    
    @Test
    void testUpdateTimetableEntry_UpdateToOccupiedSlot() {
        // Arrange
        when(timetableRepository.findById(1L)).thenReturn(Optional.of(existingEntry));
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher1));
        when(timetableRepository.existsByGradeAndSectionAndClassDayAndPeriod(7, "A", "Monday", 1)).thenReturn(true);
        
        // Mock that the slot is occupied by a different entry
        when(timetableRepository.findByGradeAndSectionAndClassDayAndPeriod(7, "A", "Monday", 1))
                .thenReturn(Optional.of(new TimetableEntry(2L, teacher2, 7, "A", "English", "Monday", 1)));
        
        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            timetableService.updateTimetableEntry(1L, validRequest);
        });
        
        assertEquals("Slot is already occupied by another teacher", exception.getMessage());
        verify(timetableRepository, never()).save(any(TimetableEntry.class));
    }
    
    @Test
    void testUpdateTimetableEntry_UpdateToTeacherBusyTime() {
        // Arrange
        when(timetableRepository.findById(1L)).thenReturn(Optional.of(existingEntry));
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher1));
        when(timetableRepository.existsByGradeAndSectionAndClassDayAndPeriod(7, "A", "Monday", 1)).thenReturn(false);
        when(timetableRepository.existsByTeacherIdAndClassDayAndPeriod(1L, "Monday", 1)).thenReturn(true);
        
        // Mock that the teacher is busy at this time in a different entry
        when(timetableRepository.findByTeacherIdAndClassDayAndPeriod(1L, "Monday", 1))
                .thenReturn(Optional.of(new TimetableEntry(2L, teacher1, 7, "B", "Science", "Monday", 1)));
        
        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            timetableService.updateTimetableEntry(1L, validRequest);
        });
        
        assertEquals("Teacher is already assigned to another class at the same time", exception.getMessage());
        verify(timetableRepository, never()).save(any(TimetableEntry.class));
    }
    
    @Test
    void testDeleteTimetableEntry_Success() throws Exception {
        // Arrange
        when(timetableRepository.existsById(1L)).thenReturn(true);
        
        // Act
        timetableService.deleteTimetableEntry(1L);
        
        // Assert
        verify(timetableRepository).deleteById(1L);
    }
    
    @Test
    void testDeleteTimetableEntry_EntryNotFound() {
        // Arrange
        when(timetableRepository.existsById(1L)).thenReturn(false);
        
        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            timetableService.deleteTimetableEntry(1L);
        });
        
        assertEquals("Timetable entry not found with ID: 1", exception.getMessage());
        verify(timetableRepository, never()).deleteById(any());
    }
    
    @Test
    void testGetAllTimetableEntries() {
        // Arrange
        List<TimetableEntry> expectedEntries = Arrays.asList(existingEntry);
        when(timetableRepository.findAll()).thenReturn(expectedEntries);
        
        // Act
        List<TimetableEntry> result = timetableService.getAllTimetableEntries();
        
        // Assert
        assertEquals(expectedEntries, result);
        verify(timetableRepository).findAll();
    }
    
    @Test
    void testGetTimetableEntryById() {
        // Arrange
        when(timetableRepository.findById(1L)).thenReturn(Optional.of(existingEntry));
        
        // Act
        Optional<TimetableEntry> result = timetableService.getTimetableEntryById(1L);
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals(existingEntry, result.get());
        verify(timetableRepository).findById(1L);
    }
    
    @Test
    void testGetTimetableByTeacher() {
        // Arrange
        List<TimetableEntry> expectedEntries = Arrays.asList(existingEntry);
        when(timetableRepository.findByTeacherId(1L)).thenReturn(expectedEntries);
        
        // Act
        List<TimetableEntry> result = timetableService.getTimetableByTeacher(1L);
        
        // Assert
        assertEquals(expectedEntries, result);
        verify(timetableRepository).findByTeacherId(1L);
    }
    
    @Test
    void testGetTimetableByGradeAndSection() {
        // Arrange
        List<TimetableEntry> expectedEntries = Arrays.asList(existingEntry);
        when(timetableRepository.findByGradeAndSection(7, "A")).thenReturn(expectedEntries);
        
        // Act
        List<TimetableEntry> result = timetableService.getTimetableByGradeAndSection(7, "A");
        
        // Assert
        assertEquals(expectedEntries, result);
        verify(timetableRepository).findByGradeAndSection(7, "A");
    }
} 