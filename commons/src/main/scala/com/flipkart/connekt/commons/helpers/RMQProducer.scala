/*
 *         -╥⌐⌐⌐⌐            -⌐⌐⌐⌐-
 *      ≡╢░░░░⌐\░░░φ     ╓╝░░░░⌐░░░░╪╕
 *     ╣╬░░`    `░░░╢┘ φ▒╣╬╝╜     ░░╢╣Q
 *    ║╣╬░⌐        ` ╤▒▒▒Å`        ║╢╬╣
 *    ╚╣╬░⌐        ╔▒▒▒▒`«╕        ╢╢╣▒
 *     ╫╬░░╖    .░ ╙╨╨  ╣╣╬░φ    ╓φ░╢╢Å
 *      ╙╢░░░░⌐"░░░╜     ╙Å░░░░⌐░░░░╝`
 *        ``˚¬ ⌐              ˚˚⌐´
 *
 *      Copyright © 2016 Flipkart.com
 */
package com.flipkart.connekt.commons.helpers

import com.rabbitmq.client.{Address, Channel, Connection, ConnectionFactory}
import com.flipkart.connekt.commons.utils.StringUtils._
import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters._


class RMQProducer {
  var factory: ConnectionFactory = _
  var connection: Connection = _
  var channel: Channel = _


  def this(producerFactoryConf: Config) {
    this()
    this.factory = new ConnectionFactory()
    this.factory.setUsername(producerFactoryConf.getString("rmq.username"))
    this.factory.setPassword(producerFactoryConf.getString("rmq.password"))
    val addresses = producerFactoryConf.getString("rmq.host").split(",").map(new Address(_))
    this.connection = factory.newConnection(addresses)
    this.channel = connection.createChannel()
    producerFactoryConf.getStringList("rmq.queues").asScala.foreach(this.channel.queueDeclare(_, true, false, false, null))
  }

  def this(host: String, username: String, password: String, queues: List[String]) {
    this(ConfigFactory.parseMap(Map(
      "rmq.username" -> username,
      "rmq.password" -> password,
      "rmq.host" -> host,
      "rmq.queues" -> queues.asJava
    ).asJava))
  }


  def writeMessage(queue: String, message: AnyRef) = {
    channel.basicPublish("", queue, null, message.getJson.getBytes)
  }

  def close() = {
    this.channel.close()
    this.connection.close()
  }

}
