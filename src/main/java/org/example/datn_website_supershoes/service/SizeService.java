package org.example.datn_website_supershoes.service;


import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.AccountRequest;
import org.example.datn_website_supershoes.dto.request.SizeRequest;
import org.example.datn_website_supershoes.dto.response.SizeResponse;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.model.Size;
import org.example.datn_website_supershoes.repository.SizeRepository;

import org.example.datn_website_supershoes.model.BillDetail;
import org.example.datn_website_supershoes.model.Size;
import org.example.datn_website_supershoes.repository.BillDetailRepository;
import org.example.datn_website_supershoes.repository.SizeReposiotry;
import org.springframework.beans.BeanUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SizeService {

    @Autowired
    SizeRepository sizeRepository;
    public List<SizeResponse> findByStatus(){
        return sizeRepository.findByStatus();
    }
    public Size createSize(SizeRequest sizeRequest){
            Optional<Size> size = sizeRepository.findByName(sizeRequest.getName());
            if(size.isPresent()){
                throw new RuntimeException("Size "+ sizeRequest.getName() +" đã tồn tại");
            }
            return sizeRepository.save(convertSizeRequestDTO(sizeRequest));
    }
    public boolean updateStatus(Long id){
        try{
            Optional<Size> size = sizeRepository.findById(id);
            if(!size.isPresent()){
                throw new RuntimeException("Size null");
            }
            String newStatus = size.get().getStatus().equals(Status.ACTIVE.toString())  ? "INACTIVE" : "ACTIVE";
            size.get().setStatus(newStatus);
            sizeRepository.save(size.get());
            return true;
        }catch (Exception e){
            e.getMessage();
            System.out.println(e.getMessage());
            return false;
        }

    }
    public Size convertSizeRequestDTO(SizeRequest sizeRequest){
        Size size = Size.builder()
                .name(sizeRequest.getName())
                .build();
        size.setStatus(Status.ACTIVE.toString());
        return size;
    }

}
