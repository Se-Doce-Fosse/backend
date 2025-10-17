package com.sedocefosse.backend.controller;

import com.sedocefosse.backend.dto.CommentDTO;
import com.sedocefosse.backend.model.Comment;
import com.sedocefosse.backend.service.CommentServiceImpl;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private CommentServiceImpl commentService;

    @PostMapping()
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO comentario){
        Comment comentarioEntity = new Comment(comentario.getId(), comentario.getPedidoId(), comentario.getClienteId(), comentario.getNota(), comentario.getDescricao(), comentario.getNomeExibicao());
        return ResponseEntity.ok(commentService.create(comentarioEntity));
    }

}
