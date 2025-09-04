import org.scalatest.funsuite.AnyFunSuite
import sttp.client4.DefaultSyncBackend
import org.scalatest.exceptions.TestFailedException

class HttpSuite extends AnyFunSuite {

  val realClient: HttpClient = new LiveHttpClient(DefaultSyncBackend())

  test("Значение ответа должно быть равно 100") {
    val result = realClient.fetchData()
    assert(result.isRight)
    assert(result.getOrElse(List.empty).head.value == 100)
  }

  ignore("Значение ответа должно быть больше 100") {
    val result = realClient.fetchData()
    assert(result.getOrElse(List.empty).head.value > 100)
  }

  test("Проверка с intercept должна поймать ожидаемое падение") {
    val result = realClient.fetchData()
    assert(result.isRight)
    intercept[TestFailedException] {
      assert(result.getOrElse(List.empty).isEmpty)
    }
  }

  test("Логика должна правильно обрабатывать пустой массив от сервера") {
    class MockHttpClientForEmptyList extends HttpClient {
      override def fetchData(): Either[String, List[DataEntry]] = Right(List.empty)
    }

    val mockClient: HttpClient = new MockHttpClientForEmptyList()
    val result = mockClient.fetchData()
    assert(result.isRight)
    assert(result.getOrElse(Nil).isEmpty)
  }
  test("Проверка с intercept должна поймать ожидаемое падение с моком") {
    class MockHttpClientForEmptyList extends HttpClient {
      override def fetchData(): Either[String, List[DataEntry]] = Right(List.empty)
    }

    val mockClient: HttpClient = new MockHttpClientForEmptyList()

    val result = mockClient.fetchData()
    assert(result.isRight)
    intercept[TestFailedException] {
      assert(result.getOrElse(List.empty).isEmpty)
    }
  }

}
