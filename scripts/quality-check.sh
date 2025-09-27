#!/bin/bash

# Quality Check Script
# This script runs the same checks as the CI pipeline locally

set -e

echo "🔍 Starting Quality Checks..."

# Make gradlew executable
chmod +x ./gradlew

echo "📊 Running Detekt..."
./gradlew detekt
echo "✅ Detekt completed successfully"

echo "🎨 Running Ktlint Check..."
./gradlew ktlintCheck
echo "✅ Ktlint Check completed successfully"

echo "🧪 Running Unit Tests..."
./gradlew test
echo "✅ Unit Tests completed successfully"

echo "🎉 All quality checks passed!"

