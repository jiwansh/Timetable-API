package com.sutram.timetableapi.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TimetableRequest {
    
    @NotNull(message = "Teacher ID is required")
    private Long teacherId;
    
    @NotNull(message = "Grade is required")
    @Min(value = 1, message = "Grade must be at least 1")
    @Max(value = 12, message = "Grade must be at most 12")
    private Integer grade;
    
    @NotBlank(message = "Section is required")
    @Pattern(regexp = "^[A-Z]$", message = "Section must be a single uppercase letter")
    private String section;
    
    @NotBlank(message = "Subject is required")
    private String subject;
    
    @NotBlank(message = "Day is required")
    @Pattern(regexp = "^(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday)$", 
             message = "Day must be one of: Monday, Tuesday, Wednesday, Thursday, Friday, Saturday")
    private String classDay;
    
    @NotNull(message = "Period is required")
    @Min(value = 1, message = "Period must be between 1 and 8")
    @Max(value = 8, message = "Period must be between 1 and 8")
    private Integer period;
} 