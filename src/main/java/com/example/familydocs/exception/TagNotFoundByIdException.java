package com.example.familydocs.exception;

public class TagNotFoundByIdException extends RuntimeException {
    public TagNotFoundByIdException(Long tagId) {
        super("Tag with id " + tagId + " not found.");
    }
}
