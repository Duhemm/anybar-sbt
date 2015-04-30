package com.github.duhemm

import sbt._
import Keys._

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

/**
 * Represents a running instance of AnyBar
 */
private[duhemm] case class AnyBarInstance(port: Int, private val newInstance: Boolean) {

  private val sock = new DatagramSocket
  private val addr = InetAddress.getByName("localhost");

  if (newInstance) {
    AnyBarPlugin addInstance this
    Process(Seq(AnyBarPlugin.anyBarLocation + "/AnyBar.app/Contents/MacOS/Anybar"), None, ("ANYBAR_PORT", port.toString)).run
  }

  /**
   * Send `word` to the current instance
   */
  def <<(word: String): Unit = {
    val bytes = word.getBytes
    val packet = new DatagramPacket(bytes, bytes.length, addr, port)
    sock send packet
  }

  /**
   * Tell this instance of AnyBar to show the result of running `task`.
   */
  def showResultOf[T](task: TaskKey[T]): Command = {
    val name = task.key.label
    Command.command(name) { (state: State) =>

      // Reinitialize the bubble
      this << "question"

      // Evaluate the task
      // None if the key is not defined
      // Some(Inc) if the task does not complete successfully (Inc for incomplete)
      // Some(Value(v)) with the resulting value
      val result = Project.evaluateTask(task, state)

      // handle the result
      result match {
        case None =>
          this << "black"

        case Some(Inc(inc)) =>
          this << "red"

        case Some(Value(v)) =>
          this << "green"
      }

      state
    }
  }

  /**
   * Tell this instance of AnyBar to show the results of running these `tasks`
   */
  def showResultOf(tasks: TaskKey[_]*): Seq[Command] = tasks map (this showResultOf _)

  /**
   * Terminate this instance of AnyBar
   */
  def exit: Unit = this << "quit"
}
