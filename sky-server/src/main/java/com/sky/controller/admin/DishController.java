package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;


    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dto){
        log.info("新增菜品：{}", dto);
        dishService.saveWithFlavor(dto);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("菜品查询")
    public Result<PageResult> page(DishPageQueryDTO dto){
        log.info("菜品查询：{}",dto);
        PageResult pageResult = dishService.pageQuery(dto);
        return Result.success(pageResult);
    }
}
