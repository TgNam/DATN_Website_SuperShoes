package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.ColorRequest;
import org.example.datn_website_supershoes.dto.response.ColorResponse;
import org.example.datn_website_supershoes.model.Color;
import org.example.datn_website_supershoes.model.Color;
import org.example.datn_website_supershoes.repository.ColorRepository;
import org.springframework.beans.BeanUtils;
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
    public boolean updateStatus(Long id){
        try{
            Optional<Color> color = colorRepository.findById(id);
            if(!color.isPresent()){
                throw new RuntimeException("Color null");
            }
            String newStatus = color.get().getStatus().equals(Status.ACTIVE.toString())  ? "INACTIVE" : "ACTIVE";
            color.get().setStatus(newStatus);
            colorRepository.save(color.get());
            return true;
        }catch (Exception e){
            e.getMessage();
            System.out.println(e.getMessage());
            return false;
        }

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
