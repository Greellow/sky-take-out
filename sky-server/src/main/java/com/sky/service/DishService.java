package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.service.impl.DishServiceImpl;
import org.springframework.stereotype.Service;

@Service

public interface DishService {


    void saveWithFlavor(DishDTO dto);
}
