package edu.du.campusflow.service;

import edu.du.campusflow.annotation.Code;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.CommonCodeGroup;
import edu.du.campusflow.repository.CommonCodeGroupRepository;
import edu.du.campusflow.repository.CommonCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;

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
} 