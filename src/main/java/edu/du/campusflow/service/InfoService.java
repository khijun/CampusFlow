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

    // 1. 모든 교육 정보를 조회하는 메서드
    public List<EducationInfo> view_all_educationInfo() {
        return educationInfoRepository.findAll();
    }

    // 2. 특정 회원의 교육 정보를 조회하는 메서드
    public List<EducationInfo> view_educationInfo_by_member(Member memberId) {
        return educationInfoRepository.findByMember(memberId);  // memberId로 조회
    }

    // 3. 모든 병역 정보를 조회하는 메서드
    public List<MilitaryInfo> view_all_militaryInfo() {
        return militaryInfoRepository.findAll();
    }

    // 4. 특정 회원의 병역 정보를 조회하는 메서드
    public List<MilitaryInfo> view_militaryInfo_by_member(Member memberId) {
        return militaryInfoRepository.findByMember(memberId);  // memberId로 조회
    }

    // 5. 모든 가족 정보를 조회하는 메서드
    public List<FamilyInfo> view_all_familyInfo() {
        return familyInfoRepository.findAll();
    }

    // 6. 특정 회원의 가족 정보를 조회하는 메서드
    public List<FamilyInfo> view_familyInfo_by_member(Member memberId) {
        return familyInfoRepository.findByMember(memberId);  // memberId로 조회
    }

    // 7. 교육 정보 추가하기 (Builder 패턴 사용)
    public EducationInfo add_educationInfo(EducationInfo educationInfo) {
        return educationInfoRepository.save(EducationInfo.builder()
                .schoolName(educationInfo.getSchoolName())
                .enrollmentDate(educationInfo.getEnrollmentDate())
                .graduationDate(educationInfo.getGraduationDate())
                .graduationStatus(educationInfo.getGraduationStatus())
                .member(educationInfo.getMember()) // Member는 필수로 넣어줘야 할 것
                .build());
    }

    // 8. 병역 정보 추가하기 (Builder 패턴 사용)
    public MilitaryInfo add_militaryInfo(MilitaryInfo militaryInfo) {
        return militaryInfoRepository.save(MilitaryInfo.builder()
                .dischargeType(militaryInfo.getDischargeType())
                .finalRank(militaryInfo.getFinalRank())
                .serviceNumber(militaryInfo.getServiceNumber())
                .member(militaryInfo.getMember()) // Member는 필수로 넣어줘야 할 것
                .build());
    }

    // 9. 가족 정보 추가하기 (Builder 패턴 사용)
    public FamilyInfo add_familyInfo(FamilyInfo familyInfo) {
        return familyInfoRepository.save(FamilyInfo.builder()
                .familyRelation(familyInfo.getFamilyRelation())
                .name(familyInfo.getName())
                .birthDate(familyInfo.getBirthDate())
                .contact(familyInfo.getContact())
                .member(familyInfo.getMember()) // Member는 필수로 넣어줘야 할 것
                .build());
    }

    // 10. 교육 정보 수정하기 (Builder 패턴 사용)
    public EducationInfo update_educationInfo(Long educationInfoId, EducationInfo updatedInfo) {
        EducationInfo existingInfo = educationInfoRepository.findById(educationInfoId)
                .orElseThrow(() -> new IllegalArgumentException("교육 정보 없음"));

        EducationInfo updatedEducationInfo = EducationInfo.builder()
                .id(educationInfoId)
                .schoolName(updatedInfo.getSchoolName())
                .enrollmentDate(updatedInfo.getEnrollmentDate())
                .graduationDate(updatedInfo.getGraduationDate())
                .graduationStatus(updatedInfo.getGraduationStatus())
                .member(updatedInfo.getMember()) // Member는 필수로 넣어줘야 할 것
                .build();

        return educationInfoRepository.save(updatedEducationInfo);  // 수정된 정보 저장
    }

    // 11. 병역 정보 수정하기 (Builder 패턴 사용)
    public MilitaryInfo update_militaryInfo(Long militaryInfoId, MilitaryInfo updatedInfo) {
        MilitaryInfo existingInfo = militaryInfoRepository.findById(militaryInfoId)
                .orElseThrow(() -> new IllegalArgumentException("병역 정보 없음"));

        MilitaryInfo updatedMilitaryInfo = MilitaryInfo.builder()
                .id(militaryInfoId)
                .dischargeType(updatedInfo.getDischargeType())
                .finalRank(updatedInfo.getFinalRank())
                .serviceNumber(updatedInfo.getServiceNumber())
                .member(updatedInfo.getMember()) // Member는 필수로 넣어줘야 할 것
                .build();

        return militaryInfoRepository.save(updatedMilitaryInfo);  // 수정된 정보 저장
    }

    // 12. 가족 정보 수정하기 (Builder 패턴 사용)
    public FamilyInfo update_familyInfo(Long familyInfoId, FamilyInfo updatedInfo) {
        FamilyInfo existingInfo = familyInfoRepository.findById(familyInfoId)
                .orElseThrow(() -> new IllegalArgumentException("가족 정보 없음"));

        FamilyInfo updatedFamilyInfo = FamilyInfo.builder()
                .id(familyInfoId)
                .familyRelation(updatedInfo.getFamilyRelation())
                .name(updatedInfo.getName())
                .birthDate(updatedInfo.getBirthDate())
                .contact(updatedInfo.getContact())
                .member(updatedInfo.getMember()) // Member는 필수로 넣어줘야 할 것
                .build();

        return familyInfoRepository.save(updatedFamilyInfo);  // 수정된 정보 저장
    }

    // 13. 교육 정보 삭제하기
    public void delete_educationInfo(Long educationInfoId) {
        EducationInfo existingInfo = educationInfoRepository.findById(educationInfoId)
                .orElseThrow(() -> new IllegalArgumentException("교육 정보 없음"));
        educationInfoRepository.delete(existingInfo);  // 정보 삭제
    }

    // 14. 병역 정보 삭제하기
    public void delete_militaryInfo(Long militaryInfoId) {
        MilitaryInfo existingInfo = militaryInfoRepository.findById(militaryInfoId)
                .orElseThrow(() -> new IllegalArgumentException("병역 정보 없음"));
        militaryInfoRepository.delete(existingInfo);  // 정보 삭제
    }

    // 15. 가족 정보 삭제하기
    public void delete_familyInfo(Long familyInfoId) {
        FamilyInfo existingInfo = familyInfoRepository.findById(familyInfoId)
                .orElseThrow(() -> new IllegalArgumentException("가족 정보 없음"));
        familyInfoRepository.delete(existingInfo);  // 정보 삭제
    }
}


