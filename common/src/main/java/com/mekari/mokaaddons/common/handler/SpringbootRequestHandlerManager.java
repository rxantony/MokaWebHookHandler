package com.mekari.mokaaddons.common.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class SpringbootRequestHandlerManager implements RequestHandlerManager {

    private ApplicationContext appContext;

    public SpringbootRequestHandlerManager(@Autowired ApplicationContext appContext) {
        Assert.notNull(appContext, "appContext");
        this.appContext = appContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TRequest extends Request<TResult>, TResult> TResult handle(TRequest request) throws Exception {
        if(request == null) throw new IllegalArgumentException("request must not be null");
        if(request instanceof Validateable){
            var validateable = (Validateable)request;
            validateable.validate();
        }

        var requestCls = request.getClass();
        var requestHandlerType = ResolvableType.forClassWithGenerics(RequestHandler.getRawClass(),
                ResolvableType.forClass(requestCls));
                
        var beanNames = appContext.getBeanNamesForType(requestHandlerType);
        if(beanNames.length == 0)
            throw new IllegalArgumentException("No handler for request:" + request.getClass().getName());
        
        var requestHandler = (RequestHandler<TRequest, TResult>) appContext.getBean(beanNames[beanNames.length - 1]);
        return requestHandler.handle(request);
    }

}
