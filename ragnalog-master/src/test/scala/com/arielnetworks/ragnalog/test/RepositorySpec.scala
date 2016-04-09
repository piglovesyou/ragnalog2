package com.arielnetworks.ragnalog.test

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri}
import org.elasticsearch.action.index.IndexRequest.OpType
import org.elasticsearch.common.settings.Settings
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{DiagrammedAssertions, FunSpec}

import scala.concurrent.ExecutionContext

class RepositorySpec extends FunSpec with DiagrammedAssertions with ScalaFutures {

  describe("Test should work") {
    val settings = Settings.settingsBuilder()
      .put("cluster.name", "ragnalog.elasticsearch")

    val client = ElasticClient.transport(settings.build,ElasticsearchClientUri("elasticsearch://localhost:9300"))

    val f = client.execute {
      index into ".ragnalog2" / "container" id "test_id2" opType OpType.CREATE fields(
        "name" -> "test-name",
        "description" -> "test-description"
        )
    }
    import ExecutionContext.Implicits.global

    f onFailure{
      case e=>
    }

//    whenReady(f.failed) { result =>
//      assert(result == "test_id2")
//    }
    whenReady(f) { result =>
      assert(result.getId == "test_id2")
    }
  }


  // NoNodesAvailableException
  // recover node

}
