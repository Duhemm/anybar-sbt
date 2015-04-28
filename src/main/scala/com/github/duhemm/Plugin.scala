package com.github.duhemm

import sbt._
import Keys._

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.URL

object AnyBarPlugin extends AutoPlugin {

  override def requires = empty
  override def trigger = allRequirements

  object autoImport {
    def showResultOf[T](task: TaskKey[T]) = {
      val name = task.key.label
      Command.command(name) { (state: State) =>

        // Reinitialize the bubble
        send("question")

        // Evaluate the task
        // None if the key is not defined
        // Some(Inc) if the task does not complete successfully (Inc for incomplete)
        // Some(Value(v)) with the resulting value
        val result = Project.evaluateTask(task, state)

        // handle the result
        result match {
          case None =>
            send("black")

          case Some(Inc(inc)) =>
            send("red")

          case Some(Value(v)) =>
            send("green")
        }

        state
      }
    }
  }

  private val anyBarDefaultLocations = List(
    "/Applications/",
    System.getProperty("user.home") + "/Applications/",
    "./.AnyBar/")

  private val sock = new DatagramSocket
  private val addr = InetAddress.getByName("localhost");

  // Randomly generate a port on which AnyBar will start listening.
  // This port will also be used to identify this AnyBar instance.
  val port = 1000 + scala.util.Random.nextInt(1000)
  val instanceIdentifier = s"anybar-sbt on port $port"

  /**
   * Downloads AnyBar.app if necessary, unzips it and sets the necessary permissions.
   * Returns the location where AnyBar.app can be found.
   */
  private def setupPlugin(): String = {
    val latestAnyBarVersion = "0.1.3"

    anyBarDefaultLocations find { l =>
      val location = new File(l + "AnyBar.app")
      location.exists && location.isDirectory
    } getOrElse {
      val location = anyBarDefaultLocations.last
      val addr = s"https://github.com/tonsky/AnyBar/releases/download/$latestAnyBarVersion/AnyBar-$latestAnyBarVersion.zip"
      if (new File(location).mkdir) {
        IO.unzipURL(new URL(addr), new File(location))
        Process(Seq("/bin/chmod", "u+x", location + "/AnyBar.app/Contents/MacOS/Anybar")).!
        location
      } else throw AutoPluginException("Could not find nor install AnyBar")
    }
  }

  /**
   * Sends `word` to anybar.
   */
  private def send(word: String): Unit = {
    val bytes = word.getBytes
    val packet = new DatagramPacket(bytes, bytes.length, addr, port)
    sock.send(packet)
  }

  /**
   * Adds an `ExitHook` to sbt so that sbt will kill the current instance of AnyBar when exiting.
   */
  def registerExitHook: State => State = (s: State) => {
    s.addExitHook {
      Process(Seq("pgrep", "-f", instanceIdentifier)).lines.headOption map (pid => Process("kill " + pid).!)
    }
  }

  // Start anybar
  // We add the instance identifier to the command to be able to easily find back this instance when we exit sbt.
  // Even if the instance identifier is not a valid command, this works because we will never actually try to run it.
  // Yes, this is an ugly hack :)
  // TODO: Find a better place to put AnyBar
  val anyBarLocation = setupPlugin()
  Process(Seq(anyBarLocation + "/AnyBar.app/Contents/MacOS/Anybar", "&&", instanceIdentifier), None, ("ANYBAR_PORT", port.toString)).run

  // Terminate this instance of AnyBar when exiting sbt
  override def projectSettings = Seq(onUnload in Global ~= (registerExitHook compose _))

}
