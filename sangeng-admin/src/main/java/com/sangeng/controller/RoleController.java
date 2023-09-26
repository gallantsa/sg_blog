package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.ChangeRoleStatusDto;
import com.sangeng.domain.entity.Role;
import com.sangeng.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult list(Role role, Integer pageNum, Integer pageSize) {
        return roleService.selectRolePage(role, pageNum, pageSize);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeRoleStatusDto roleDto) {
        Role role = new Role();
        role.setId(roleDto.getRoleId());
        role.setStatus(roleDto.getStatus());
        roleService.updateById(role);
        return ResponseResult.okResult();
    }

    /**
     * 新增角色
     */
    @PostMapping
    public ResponseResult add( @RequestBody Role role)
    {
        roleService.insertRole(role);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable Long id) {
        Role role = roleService.getById(id);
        return ResponseResult.okResult(role);
    }

    @PutMapping
    public ResponseResult edit(@RequestBody Role role) {
        roleService.updateRole(role);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult remove(@PathVariable Long id) {
        roleService.removeById(id);
        return ResponseResult.okResult();
    }
}
