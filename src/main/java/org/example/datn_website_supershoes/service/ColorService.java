package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.ColorRequest;
import org.example.datn_website_supershoes.dto.response.ColorResponse;
import org.example.datn_website_supershoes.model.Color;
import org.example.datn_website_supershoes.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColorService {

    @Autowired
    private ColorRepository colorRepository;

    public List<ColorResponse> findAllColor(){
        return colorRepository.findAllColor();
    }
    public List<ColorResponse> findColorByStatusACTIVE(){
        return colorRepository.findColorByStatus(Status.ACTIVE.toString());
    }
    public Color createColor(ColorRequest ColorRequest){
        Optional<Color> color = colorRepository.findByCodeColor(ColorRequest.getCodeColor());
        if(color.isPresent()){
            throw new RuntimeException("Màu "+ ColorRequest.getCodeColor() +" đã tồn tại");
        }
        return colorRepository.save(convertColorRequestDTO(ColorRequest));
    }
    public Color updateStatus(Long id, boolean aBoolean){
            Optional<Color> color = colorRepository.findById(id);
            if(!color.isPresent()){
                throw new RuntimeException("Id "+color.get().getId()+" của màu sắc không tồn tại");
            }
        String newStatus = aBoolean ? Status.ACTIVE.toString() : Status.INACTIVE.toString();
            color.get().setStatus(newStatus);
            return colorRepository.save(color.get());

    }
    public Color convertColorRequestDTO(ColorRequest ColorRequest){
        Color color = Color.builder()
                .name(ColorRequest.getName())
                .codeColor(ColorRequest.getCodeColor())
                .build();
        color.setStatus(Status.ACTIVE.toString());
        return color;
    }
}
