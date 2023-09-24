package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddArticleDto;
import com.sangeng.domain.dto.ArticleDto;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.vo.ArticleVo;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article){
        return articleService.add(article);
    }

    @GetMapping("/list")
    public ResponseResult list(Article article, Integer pageNum, Integer pageSize) {
        PageVo pageVo = articleService.selectArticlePage(article, pageNum, pageSize);
        return ResponseResult.okResult(pageVo);
    }

    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable Long id) {
        ArticleVo articleVo = articleService.getInfo(id);
        return ResponseResult.okResult(articleVo);
    }

    @PutMapping
    public ResponseResult edit(@RequestBody ArticleDto articleVo) {
        articleService.edit(articleVo);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id) {
        articleService.removeById(id);
        return ResponseResult.okResult();
    }



}
