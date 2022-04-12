package com.mekari.mokaaddons.webhookconsumer.webhook.service.event.lock;

import com.mekari.mokaaddons.common.webhook.LockTrackerStorage.NewItem;
import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;

import org.springframework.util.Assert;


public class LockEventResult implements AutoCloseable {
    public interface ReleaseLock {
        void execute() throws Exception;
    }

    private final NewItem lockInfo;
    private final AbstractEvent event;
    private final ReleaseLock releaseLock;
    
    public LockEventResult(AbstractEvent event, NewItem lockInfo, ReleaseLock releaseLock){

        Assert.notNull(event, "event must not be null");
        Assert.notNull(lockInfo, "lockInfo must not be null");
        Assert.notNull(releaseLock, "releaseLock must not be null");

        this.event = event;
        this.lockInfo = lockInfo;
        this.releaseLock = releaseLock;
    }

    public AbstractEvent getEvent(){
        return event;
    }

    public NewItem getInfo(){
        return lockInfo;
    }

    @Override
    public void close() throws Exception {
        releaseLock.execute();
    }
}
