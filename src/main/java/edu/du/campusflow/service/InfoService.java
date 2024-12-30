package edu.du.campusflow.service;

import edu.du.campusflow.entity.EducationInfo;
import edu.du.campusflow.repository.EducationInfoRepository;
import edu.du.campusflow.repository.FamilyInfoRepository;
import edu.du.campusflow.repository.MilitaryInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InfoService {
    private final EducationInfoRepository educationInfoRepository;
    private final MilitaryInfoRepository militaryInfoRepository;
    private final FamilyInfoRepository familyInfoRepository;
}
