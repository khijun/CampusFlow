package edu.du.campusflow.service;

import edu.du.campusflow.annotation.Code;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.CommonCodeGroup;
import edu.du.campusflow.repository.CommonCodeGroupRepository;
import edu.du.campusflow.repository.CommonCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonCodeService {
    
    private final CommonCodeRepository commonCodeRepository;
    private final CommonCodeGroupRepository commonCodeGroupRepository;



    @Transactional
    public <E extends Enum<E>> void insertCodeFromEnum(Class<E> enumClass) throws NoSuchFieldException {
        Code groupInfo = enumClass.getAnnotation(Code.class);
        CommonCodeGroup commonCodeGroup = CommonCodeGroup.builder()
                .groupCode(enumClass.getSimpleName().toUpperCase())
                .groupName(groupInfo.name())
                .groupDescription(groupInfo.description())
                .build();
        CommonCodeGroup group = commonCodeGroupRepository.save(commonCodeGroup);
        for (E enumValue : enumClass.getEnumConstants()) {
            Field field = enumValue.getDeclaringClass().getField(enumValue.name());
            Code fieldInfo = field.getAnnotation(Code.class);
            String codeValue = enumValue.name();               // Enum의 이름 (ENROLLED, LEAVE_OF_ABSENCE 등)
            CommonCode commonCode = CommonCode.builder()
                    .codeValue(codeValue)
                    .codeName(fieldInfo.name())
                    .codeDescription(fieldInfo.description())
                    .codeGroup(group)
                    .build();
            commonCodeRepository.save(commonCode);
        }
    }

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