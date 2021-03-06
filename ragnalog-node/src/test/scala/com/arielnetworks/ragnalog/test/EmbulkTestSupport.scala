package com.arielnetworks.ragnalog.test

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.util.zip.ZipInputStream

import com.arielnetworks.ragnalog.port.adapter.embulk.RegistrationConfiguration
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri}
import org.elasticsearch.common.settings.Settings

import scala.concurrent.Await
import scala.concurrent.duration._
import scalax.file.Path

trait EmbulkTestSupport {

  val userDir = Path(System.getProperty("user.dir"), '/')
  val embulkBinPath = userDir / Path("etc", "embulk", "bin", "embulk")
  val embulkBundleDir = userDir / Path("etc", "embulk")
  val embulkPluginsDir = embulkBundleDir / Path("jruby", "2.2.0", "gems")
  val embulkWorkingDir = userDir / Path("tmp", "embulk", "work")

  val baseParams = Map(
    "elasticsearch/host" -> "localhost",
    "elasticsearch/port" -> "9300",
    "elasticsearch/cluster_name" -> "ragnalog2.elasticsearch"
  )

  val grokPatternFilePath = embulkPluginsDir / Path("embulk-parser-grok-0.1.7", "pattern", "grok-patterns")
  val grokTemplate = Path(getClass.getClassLoader.getResource("template/access_without_filter.template").getPath, '/')
  val grokParams = Map("grok/grok_pattern_files" -> List(grokPatternFilePath.path))

  val apacheAccessConfig = RegistrationConfiguration(
    name = "Apache access log",
    description = None,
    parser = "grok",
    filters = Seq(),
    timeField = "timestamp",
    template = grokTemplate,
    doGuess = false,
    guessPluginNames = None,
    params = grokParams
  )

  val grokGuessTemplate = Path(getClass.getClassLoader.getResource("template/grok_guess.template").getPath, '/')

  def decodeZip(byteArray: Array[Byte]): String = {
    val zis = new ZipInputStream(new ByteArrayInputStream(byteArray))

    val output = new ByteArrayOutputStream()
    Iterator.continually(zis.getNextEntry)
      .takeWhile(_ != null).filterNot(_.isDirectory)
      .foreach(entry => {
        Iterator.continually(zis.read()).takeWhile(_ != -1).foreach(b => {
          output.write(b)
        })
      })
    zis.close()
    output.close()

    output.toString()
  }


  val elasticsearchSettings = Settings.settingsBuilder().put("cluster.name", "ragnalog2.elasticsearch")
  implicit val elasticClient = ElasticClient.transport(elasticsearchSettings.build, ElasticsearchClientUri("elasticsearch://localhost:9300"))
}
