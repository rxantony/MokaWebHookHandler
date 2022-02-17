# MokaWebhookHandler

This projects just reached 60% from it's completion and has a goal to handle unordered incoming webhook events sent by Mokapos while processing them sequentially based on it modified date in parallel, leveraging MySql row locking to scynronize distributted message processing on RabbitMQ consumers.

### Tech Stacks

* [Oracle](https://www.oracle.com/java/technologies/downloads/) [ / OpenJdk java 11 or higher](https://openjdk.java.net)
* [Mysql 8 or higher](https://www.mysql.com)
* [RabbitMQ 3.6.15 or higher](https://www.rabbitmq.com)
* [Apache Maven](https://maven.apache.org)
* [Springboot 2.6.3](https://spring.io/projects/spring-boot)

### How to debug and run the app

* **On RabbitMq**

    * Create webHookEventReceived exchange.
    * Create webHookEventProcessed exchane.
    * Create webHookEventReceivedQueue and bind it to webHookEventReceived exchange.
    * Create webHookEventProcessedQueue and bind it to webHookEventProcessed exchange
    
* **On Mysql**

    * Create eventstore\_db database.
    * Create event_source table by running event\_source.sql script.
    * Create lock_tracker table by running lock\_tracker.sql script.
    * Create dead_letter table by running dead\_letter.sql script.      
    * Create mokaaddson\_db database.
    * Create item table by running item.sql script.
    
* **Debugging On VS Code**

    * Open this solution and select "open and debug" menu on left panels, chose "Debug-Webhookhandler-consumer-api" to run all projects  
      at the same time
    
* **Running the app**
    * .

**Note**: all the classes in common folder are going to be moved into separate project as a library.
