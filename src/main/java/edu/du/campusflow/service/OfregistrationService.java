package edu.du.campusflow.service;

import edu.du.campusflow.entity.Ofregistration;
import edu.du.campusflow.repository.OfregistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfregistrationService {

    private final OfregistrationRepository ofregistrationRepository;

    public Ofregistration getOfregistrationById(Long id) {
        return ofregistrationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("수강신청 정보를 찾을 수 없습니다. ID: " + id));
    }
}