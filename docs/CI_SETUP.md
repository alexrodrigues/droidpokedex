# CI Pipeline Setup

This document describes the Continuous Integration (CI) pipeline setup for the DroidPokedex Android project.

## Overview

The CI pipeline ensures code quality and reliability by running the following checks on every push and pull request:

1. **Detekt** - Static code analysis for Kotlin
2. **Ktlint** - Kotlin code style checker
3. **Unit Tests** - Automated test execution

## Workflows

### 1. Quality Check Workflow (`quality-check.yml`)

**Trigger:** Push to main/develop/feature branches and Pull Requests

**Jobs:**

- **quality-checks**: Runs Detekt, Ktlint, and Unit Tests in parallel

**Features:**

- ✅ Detekt static analysis
- ✅ Ktlint code style checking
- ✅ Unit test execution
- ✅ Test report artifacts
- ✅ Detekt report artifacts
- ✅ Ktlint report artifacts

### 2. Comprehensive CI Workflow (`ci.yml`)

**Trigger:** Push to main/develop/feature branches and Pull Requests

**Jobs:**

- **code-quality**: Detekt and Ktlint checks
- **unit-tests**: Unit test execution
- **ui-tests**: Paparazzi UI tests
- **build-check**: APK build verification
- **lint-and-format**: Android Lint checks

## Local Development

### Running Quality Checks Locally

You can run the same quality checks locally using the provided script:

```bash
./scripts/quality-check.sh
```

### Manual Commands

```bash
# Run Detekt
./gradlew detekt

# Run Ktlint Check
./gradlew ktlintCheck

# Run Unit Tests
./gradlew test

# Run all checks
./gradlew detekt ktlintCheck test
```

## Configuration

### Detekt Configuration

- **File**: `config/detekt/detekt.yml`
- **Version**: 1.23.0
- **Features**: Custom rules, auto-correction enabled

### Ktlint Configuration

- **Version**: 12.1.0
- **Android Support**: Enabled
- **Experimental Rules**: Enabled

### Test Configuration

- **JUnit**: 4.13.2
- **MockK**: 1.13.5
- **Coroutines Test**: 1.7.3

## Artifacts

The CI pipeline generates the following artifacts:

1. **Test Reports**: HTML and XML test reports for all modules
2. **Detekt Reports**: Static analysis reports
3. **Ktlint Reports**: Code style violation reports
4. **Lint Results**: Android Lint reports (comprehensive workflow)

## Branch Protection

Recommended branch protection rules for `main` and `develop`:

1. Require status checks to pass before merging
2. Require branches to be up to date before merging
3. Require pull request reviews before merging
4. Restrict pushes to matching branches

## Troubleshooting

### Common Issues

1. **Detekt Failures**: Check `config/detekt/detekt.yml` for rule configuration
2. **Ktlint Failures**: Run `./gradlew ktlintFormat` to auto-fix issues
3. **Test Failures**: Check test reports in artifacts for detailed failure information

### Cache Issues

If you encounter cache-related issues, you can clear the Gradle cache:

```bash
./gradlew clean
rm -rf ~/.gradle/caches
```

## Monitoring

- Check the Actions tab in GitHub for workflow status
- Review artifacts for detailed reports
- Monitor build times and optimize as needed

## Contributing

When contributing to this project:

1. Ensure all quality checks pass locally before pushing
2. Address any Detekt or Ktlint violations
3. Add unit tests for new functionality
4. Update documentation as needed
