package com.mekari.mokaaddons.webhookhandler.config;

public class AppConstant {
    public static class EventName {
        public static final String MOKA_EVENT_RECEIVED = "webHookEventReceived";
        public static final String MOKA_EVENT_PROCESSED = "webHookEventProcessed";
    }

    public static class ExchangeName {
        public static final String MOKA_EVENT_RECEIVED_EXCHANGE = "webHookEventReceived";
        public static final String MOKA_EVENT_PROCESSED_EXCHANGE = "webHookEventProcessed";
        public static final String MOKA_DLX = "webHookDeadLetter.dlx";
    }

    public static class QueueName {
        public static final String MOKA_EVENT_RECEIVED_QUEUE = "webHookEventReceivedQueue";
        public static final String MOKA_EVENT_PROCESSED_QUEUE = "webHookEventProcessedQueue";
        public static final String MOKA_DLQ = "webHookDeadLetter.dlq";
    }
}
