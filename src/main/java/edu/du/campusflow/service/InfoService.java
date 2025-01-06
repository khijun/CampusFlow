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
    private final CommonCodeRepository commonCodeRepository;

    // FamilyInfo Methods

    public List<FamilyInfo> getAllFamilyInfos() {
        return familyInfoRepository.findAll();
    }

    public Optional<FamilyInfo> getFamilyInfoById(Long id) {
        return familyInfoRepository.findById(id);
    }

    public FamilyInfo saveFamilyInfo(FamilyInfo familyInfo) {
        return familyInfoRepository.save(familyInfo);
    }

    public void deleteFamilyInfo(Long id) {
        familyInfoRepository.deleteById(id);
    }

    // EducationInfo Methods

    public List<EducationInfo> getAllEducationInfos() {
        return educationInfoRepository.findAll();
    }

    public Optional<EducationInfo> getEducationInfoById(Long id) {
        return educationInfoRepository.findById(id);
    }

    public EducationInfo saveEducationInfo(EducationInfo educationInfo) {
        return educationInfoRepository.save(educationInfo);
    }

    public void deleteEducationInfo(Long id) {
        educationInfoRepository.deleteById(id);
    }

    // MilitaryInfo Methods

    public List<MilitaryInfo> getAllMilitaryInfos() {
        return militaryInfoRepository.findAll();
    }

    public Optional<MilitaryInfo> getMilitaryInfoById(Long id) {
        return militaryInfoRepository.findById(id);
    }

    public MilitaryInfo saveMilitaryInfo(MilitaryInfo militaryInfo) {
        return militaryInfoRepository.save(militaryInfo);
    }

    public void deleteMilitaryInfo(Long id) {
        militaryInfoRepository.deleteById(id);
    }
}


