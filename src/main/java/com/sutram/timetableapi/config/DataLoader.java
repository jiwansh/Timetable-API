package com.sutram.timetableapi.config;

import com.sutram.timetableapi.model.Teacher;
import com.sutram.timetableapi.model.TimetableEntry;
import com.sutram.timetableapi.repository.TeacherRepository;
import com.sutram.timetableapi.repository.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private TimetableRepository timetableRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Load sample teachers
        Teacher teacher1 = new Teacher();
        teacher1.setName("Bijay Panda");
        teacher1.setEmail("bijayaprasana.job@gmail.com");
        teacher1 = teacherRepository.save(teacher1);
        
        Teacher teacher2 = new Teacher();
        teacher2.setName("Jiwanshu Kumar");
        teacher2.setEmail("javajiwanshu@sutramsolutions.com");
        teacher2 = teacherRepository.save(teacher2);
        
        Teacher teacher3 = new Teacher();
        teacher3.setName("Demo Teacher");
        teacher3.setEmail("teacher@sutramsolutions.com");
        teacher3 = teacherRepository.save(teacher3);
        
        // Load sample timetable entries
        TimetableEntry entry1 = new TimetableEntry();
        entry1.setTeacher(teacher1);
        entry1.setGrade(7);
        entry1.setSection("A");
        entry1.setSubject("Mathematics");
        entry1.setClassDay("Monday");
        entry1.setPeriod(1);
        timetableRepository.save(entry1);
        
        TimetableEntry entry2 = new TimetableEntry();
        entry2.setTeacher(teacher2);
        entry2.setGrade(6);
        entry2.setSection("A");
        entry2.setSubject("English");
        entry2.setClassDay("Monday");
        entry2.setPeriod(2);
        timetableRepository.save(entry2);
        
        TimetableEntry entry3 = new TimetableEntry();
        entry3.setTeacher(teacher3);
        entry3.setGrade(6);
        entry3.setSection("B");
        entry3.setSubject("Science");
        entry3.setClassDay("Monday");
        entry3.setPeriod(1);
        timetableRepository.save(entry3);
        
        System.out.println("Sample data loaded successfully!");
    }
} 