package com.mekari.mokaaddons.api.config;

public class AppConstant {
    public static class EventName {
        public static final String MOKA_EVENT_RECEIVED = "webHookEventReceived";
    }

    public static class ExchangeName{
        public static final String MOKA_EVENT_RECEIVED_EXCHANGE = "webHookEventReceived";
    }

    public static class QueueName {
        public static final String MOKA_EVENT_RECEIVED_QUEUE = "webHookEventReceivedQueue";   
    }  
}
