# Контейнеризация с Docker: Написание Dockerfile и публикация образа

Docker — это платформа для разработки, доставки и запуска приложений в контейнерах. Контейнеры позволяют упаковать приложение со всеми его зависимостями в один изолированный "пакет", который можно легко переносить и запускать в любой среде.

## Зачем Docker для Scala Native?

*   **Изоляция:** Приложение работает в изолированной среде, что исключает конфликты зависимомостей с хост-системой.
*   **Портативность:** Один и тот же образ Docker можно запустить на любой машине с Docker, будь то локальный компьютер, сервер или облако.
*   **Воспроизводимость:** Гарантирует, что приложение всегда будет работать одинаково, независимо от окружения.
*   **Упрощенное развертывание:** Деплой сводится к запуску одного Docker-образа.

## Написание Dockerfile для Scala Native приложения

Dockerfile — это текстовый файл, содержащий инструкции для сборки Docker-образа. Для Scala Native приложения нам понадобится многоступенчатая сборка (multi-stage build), чтобы образ был максимально компактным.

### Пример Dockerfile

```dockerfile
# --- Этап 1: Сборка нативного исполняемого файла ---
# Используем базовый образ с JDK и sbt для сборки
FROM eclipse-temurin:17-jdk-jammy AS builder

# Устанавливаем необходимые системные зависимости для Scala Native
# (например, clang, zlib, libcurl, libidn2, libssl)
RUN apt-get update && apt-get install -y \
    clang \
    zlib1g-dev \
    libcurl4-openssl-dev \
    libidn2-dev \
    libssl-dev \
    pkg-config \
    git \
    && rm -rf /var/lib/apt/lists/*

# Устанавливаем sbt
ENV SBT_VERSION 1.10.7
RUN curl -L "https://github.com/sbt/sbt/releases/download/v${SBT_VERSION}/sbt-${SBT_VERSION}.tgz" | tar xz -C /usr/local --strip-components=1
ENV PATH="/usr/local/bin:${PATH}"

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы проекта
COPY . .

# Запускаем сборку нативного исполняемого файла
# Используем 'sbt nativeLink' для создания нативного бинарника
# 'sbt clean' для очистки, 'sbt update' для обновления зависимостей
RUN sbt clean update nativeLink

# --- Этап 2: Создание финального, минимального образа ---
# Используем минимальный базовый образ, так как нативный бинарник не требует JVM
FROM ubuntu:jammy-slim

# Устанавливаем только те системные библиотеки, которые нужны нативному бинарнику
# (например, libcurl, libidn2, libssl)
RUN apt-get update && apt-get install -y \
    libcurl4 \
    libidn2-0 \
    libssl3 \
    && rm -rf /var/lib/apt/lists/*

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем нативный исполняемый файл из этапа сборки
COPY --from=builder /app/target/scala-3.4.2/scala-native-hello-native .

# Указываем команду для запуска приложения
ENTRYPOINT ["/app/scala-native-hello-native"]

# Опционально: Указываем порт, если приложение является сервером
# EXPOSE 8080
```

### Пояснения к Dockerfile:

*   **Этап 1 (builder):** Используется для компиляции. Включает JDK, sbt и все dev-зависимости, необходимые для Scala Native. После сборки этот этап выбрасывается, и в финальный образ попадает только результат.
*   **Этап 2 (финальный):** Использует минимальный образ (например, `ubuntu:jammy-slim`), так как нативному бинарнику не нужна JVM. Сюда копируются только сам исполняемый файл и необходимые ему рантайм-библиотеки.
*   **`scala-native-hello-native`:** Замените на актуальное имя вашего исполняемого файла.
*   **`target/scala-3.4.2/`:** Путь к исполняемому файлу может отличаться в зависимости от версии Scala.

## Сборка Docker-образа

Перейдите в корневую директорию вашего проекта (где находится Dockerfile) и выполните команду:

```bash
docker build -t your-docker-username/scala-native-app:latest .
```

*   `-t`: Тег для вашего образа (имя пользователя Docker Hub / имя образа : версия).
*   `.`: Указывает, что Dockerfile находится в текущей директории.

## Запуск Docker-образа

```bash
docker run -p 8080:8080 your-docker-username/scala-native-app:latest
```

*   `-p`: Проброс портов (если ваше приложение слушает порт).

## Публикация Docker-образа

Чтобы опубликовать образ в Docker Hub или GitHub Container Registry (GHCR):

1.  **Авторизуйтесь:**
    ```bash
docker login # Для Docker Hub
# или
docker login ghcr.io -u YOUR_GITHUB_USERNAME # Для GHCR
    ```
2.  **Отправьте образ:**
    ```bash
docker push your-docker-username/scala-native-app:latest # Для Docker Hub
# или
docker push ghcr.io/YOUR_GITHUB_USERNAME/YOUR_REPO_NAME/scala-native-app:latest # Для GHCR
    ```

## Интеграция с `sbt-native-packager` (Альтернатива/Дополнение)

Плагин `sbt-native-packager` (который уже есть в вашем `project/plugins.sbt`) может автоматизировать создание Dockerfile и сборку образа.

*   **Добавьте настройки в `build.sbt`:**
    ```scala
enablePlugins(DockerPlugin)

dockerBaseImage := "ubuntu:jammy-slim"
dockerEntrypoint := Seq("bin/scala-native-hello-native") // Имя вашего бинарника
dockerExposedPorts := Seq(8080) // Если нужно
dockerRepository := Some("your-docker-username") // Для Docker Hub
# dockerRepository := Some("ghcr.io/YOUR_GITHUB_USERNAME") // Для GHCR
# dockerAlias := DockerAlias(
#   dockerRepository.value,
#   None,
#   Some("scala-native-app"),
#   Some("latest")
# )
    ```
*   **Сборка образа через sbt:**
    ```bash
sbt docker:publishLocal # Собрать и сохранить локально
sbt docker:publish      # Собрать и опубликовать (требует авторизации)
    ```
Это более интегрированный способ, но требует настройки в `build.sbt`. 
