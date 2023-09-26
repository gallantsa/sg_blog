package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Link;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult list(Link link, Integer pageNum, Integer pageSize) {
        PageVo pageVo = linkService.selectLinkPage(link, pageNum, pageSize);
        return ResponseResult.okResult(pageVo);
    }

    @PostMapping
    public ResponseResult add(@RequestBody Link link) {
        linkService.save(link);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable Long id) {
        Link link = linkService.getById(id);
        return ResponseResult.okResult(link);
    }

    @PutMapping
    public ResponseResult edit(@RequestBody Link link) {
        linkService.updateById(link);
        return ResponseResult.okResult();
    }
}
