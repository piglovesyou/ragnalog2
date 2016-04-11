package com.arielnetworks.ragnalog.domain.model.container

import com.arielnetworks.ragnalog.domain.model.common.{Entity, Identifier}

import scala.concurrent.Future

object ContainerStatus extends Enumeration {
  val Active, Inactive = Value
}


case class ContainerId(value: String) extends Identifier[String]

case class Container
(
  id: ContainerId,
  name: String,
  description: Option[String],
  status: ContainerStatus.Value
)
  extends Entity[ContainerId] {

  def activate(): Future[Unit] = ???

  def deactivate(): Future[Unit] = ???

  def save(): Future[Unit] = ???
}
