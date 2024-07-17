package com.bision.mini.controller;

import com.bision.mini.dto.BoardDTO;
import com.bision.mini.dto.BoardFileDTO;
import com.bision.mini.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/save")
    public String save(){
        return "save";
    }

    @PostMapping("/save")
    public String save(BoardDTO boardDTO) throws IOException {
        boardService.save(boardDTO);
        return "redirect:/list";
    }

    /*
     * 글의 목록을 불러온다. 목록화면에서는 글의 상세내용이 필요 없기 때문에 실제로 보여지는 데이터만 가져오면 된다.
     * 떄문에 sql문도 필요없는 데이터는 작성하지 않도록 한다.
     * */
    @GetMapping("/list")
    public String findAll(Model model){
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "list";
    }

    /*
     * @PathVariable을 통해 url에 id만 주어졌을 때 parameter를 읽어올 수 있도록 함
     *
     * */
    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        //조회수 처리
        boardService.updateHits(id);
        //상세내용 가져오기
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        if(boardDTO.getFileAttached() == 1){
            List<BoardFileDTO> boardFileDTOList = boardService.findFile(id);
            model.addAttribute("boardFileDTOList", boardFileDTOList);
        }
        return "detail";
    }
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, Model model){
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        return "update";
    }

    @PostMapping("/update/{id}")
    public String update(BoardDTO boardDTO, Model model){
        boardService.update(boardDTO);
        BoardDTO dto = boardService.findById(boardDTO.getId());
        model.addAttribute("board", dto);
        return "detail";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        boardService.delete(id);
        return "redirect:/list";
    }
}