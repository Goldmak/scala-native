#!/bin/bash

# Функция для проверки команды
check_command() {
    if command -v $1 >/dev/null 2>&1; then
        echo "✅ $2 найдено."
        $1 --version | head -n 1
        return 0
    else
        echo "❌ $2 не найдено."
        return 1
    fi
}

# Функция для проверки системной библиотеки (для Debian/Ubuntu)
check_library() {
    if dpkg -s $1 &> /dev/null; then
        echo "✅ Системная библиотека '$1' найдена."
    else
        echo "❌ Системная библиотека '$1' не найдена."
        echo "   Для Debian/Ubuntu, попробуйте: sudo apt-get install $1"
    fi
}

echo "--- Проверка основных инструментов ---"
check_command java "Java (JDK)"
check_command sbt "sbt (Scala Build Tool)"
check_command clang "Clang (LLVM C compiler)"

echo ""
echo "--- Проверка системных библиотек для HTTP-запросов ---"
if [[ "$(uname)" == "Linux" ]]; then
    if command -v dpkg >/dev/null 2>&1; then
        check_library libcurl4-openssl-dev
        check_library libidn2-dev
    else
        echo "⚠️ Не удалось проверить системные библиотеки (не Debian/Ubuntu). Убедитесь, что у вас установлены dev-версии 'libcurl' и 'libidn2'."
    fi
else
    echo "⚠️ Проверка системных библиотек доступна только для Linux."
fi

echo ""
echo "Проверка завершена."
