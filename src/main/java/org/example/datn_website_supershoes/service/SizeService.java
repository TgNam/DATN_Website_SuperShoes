package org.example.datn_website_supershoes.service;


import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.SizeRequest;
import org.example.datn_website_supershoes.dto.response.SizeResponse;
import org.example.datn_website_supershoes.model.Size;
import org.example.datn_website_supershoes.repository.SizeRepository;

import org.example.datn_website_supershoes.webconfig.NotificationController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SizeService {

    @Autowired
    SizeRepository sizeRepository;
    @Autowired
    NotificationController notificationController;

    public List<SizeResponse> findAllSize() {
        return sizeRepository.findAllSize();
    }

    public List<SizeResponse> findSizeByStatusACTIVE() {
        return sizeRepository.findSizeByStatus(Status.ACTIVE.toString());
    }

    public Size createSize(SizeRequest sizeRequest) {
        Optional<Size> size = sizeRepository.findByName(sizeRequest.getName());
        if (size.isPresent()) {
            throw new RuntimeException("Kích cỡ " + sizeRequest.getName() + " đã tồn tại");
        }
        return sizeRepository.save(convertSizeRequestDTO(sizeRequest));
    }

    public Size updateStatus(Long id, boolean aBoolean) {
        Optional<Size> sizeOt = sizeRepository.findById(id);
        if (!sizeOt.isPresent()) {
            throw new RuntimeException("Không tìm thấy tài nguyên kích cỡ trong hệ thống!");
        }
        String newStatus = aBoolean ? Status.ACTIVE.toString() : Status.INACTIVE.toString();
        sizeOt.get().setStatus(newStatus);
        Size size = sizeRepository.save(sizeOt.get());
        return size;

    }

    public Size convertSizeRequestDTO(SizeRequest sizeRequest) {
        Size size = Size.builder()
                .name(sizeRequest.getName())
                .build();
        size.setStatus(Status.ACTIVE.toString());
        return size;
    }

}
