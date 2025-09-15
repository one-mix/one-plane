package com.oneplane.post.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class MyBoardController{

    @GetMapping("/myboard")
    public String myBoard(Model model) {


        // 실제 게시글 목록을 Service/DAO에서 가져와 model에 추가한다면 여기에 추가
        // model.addAttribute("posts", myBoardService.getMyPosts(userId));

        model.addAttribute("activeMenu", "write");
        model.addAttribute("showSidebar", false);
        model.addAttribute("contentPage", "post/myBoard.jsp");
        model.addAttribute("pageTitle", "내가 작성한 글");

        return "layout/layout";
    }


}
