package com.mekari.mokaaddons.webhookhandler.query.getUser;

import com.mekari.mokaaddons.webhookhandler.common.handler.RequestHandler;

import org.springframework.stereotype.Component;

@Component
public class Handler implements RequestHandler<Request, User>{

    //just for testing
    @Override
    public User handle(Request request) {
        return User.builder().id(request.getId()).build();
    }
    
}
