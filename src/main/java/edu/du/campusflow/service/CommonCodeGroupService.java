package edu.du.campusflow.service;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.CommonCodeGroup;
import edu.du.campusflow.repository.CommonCodeGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommonCodeGroupService {
    private final CommonCodeGroupRepository commonCodeGroupRepository;

}
