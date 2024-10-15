package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.ShoeSoleRequest;
import org.example.datn_website_supershoes.dto.response.ShoeSoleResponse;
import org.example.datn_website_supershoes.model.Category;
import org.example.datn_website_supershoes.model.ShoeSole;
import org.example.datn_website_supershoes.model.ShoeSole;
import org.example.datn_website_supershoes.repository.CategoryRepository;
import org.example.datn_website_supershoes.repository.ShoeSoleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ShoeSoleService {

    @Autowired
    private ShoeSoleRepository shoeSoleRepository;

    public List<ShoeSoleResponse> findByStatus(){
        return shoeSoleRepository.findByStatus();
    }
    public ShoeSole createShoeSole(ShoeSoleRequest shoeSoleRequest){
        Optional<ShoeSole> shoeSole = shoeSoleRepository.findByName(shoeSoleRequest.getName());
        if(shoeSole.isPresent()){
            throw new RuntimeException("Loại đế "+ shoeSoleRequest.getName() +" đã tồn tại");
        }
        return shoeSoleRepository.save(convertShoeSoleRequestDTO(shoeSoleRequest));
    }
    public ShoeSole updateStatus(Long id, String status){
            Optional<ShoeSole> shoeSole = shoeSoleRepository.findById(id);
            if(!shoeSole.isPresent()){
                throw new RuntimeException("Id "+shoeSole.get().getId()+" của loại đế giày không tồn tại");
            }
            shoeSole.get().setStatus(status);

            return shoeSoleRepository.save(shoeSole.get());


    }
    public ShoeSole convertShoeSoleRequestDTO(ShoeSoleRequest ShoeSoleRequest){
        ShoeSole shoeSole = ShoeSole.builder()
                .name(ShoeSoleRequest.getName())
                .build();
        shoeSole.setStatus(Status.ACTIVE.toString());
        return shoeSole;
    }

}
