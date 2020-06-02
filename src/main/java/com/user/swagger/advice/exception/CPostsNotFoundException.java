package com.user.swagger.advice.exception;

public class CPostsNotFoundException extends RuntimeException {
    public CPostsNotFoundException(String msg, Throwable t) { super(msg, t); }

    public CPostsNotFoundException(String msg) { super(msg); }

    public CPostsNotFoundException() {
        super();
    }
}