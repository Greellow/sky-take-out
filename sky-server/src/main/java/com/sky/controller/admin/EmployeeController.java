package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "员工退出")
    public Result<String> logout() {
        return Result.success();
    }

    @PostMapping
    @ApiOperation(value = "新增员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        log.info("新增员工：{}", employeeDTO);
        //System.out.println("当前的线程id" + Thread.currentThread().getId());
        employeeService.save(employeeDTO);
        return Result.success();
    }

    //进行分页功能
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public Result<PageResult> queryPage(EmployeePageQueryDTO dto){
        log.info("分页查询，员工参数为：{}", dto);
        PageResult pageResult =employeeService.pageQuery(dto);
        return Result.success(pageResult);
    }

    //进行启用禁用员工账号功能
    @PostMapping("/status/{status}")
    @ApiOperation(value = "是否禁用员工账号")
    public Result startOrStop(@PathVariable Integer status,Long id){
        log.info("是否启用员工账号，status is {},id is {}",status,id);
        employeeService.startOrStop(status,id);
        return Result.success();
    }

    //根据id查询员工信息
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询员工相关信息")
    public Result<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息"+"id is {}",id);
        Employee emp = employeeService.getById(id);
        return Result.success(emp);
    }

    //修改员工信息
    @PutMapping
    @ApiOperation(value = "修改员工信息")
    public Result update(@RequestBody EmployeeDTO dto){
        log.info("员工信息修改，参数为：{}", dto);
        employeeService.update(dto);
        return Result.success(dto);
    }

}
