package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result delete (@RequestParam List<Long> ids){
        log.info("删除菜品：{}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    //根据id查询菜品信息
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品信息")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("根据id查询菜品信息：{}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dto){
        log.info("修改菜品:{}",dto);
        dishService.updateWithFlavor(dto);
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

}
