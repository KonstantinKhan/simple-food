#!/bin/bash
OLD_NAME=$1
NEW_NAME=$2

if [ -z "$OLD_NAME" ] || [ -z "$NEW_NAME" ]; then
    echo "Usage: ./rename-module.sh <old-name> <new-name>"
    exit 1
fi

echo "🔍 Searching for files containing $OLD_NAME..."
FILES=$(find . -type f \( -name "*.md" -o -name "*.gradle*" -o -name "*.kt" -o -name "*.java" \) -exec grep -l "$OLD_NAME" {} \;)

echo "📋 Files to update:"
echo "$FILES"

# Переименование директории
if [ -d "$OLD_NAME" ]; then
    echo "📁 Renaming directory: $OLD_NAME → $NEW_NAME"
    mv "$OLD_NAME" "$NEW_NAME"
fi

# Замена в файлах
echo "✏️  Replacing text in files..."
echo "$FILES" | xargs sed -i "s/$OLD_NAME/$NEW_NAME/g"

# Переименование файла документации
if [ -f "docs/modules/$OLD_NAME.md" ]; then
    echo "📄 Renaming documentation: docs/modules/$OLD_NAME.md → docs/modules/$NEW_NAME.md"
    mv "docs/modules/$OLD_NAME.md" "docs/modules/$NEW_NAME.md"
fi

echo "✅ Done!"