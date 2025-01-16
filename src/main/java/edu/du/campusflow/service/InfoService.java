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
    private final MemberRepository memberRepository;

    // FamilyInfo Methods

    public List<FamilyInfo> getAllFamilyInfos() {
        return familyInfoRepository.findAll();
    }

    public List<FamilyInfo> getFamilyInfoById(Long memberId) {
        return familyInfoRepository.findByMember_MemberId(memberId);
    }

    public FamilyInfo saveFamilyInfo(FamilyInfo familyInfo, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
        FamilyInfo newFamilyInfo = FamilyInfo.builder()
                .member(member)
                .familyRelation(familyInfo.getFamilyRelation())
                .birthDate(familyInfo.getBirthDate())
                .contact(familyInfo.getContact())
                .name(familyInfo.getName())
                .build();
        return familyInfoRepository.save(newFamilyInfo);
    }

    public void deleteFamilyInfo(Long id) {
        familyInfoRepository.deleteById(id);
    }

    // EducationInfo Methods

    public List<EducationInfo> getAllEducationInfos() {
        return educationInfoRepository.findAll();
    }

    public List<EducationInfo> getEducationInfoById(Long memberId) {
        return educationInfoRepository.findByMember_MemberId(memberId);
    }

    public EducationInfo saveEducationInfo(EducationInfo educationInfo, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        EducationInfo newEducationInfo = EducationInfo.builder()
                .schoolName(educationInfo.getSchoolName())
                .enrollmentDate(educationInfo.getEnrollmentDate())
                .graduationDate(educationInfo.getGraduationDate())
                .graduationStatus(educationInfo.getGraduationStatus())
                .member(member) // Member와 연관 관계 설정
                .build();


        return educationInfoRepository.save(newEducationInfo);
    }

    public void deleteEducationInfo(Long id) {
        educationInfoRepository.deleteById(id);
    }

    // MilitaryInfo Methods

    public List<MilitaryInfo> getAllMilitaryInfos() {
        return militaryInfoRepository.findAll();
    }

    public List<MilitaryInfo> getMilitaryInfoById(Long memberId) {
        return militaryInfoRepository.findByMember_MemberId(memberId);
    }

    public MilitaryInfo saveMilitaryInfo(MilitaryInfo militaryInfo, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        MilitaryInfo newMilitaryInfo = MilitaryInfo.builder()
                .member(member)
                .dischargeType(militaryInfo.getDischargeType())
                .serviceNumber(militaryInfo.getServiceNumber())
                .finalRank(militaryInfo.getFinalRank())
                .build();
        return militaryInfoRepository.save(newMilitaryInfo);
    }

    public void deleteMilitaryInfo(Long id) {
        militaryInfoRepository.deleteById(id);
    }
}


