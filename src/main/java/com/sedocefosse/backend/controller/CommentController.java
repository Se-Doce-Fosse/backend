package com.sedocefosse.backend.controller;

import com.sedocefosse.backend.dto.CommentDTO;
import com.sedocefosse.backend.model.Comment;
import com.sedocefosse.backend.service.customer.CommentServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
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

    @Operation(summary = "Get a greeting message", description = "Returns a personalized greeting based on the provided name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of greeting"),
            @ApiResponse(responseCode = "400", description = "Invalid name supplied")
    })
    @PostMapping()
    public ResponseEntity<?> createComment(@RequestBody CommentDTO comentario){
        Comment comentarioEntity = new Comment(comentario.getId(), comentario.getPedidoId(), comentario.getClienteId(), comentario.getNota(), comentario.getDescricao(), comentario.getNomeExibicao());
        CommentDTO criado = commentService.create(comentarioEntity);
        if (criado == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuário deve fazer um pedido antes de poder comentar.");
        }
        return ResponseEntity.ok(criado);
    }

}
