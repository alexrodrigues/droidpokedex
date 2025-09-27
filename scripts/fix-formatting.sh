#!/bin/bash

# Fix Formatting Script
# This script auto-fixes formatting issues using Ktlint and Detekt

set -e

echo "🔧 Fixing Code Formatting..."

# Make gradlew executable
chmod +x ./gradlew

echo "🎨 Running Ktlint Format (Auto-fix)..."
./gradlew ktlintFormat
echo "✅ Ktlint formatting completed"

echo "📊 Running Detekt with auto-correction..."
./gradlew detekt
echo "✅ Detekt auto-correction completed"

echo "🎉 Formatting fixes completed!"
echo ""
echo "💡 You can now run './scripts/quality-check.sh' to verify all issues are resolved"

