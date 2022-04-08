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
        var requestCls = request.getClass();
        var queryHandlerType = ResolvableType.forClassWithGenerics(RawRequestHandler.class,
                ResolvableType.forClass(requestCls));
        var beanName = appContext.getBeanNamesForType(queryHandlerType);
        if(beanName.length == 0)
            throw new IllegalArgumentException("No handler for request:" + request.getClass().getName());
        var queryHandler = (RequestHandler<TRequest, TResult>) appContext.getBean(beanName[beanName.length - 1]);
        return queryHandler.handle(request);
    }

}
