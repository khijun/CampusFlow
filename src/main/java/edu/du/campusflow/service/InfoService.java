package edu.du.campusflow.service;

import edu.du.campusflow.entity.*;
import edu.du.campusflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InfoService {
    private final EducationInfoRepository educationInfoRepository;
    private final MilitaryInfoRepository militaryInfoRepository;
    private final FamilyInfoRepository familyInfoRepository;
    private final StudentRepository studentRepository;
    private final CommonCodeRepository commonCodeRepository;

    public List<EducationInfo> view_all_educationInfo(){
        return educationInfoRepository.findAll();
    }
    public List<MilitaryInfo> view_all_militaryInfo(){
        return militaryInfoRepository.findAll();
    }
    public List<FamilyInfo> view_all_familyInfo(){
        return familyInfoRepository.findAll();
    }
//    public EducationInfo save_educationInfo(EducationInfo educationInfo,Long studentId,Long codeId){
//        Student student = studentRepository.findById(studentId);
//        Optional<CommonCode> commonCode = commonCodeRepository.findByCodeId(codeId);
//
//        educationInfo = EducationInfo.builder()
//                .student(student)
//                .build()
//    }
}
