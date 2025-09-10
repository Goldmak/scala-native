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
ENV SBT_VERSION=1.10.7
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
FROM ubuntu:22.04

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
# Используем правильный путь к исполняемому файлу (в Docker он называется app)
COPY --from=builder /app/target/scala-3.4.2/app .

# Переименовываем файл для удобства
RUN mv /app/app /app/scala-native-hello

# Указываем команду для запуска приложения
ENTRYPOINT ["/app/scala-native-hello"]