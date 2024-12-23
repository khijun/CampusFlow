package edu.du.academic_management_system.service;

import edu.du.academic_management_system.entity.CommonCode;
import edu.du.academic_management_system.entity.CommonCodeGroup;
import edu.du.academic_management_system.repository.CommonCodeGroupRepository;
import edu.du.academic_management_system.repository.CommonCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonCodeService {
    
    private final CommonCodeRepository commonCodeRepository;
    private final CommonCodeGroupRepository commonCodeGroupRepository;

    @Transactional
    public void createCommonCodes() {
        // CommonCode Group 생성
        CommonCodeGroup statusGroup = CommonCodeGroup.builder()
                .groupCode("ACADEMIC_STATUS")
                .groupName("학적상태")
                .groupDescription("학생의 학적 상태를 나타내는 코드")
                .build();

        CommonCodeGroup genderGroup = CommonCodeGroup.builder()
                .groupCode("GENDER")
                .groupName("성별")
                .groupDescription("성별을 나타내는 코드")
                .build();

        // CommonCode 생성
        CommonCode activeStatus = CommonCode.builder()
                .codeGroup(statusGroup)
                .codeValue("ACTIVE")
                .codeName("재학")
                .codeDescription("현재 재학 중인 상태")
                .build();

        CommonCode leaveStatus = CommonCode.builder()
                .codeGroup(statusGroup)
                .codeValue("LEAVE")
                .codeName("휴학")
                .codeDescription("현재 휴학 중인 상태")
                .build();

        CommonCode maleGender = CommonCode.builder()
                .codeGroup(genderGroup)
                .codeValue("MALE")
                .codeName("남성")
                .codeDescription("남성")
                .build();

        CommonCode femaleGender = CommonCode.builder()
                .codeGroup(genderGroup)
                .codeValue("FEMALE")
                .codeName("여성")
                .codeDescription("여성")
                .build();

        List<CommonCodeGroup> commonCodeGroups = List.of(statusGroup, genderGroup);
        List<CommonCode> commonCodes = List.of(
            activeStatus, leaveStatus, maleGender, femaleGender
        );
        
        commonCodeGroupRepository.saveAll(commonCodeGroups);
        commonCodeRepository.saveAll(commonCodes);
    }
} 