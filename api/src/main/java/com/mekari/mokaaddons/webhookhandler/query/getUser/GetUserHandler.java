package com.mekari.mokaaddons.webhookhandler.query.getUser;

import com.mekari.mokaaddons.webhookhandler.common.handler.RequestHandler;

import org.springframework.stereotype.Component;

@Component
public class GetUserHandler implements RequestHandler<GetUserRequest, User>{

    //just for testing
    @Override
    public User handle(GetUserRequest request) {
        return User.builder().id(request.getId()).build();
    }
    
}
