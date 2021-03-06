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
package com.flipkart.connekt.receptors.routes.common

import akka.http.scaladsl.model.StatusCodes
import akka.stream.ActorMaterializer
import com.flipkart.connekt.commons.iomodels.{GenericResponse, Response}
import com.flipkart.connekt.receptors.routes.BaseJsonHandler
import com.flipkart.connekt.receptors.service.AuthenticationService
import com.flipkart.connekt.receptors.wire.ResponseUtils._

class DebuggerRoute(implicit am: ActorMaterializer) extends BaseJsonHandler {

  val route =
    authenticate {
      user =>
        pathPrefix("v1") {
          pathPrefix("debugger") {
            authorize(user, "DEBUGGER") {
              path("secure-code" / "generate") {
                get {
                  parameters('keys) { k =>
                    val keys = k.split(',')
                    val code = AuthenticationService.generateSecureCode(keys.mkString(":"))
                    complete(GenericResponse(StatusCodes.OK.intValue, Map("k" -> keys), Response("Your Secure Code is", code)))
                  }
                }
              }
            }
          }
        }
    }
}
