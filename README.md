# Проект: HTTP-клиент на Scala Native
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Goldmak_scala-native&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Goldmak_scala-native) [![CI](https://github.com/Goldmak/scala-native/actions/workflows/scala.yml/badge.svg)](https://github.com/Goldmak/scala-native/actions/workflows/scala.yml) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Этот проект является простым примером консольного приложения на Scala, скомпилированного в нативный исполняемый файл с помощью Scala Native. Приложение отправляет HTTP GET-запрос, получает и парсит JSON-ответ.

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
(Содержимое этого раздела остается без изменений)

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
