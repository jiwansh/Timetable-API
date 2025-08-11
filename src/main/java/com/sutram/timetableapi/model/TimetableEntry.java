package com.sutram.timetableapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "timetable_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimetableEntry {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;
    
    @Column(nullable = false)
    @Min(value = 1, message = "Grade must be at least 1")
    @Max(value = 12, message = "Grade must be at most 12")
    private Integer grade;
    
    @Column(nullable = false)
    @Pattern(regexp = "^[A-Z]$", message = "Section must be a single uppercase letter")
    private String section;
    
    @Column(nullable = false)
    private String subject;
    
    @Column(name = "class_day", nullable = false)
    @Pattern(regexp = "^(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday)$", 
             message = "Day must be one of: Monday, Tuesday, Wednesday, Thursday, Friday, Saturday")
    private String classDay;
    
    @Column(nullable = false)
    @Min(value = 1, message = "Period must be between 1 and 8")
    @Max(value = 8, message = "Period must be between 1 and 8")
    private Integer period;
    
    // Helper method to get valid days
    public static List<String> getValidDays() {
        return Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
    }
    
    // Helper method to get valid periods
    public static List<Integer> getValidPeriods() {
        return Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
    }
} 