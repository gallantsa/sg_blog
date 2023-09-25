package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Role;
import com.sangeng.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult list(Role role, Integer pageNum, Integer pageSize) {
        return roleService.selectRolePage(role, pageNum, pageSize);
    }
}
