This is the simple spring boot application which cover different types of rabbitmq concepts.

For running application, be sure to have rabbitmq in your local machine.


Then use postman collection in _/postman_ directory to create queues and exchanges

At last, uncomment corresponding consumer **@Component** annotation for using consumers

For push messages to queue, use **ProducerController**