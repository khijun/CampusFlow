package edu.du.campusflow.service;

import edu.du.campusflow.entity.Student;
import edu.du.campusflow.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;

    // ID로 학생 조회
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 학생이 존재하지 않습니다. ID: " + id));
    }

} 