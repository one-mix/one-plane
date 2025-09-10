//package com.oneplane.myBoard.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/board")
//public class MyBoardController {
//
//
//    @GetMapping("/myboard")
//    public String myBoard(Model model) {
//        model.addAttribute("contentPage", "myBoard/myBoard.jsp");
//
//        return "layout/layout";
//    }
//}
//
//


package com.oneplane.myBoard.controller;

import jakarta.servlet.http.HttpSession;
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
        model.addAttribute("contentPage", "board/myBoard.jsp");
        model.addAttribute("pageTitle", "내가 작성한 글");

        return "layout/layout";
    }
}
