import sttp.client4._
import sttp.client4.circe._
import io.circe.generic.auto._

case class DataEntry(key: String, `type`: String, value: Int)

trait HttpClient {
  def fetchData(): Either[String, List[DataEntry]]
}

class LiveHttpClient(backend: SyncBackend) extends HttpClient {
  def fetchData(): Either[String, List[DataEntry]] = {
    val url = uri"https://devnet-swagger.wvservices.com/addresses/data/3FjPCqwixESjRkfm2mCfM4Edy8DyzmhfMnk?key=miner_3Fgtiv5L5q4CXFfwuAfbkjy9ppehkNzjbEG_SkippedEpochCount"
    val request = basicRequest
      .get(url)
      .response(asJson[List[DataEntry]])

    val response = request.send(backend)
    response.body.left.map(_.toString)
  }
}
