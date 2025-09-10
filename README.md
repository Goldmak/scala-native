# Проект: HTTP-клиент на Scala Native
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Goldmak_scala-native&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Goldmak_scala-native) [![CI](https://github.com/Goldmak/scala-native/actions/workflows/scala.yml/badge.svg)](https://github.com/Goldmak/scala-native/actions/workflows/scala.yml) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [![Built with Laika](https://img.shields.io/badge/Built%20with-Laika-blue.svg)](https://laika.site/) [![GitHub Pages deploy](https://github.com/Goldmak/scala-native/actions/workflows/pages.yml/badge.svg)](https://github.com/Goldmak/scala-native/actions/workflows/pages.yml) [![Scala Version](https://img.shields.io/badge/Scala-3.4.2-blue.svg)](https://www.scala-lang.org/) [![Docker Image](https://img.shields.io/docker/v/goodmak/scala-native-hello?label=Docker&sort=semver)](https://hub.docker.com/r/goodmak/scala-native-hello)

Этот проект является простым примером консольного приложения на Scala, скомпилированного в нативный исполняемый файл с помощью Scala Native. Приложение отправляет HTTP GET-запрос, получает и парсит JSON-ответ.

## Структура Проекта
Проект организован как мульти-проект `sbt` и состоит из двух основных частей:
*   **Основное приложение:** Находится в корневой директории и содержит исходный код Scala Native приложения.
*   **Подпроект документации (`docs`):** Находится в директории `docs/` и используется для генерации статического сайта с помощью Laika.

## 1. Пререквизиты (Системные требования)
Для сборки и запуска проекта необходимы следующие инструменты.

### Bash-скрипт для проверки и установки prereq.sh
Этот скрипт проверит наличие всех необходимых компонентов и, если вы используете систему на базе Debian/Ubuntu,
Как использовать: сделай его исполняемым (chmod +x check_prereqs.sh) и запусти (./check_prereqs.sh).

## 2. Сборка и запуск
Все команды выполняются из корневой папки проекта.
```bash
sbt nativeLink
./target/scala-3.4.2/scala-native-hello-native
```

## 3. Методика отладки типовых ошибок
### Проблема 1: Ошибка разрешения зависимостей (ResolveException: Not Found)
**Симптом:** sbt не может скачать библиотеку.

**Причина:** Несовместимость версий "Золотого Трио": Версия Scala + Версия Scala Native + Версия Библиотеки.

**Решение:**

1.  Зайти на Scaladex.
2.  Найти каждую библиотеку и проверить, какие её версии совместимы с твоей версией Scala (3.x) и Scala Native (0.5).
3.  Подобрать рабочую комбинацию версий в build.sbt и project/plugins.sbt.
4.  Убедиться, что для Scala Native зависимостей используется тройной знак процента (%%%).

### Проблема 2: Ошибки нативной компиляции/компоновки
**Симптом:** sbt скачал зависимости, но сборка падает с ошибкой от clang или ld.

**Причина A:** `fatal error: 'some/file.h' file not found`. Это значит, что отсутствует dev-пакет системной библиотеки.

**Решение:** Найти по имени файла (curl.h -> libcurl) соответствующий dev-пакет (libcurl4-openssl-dev) и установить его через системный менеджер пакетов (sudo apt-get install ...).

**Причина B:** `/usr/bin/ld: cannot find -l....` Это значит, что компоновщик не может найти саму системную библиотеку. Часто это "зависимость зависимости".

**Решение:** По имени (-lidn2 -> libidn2) найти соответствующий dev-пакет (libidn2-dev) и установить его.

### Проблема 3: Ошибка бинарной несовместимости (binary-incompatible version of NIR)
**Симптом:** Глубокая ошибка AssertionError от самого компилятора Scala Native.

**Причина:** Конфликт версий между плагином sbt-scala-native и системными библиотеками Scala Native (nativelib, javalib), которые "притащила" одна из зависимостей.

**Решение:**

1.  Посмотреть в логе ошибки, какую версию системной библиотеки он пытается использовать (например, nativelib версии 0.5.8).
2.  Обновить версию плагина в project/plugins.sbt до этой же версии (0.5.8).
3.  Если это не помогает, произвести полную очистку кеша: `rm -rf ~/.cache/coursier/`.

## 4. CI/CD - Непрерывная интеграция
Этот проект использует GitHub Actions для автоматической сборки, тестирования и анализа кода при каждом изменении. Процесс состоит из двух независимых воркфлоу:

### Основной воркфлоу (`.github/workflows/scala.yml`)
Этот воркфлоу отвечает за основное приложение:
1.  **Установка зависимостей:** Настраивает Java 17, sbt и системные библиотеки, необходимые для Scala Native (clang, libcurl).
2.  **Запуск тестов:** Выполняет команду `sbt test` для проверки корректности кода.
3.  **Анализ качества кода:** Запускает сканер SonarCloud для поиска багов, уязвимостей и "кодов с запашком". Результаты анализа доступны на [SonarCloud](https://sonarcloud.io/summary/new_code?id=Goldmak_scala-native) и прямо в Pull Request'ах.
4.  **Сбор графа зависимостей:** Отправляет информацию о зависимостях проекта в GitHub для отслеживания уязвимостей.

### Воркфлоу для сайта (`.github/workflows/pages.yml`)
Этот воркфлоу отвечает за сборку и публикацию сайта на GitHub Pages:
1.  **Генерация сайта:** Запускает команду `cd docs && sbt laikaSite` для генерации HTML-файлов с помощью библиотеки Laika.
2.  **Публикация:** Автоматически публикует сгенерированные файлы на GitHub Pages.