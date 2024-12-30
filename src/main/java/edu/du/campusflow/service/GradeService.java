package edu.du.campusflow.service;

import edu.du.campusflow.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradeService {
    private final GradeRepository gradeRepository;
}
