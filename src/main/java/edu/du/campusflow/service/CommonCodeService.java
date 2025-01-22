package edu.du.campusflow.service;

import edu.du.campusflow.annotation.Code;
import edu.du.campusflow.dto.CommonCodeDTO;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.CommonCodeGroup;
import edu.du.campusflow.repository.CommonCodeGroupRepository;
import edu.du.campusflow.repository.CommonCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

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

    // 특정 코드 그룹의 공통 코드 조회 (기존 메서드 활용)
    public List<CommonCodeDTO> getCommonCodesByGroup(String groupCode) {
        List<CommonCode> codes = commonCodeRepository.findByGroupCode(groupCode);
        return codes.stream()
                .map(code -> new CommonCodeDTO(code.getCodeValue(), code.getCodeName()))
                .collect(Collectors.toList());
    }

    public CommonCode getCodeByValue(Long groupId, String codeValue) {
        CommonCode code = commonCodeRepository.findByGroupIdAndCodeValue(groupId, codeValue);
        if (code == null) {
            throw new IllegalArgumentException("❌ 해당 코드가 존재하지 않습니다: groupId=" + groupId + ", codeValue=" + codeValue);
        }
        return code;
    }

    public CommonCode findById(Long id){
        return commonCodeRepository.findById(id).orElse(null);
    }

    public CommonCode findByCodeValue(String codeValue){
        return commonCodeRepository.findByCodeValue(codeValue);
    }

    public CommonCode findByCodeGroupAndCodeValue(String codeGroup, String codeValue){
        return commonCodeRepository.findByCodeGroupAndCodeValue(codeGroup, codeValue);
    }
}