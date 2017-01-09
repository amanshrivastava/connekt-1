package com.flipkart.connekt.callback.topologies

import akka.stream.scaladsl.Source
import com.flipkart.connekt.busybees.tests.streams.TopologyUTSpec
import com.flipkart.connekt.commons.entities.SubscriptionEvent
import com.flipkart.connekt.commons.helpers.RMQProducer
import com.flipkart.connekt.commons.iomodels.PNCallbackEvent
import com.flipkart.connekt.commons.metrics.Instrumented
import com.flipkart.connekt.commons.utils.StringUtils
import com.flipkart.connekt.firefly.sinks.rmq.RMQSink

class RMQTopologyTest extends TopologyUTSpec with Instrumented {
  "RMQTopology Test" should "run" in {
    val pncallback = PNCallbackEvent(messageId = StringUtils.generateRandomStr(8),
      clientId = StringUtils.generateRandomStr(8),
      deviceId = StringUtils.generateRandomStr(8),
      eventType = StringUtils.generateRandomStr(8),
      platform = StringUtils.generateRandomStr(8),
      appName = StringUtils.generateRandomStr(8),
      contextId = StringUtils.generateRandomStr(8))

    Source.single(SubscriptionEvent(payload = pncallback)).runWith(new RMQSink("mycallback", new RMQProducer("localhost", "guest", "guest", List("mycallback"))).sink)
    Thread.sleep(1000)
  }

}
