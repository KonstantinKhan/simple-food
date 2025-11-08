# Настройка VS Code / Cursor для проекта Simple Food

Этот проект использует **Kotlin**, **Gradle** и **Ktor**. Для комфортной работы необходимо установить следующие расширения и выполнить настройки.

## Необходимые расширения

### Обязательные расширения:

1. **Kotlin Language** (`fwcd.kotlin`)
   - Поддержка синтаксиса Kotlin
   - Автодополнение и навигация по коду
   - Установка: `Ctrl+Shift+X` → поиск "Kotlin Language"

2. **Gradle for Java** (`vscjava.vscode-gradle`)
   - Поддержка Gradle проектов
   - Запуск задач Gradle из IDE
   - Установка: `Ctrl+Shift+X` → поиск "Gradle for Java"

3. **Extension Pack for Java** (`vscjava.vscode-java-pack`)
   - Включает все необходимые Java/Kotlin инструменты
   - Установка: `Ctrl+Shift+X` → поиск "Extension Pack for Java"

### Рекомендуемые расширения:

4. **Language Support for Java(TM) by Red Hat** (`redhat.java`)
   - Улучшенная поддержка Java/Kotlin
   - Обычно входит в Extension Pack for Java

5. **Debugger for Java** (`vscjava.vscode-java-debug`)
   - Отладка Java/Kotlin приложений
   - Обычно входит в Extension Pack for Java

6. **Test Runner for Java** (`vscjava.vscode-java-test`)
   - Запуск тестов из IDE
   - Обычно входит in Extension Pack for Java

## Быстрая установка

Файл `.vscode/extensions.json` уже создан в проекте. VS Code/Cursor автоматически предложит установить рекомендуемые расширения при открытии проекта.

Если предложение не появилось:
1. Нажмите `Ctrl+Shift+P`
2. Введите "Extensions: Show Recommended Extensions"
3. Нажмите "Install All"

## Настройки проекта

Файл `.vscode/settings.json` уже настроен для проекта. Он включает:
- Автоматическое форматирование Kotlin кода
- Правильные настройки для Gradle
- Исключение build директорий из поиска
- Настройки табуляции и отступов

## Решение проблем

### Проблема: Ошибки в IDE, но проект собирается

Это нормально! IDE может показывать ошибки, которые не влияют на сборку. Причины:

1. **Gradle синхронизация**: IDE может не синхронизироваться с Gradle
   - Решение: `Ctrl+Shift+P` → "Java: Clean Java Language Server Workspace"
   - Затем: `Ctrl+Shift+P` → "Gradle: Refresh Gradle Project"

2. **Кэш IDE**: Старый кэш может вызывать проблемы
   - Решение: Закройте и откройте проект заново
   - Или: `Ctrl+Shift+P` → "Java: Clean Java Language Server Workspace"

3. **Версия Gradle**: Убедитесь, что используется правильная версия
   - Текущая версия: Gradle 9.1.0 (указана в `gradle/wrapper/gradle-wrapper.properties`)
   - Если проблемы продолжаются, попробуйте обновить до последней версии

### Проблема: Ошибка "Shadow supports Gradle 8.11+ only"

Эта ошибка в IDE обычно не критична, если проект собирается. Это может быть проблема с:
- Кэшем IDE
- Неправильной синхронизацией Gradle

**Решение:**
1. Выполните `./gradlew clean build` в терминале
2. Очистите кэш IDE: `Ctrl+Shift+P` → "Java: Clean Java Language Server Workspace"
3. Обновите Gradle проект: `Ctrl+Shift+P` → "Gradle: Refresh Gradle Project"

### Проблема: Автодополнение не работает

1. Убедитесь, что установлено расширение Kotlin Language
2. Проверьте, что Java Language Server запущен (иконка в статус-баре)
3. Дождитесь индексации проекта (может занять несколько минут)
4. Перезапустите Language Server: `Ctrl+Shift+P` → "Java: Restart Language Server"

## Проверка установки

После установки расширений:

1. Откройте любой `.kt` файл
2. Убедитесь, что есть подсветка синтаксиса
3. Попробуйте автодополнение (`Ctrl+Space`)
4. Проверьте, что нет красных подчеркиваний (кроме реальных ошибок)

## Полезные команды

- `Ctrl+Shift+P` → "Gradle: Refresh Gradle Project" - обновить проект
- `Ctrl+Shift+P` → "Java: Clean Java Language Server Workspace" - очистить кэш
- `Ctrl+Shift+P` → "Gradle: Run Gradle Task" - запустить задачу Gradle
- `Ctrl+Shift+P` → "Java: Restart Language Server" - перезапустить сервер

## Дополнительная информация

- Проект использует Kotlin 2.2.20
- Проект использует Ktor 3.3.1
- Проект использует Gradle 9.1.0
- Целевая версия JVM: 21

Если проблемы продолжаются, проверьте логи:
- `Ctrl+Shift+P` → "Java: Show Server Task Status"
- `Ctrl+Shift+P` → "Output: Show Output" → выберите "Language Support for Java"

