package com.mekari.mokaaddons.webhookhandler.common.handler;

import java.lang.reflect.ParameterizedType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class SpringbootRequestHandlerManager implements RequestHandlerManager {

    private @Autowired ApplicationContext appContext;

    /**
     * do not use this default constructor, using another parameterized constructors
     * for manual instantiation instead.
     * this constuctor is neccessary for springboot to instantiate this class.
     */
    public SpringbootRequestHandlerManager() {
    }

    public SpringbootRequestHandlerManager(ApplicationContext appContext) {
        Assert.notNull(appContext, "appContext");
        this.appContext = appContext;
    }

    @Override
    @SuppressWarnings("unchecked") 
    public <TRequest extends RequestParam<TResult>, TResult> TResult handle(TRequest request) throws Exception{
        var resultCls = gertResultClassFromRequest(request.getClass());
        var queryHandlerType = ResolvableType.forClassWithGenerics(RequestHandler.class, request.getClass(), resultCls);
        var queryHandler = (RequestHandler<TRequest, TResult>)appContext.getBean(queryHandlerType.resolve());
        return queryHandler.handle(request);
    }

    @SuppressWarnings("unchecked")
    private static <TRequest, TResult> Class<TResult> gertResultClassFromRequest(Class<TRequest> requestCls) {
        var interfaces = requestCls.getGenericInterfaces();
        if (interfaces.length > 0) {
            return (Class<TResult>) ((ParameterizedType) requestCls.getGenericInterfaces()[0])
                    .getActualTypeArguments()[0];
        }
        return (Class<TResult>) ((ParameterizedType) requestCls.getGenericSuperclass()).getActualTypeArguments()[0];
    }
    
}
