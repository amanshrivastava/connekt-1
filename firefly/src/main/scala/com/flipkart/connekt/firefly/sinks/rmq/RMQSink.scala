package com.flipkart.connekt.firefly.sinks.rmq

import akka.stream.scaladsl.Sink
import com.flipkart.connekt.commons.entities.SubscriptionEvent
import com.flipkart.connekt.commons.factories.{ConnektLogger, LogFile}
import com.flipkart.connekt.commons.helpers.RMQProducer
import com.flipkart.connekt.commons.metrics.Instrumented
import com.rabbitmq.client.ConnectionFactory
import com.flipkart.connekt.commons.utils.StringUtils._

class RMQSink(queue: String, rmqProducer: RMQProducer) extends Instrumented {

  def sink = Sink.foreach[SubscriptionEvent](e => {
    ConnektLogger(LogFile.SERVICE).info(s"Received callback event ${e.getJson}")
    rmqProducer.writeMessage(queue, e.payload)
  })


}
