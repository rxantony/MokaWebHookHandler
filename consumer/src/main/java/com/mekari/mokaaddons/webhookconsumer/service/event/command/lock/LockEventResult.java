package com.mekari.mokaaddons.webhookconsumer.service.event.command.lock;

import org.springframework.util.Assert;

import com.mekari.mokaaddons.common.webhook.event.moka.AbstractEvent;
import com.mekari.mokaaddons.common.webhook.persistence.storage.LockTrackerStorage.NewLockTracker;


public class LockEventResult implements AutoCloseable {
    public interface ReleaseLock {
        void execute() throws Exception;
    }

    private final NewLockTracker lockInfo;
    private final AbstractEvent event;
    private final ReleaseLock releaseLock;
    
    public LockEventResult(AbstractEvent event, NewLockTracker lockInfo, ReleaseLock releaseLock){

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

    public NewLockTracker getInfo(){
        return lockInfo;
    }

    @Override
    public void close() throws Exception {
        releaseLock.execute();
    }
}
