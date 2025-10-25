package com.sedocefosse.backend.service.customer;

import org.springframework.stereotype.Service;

import com.sedocefosse.backend.dto.CommentDTO;
import com.sedocefosse.backend.model.Comment;
import com.sedocefosse.backend.repository.customer.CommentRepository;
import com.sedocefosse.backend.repository.order.OrderRepository;

@Service
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final OrderRepository orderRepository;
    
    public CommentServiceImpl(CommentRepository commentRepository, OrderRepository orderRepository){
        this.commentRepository = commentRepository;
        this.orderRepository = orderRepository;
    }
    
    @Override
    public CommentDTO create(Comment comentario){
        if (orderRepository.existsByClientId(comentario.getClienteId())) {
            Comment comentarioSalvo = commentRepository.save(comentario);
            return new CommentDTO(comentarioSalvo.getId(), comentarioSalvo.getPedidoId(), comentarioSalvo.getClienteId(), comentarioSalvo.getNota(), comentarioSalvo.getDescricao(), comentarioSalvo.getNomeExibicao());
        } else {
            return null;
        }
    }

}
