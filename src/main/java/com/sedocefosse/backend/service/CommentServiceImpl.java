package com.sedocefosse.backend.service;

import org.springframework.stereotype.Service;

import com.sedocefosse.backend.dto.CommentDTO;
import com.sedocefosse.backend.model.Comment;
import com.sedocefosse.backend.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    
    public CommentServiceImpl(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }
    
    @Override
    public CommentDTO create(Comment comentario){
        Comment comentarioSalvo = commentRepository.save(comentario);
        return new CommentDTO(comentarioSalvo.getId(), comentarioSalvo.getPedidoId(), comentarioSalvo.getClienteId(), comentarioSalvo.getNota(), comentarioSalvo.getDescricao(), comentarioSalvo.getNomeExibicao());
    }

}
