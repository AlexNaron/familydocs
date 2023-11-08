package com.example.familydocs.exception;

public class TagNotFoundByNameException extends RuntimeException {
    public TagNotFoundByNameException(String tagName) {
        super("Tag with Name " + tagName + " not found.");
    }
}
