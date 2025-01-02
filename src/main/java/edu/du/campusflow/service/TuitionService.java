package edu.du.campusflow.service;

import edu.du.campusflow.repository.TuitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TuitionService {
    @Autowired
    private TuitionRepository tuitionRepository;
}