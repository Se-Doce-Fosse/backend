package com.sedocefosse.backend.service.customer;

import com.sedocefosse.backend.dto.CommentDTO;
import com.sedocefosse.backend.model.Comment;
import java.util.List;

public interface CommentService {

    public CommentDTO create(Comment comentario);
    
    public List<CommentDTO> getAll();

}
