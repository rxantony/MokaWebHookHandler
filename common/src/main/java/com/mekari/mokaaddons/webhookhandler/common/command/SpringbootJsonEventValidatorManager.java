package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.util.SingletonUtil;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class SpringbootJsonEventValidatorManager implements JsonEventValidatorManager {
    private @Autowired ApplicationContext appContext;
    private @Autowired JsonEventValidator defaultValidator;
    /**
     * do not use this default constructor, using another parameterized constructors
     * for manual instantiation instead.
     * this constuctor is neccessary by springboot to instantiate this class.
     */
    public SpringbootJsonEventValidatorManager() {
    }

    public SpringbootJsonEventValidatorManager(ApplicationContext appContext, JsonEventValidator defaultValidator) {
        Assert.notNull(appContext, "appContext");
        this.appContext = appContext;
        this.defaultValidator = defaultValidator;
    }
    @Override
    public JsonEventValidator crateValidator(String eventName) {
        try{
            return appContext.getBean(eventName+ "-validator", JsonEventValidator.class);
        }
        catch(NoSuchBeanDefinitionException ex){
            return null;
        }
    }

    @Override
    public JsonEventValidator getDeafultValidator() {
        return defaultValidator != null ? defaultValidator : SingletonUtil.DEFAULT_JSONEVENT_VALIDATOR;
    }
    
}
