package com.sedocefosse.backend.service;

import com.sedocefosse.backend.dto.CommentDTO;
import com.sedocefosse.backend.model.Comment;

public interface CommentService {

    public CommentDTO create(Comment comentario);

}
