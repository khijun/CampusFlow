package edu.du.campusflow.service;

import edu.du.campusflow.entity.CommonCodeGroup;
import edu.du.campusflow.repository.CommonCodeGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonCodeGroupService {
    private final CommonCodeGroupRepository commonCodeGroupRepository;

    public CommonCodeGroup findByGroupCode(String groupCode) {
        return commonCodeGroupRepository.findByGroupCode(groupCode);
    }
}
