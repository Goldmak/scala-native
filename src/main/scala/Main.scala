import sttp.client4.DefaultSyncBackend

@main def run(): Unit = {
  val backend = DefaultSyncBackend()
  val client: HttpClient = new LiveHttpClient(backend)

  client.fetchData() match {
    case Right(dataList) =>
      dataList.headOption match {
        case Some(data) =>
          println("✅ Успешно получили данные!")
          println(s"  Ключ: ${data.key}")
          println(s"  Тип: ${data.`type`}")
          println(s"  Значение: ${data.value}")
        case None =>
          println("❌ Ошибка: Сервер вернул пустой список.")
      }
    case Left(error) =>
      println(s"❌ Ошибка: $error")
  }
}
