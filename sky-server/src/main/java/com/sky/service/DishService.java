package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.service.impl.DishServiceImpl;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface DishService {


    void saveWithFlavor(DishDTO dto);

    PageResult pageQuery(DishPageQueryDTO dto);


    void deleteBatch(List<Long> ids);

    DishVO getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDTO dto);

    List<Dish> list(Long categoryId);
}
