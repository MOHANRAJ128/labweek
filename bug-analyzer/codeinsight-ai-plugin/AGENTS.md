# AGENTS Guide

## Project snapshot
- This repository is a **minimal IntelliJ Platform plugin scaffold** (not a feature-complete plugin yet).
- Core runtime pieces are:
  - `src/main/resources/META-INF/plugin.xml` (plugin metadata + extension/action registration)
  - `src/main/java/com/example/codeinsight/CodeinsightAiPluginLoader.java` (placeholder class only)
  - `build.gradle.kts` (IntelliJ Gradle plugin setup + run/build tasks)
- Current structure indicates future work should be added through IntelliJ extension points and actions declared in `plugin.xml`.

## Architecture and boundaries
- There is no multi-module split; everything is in a single Gradle module (`settings.gradle`).
- Platform boundary is IntelliJ IDEA Ultimate (`platformType=IU`, `intellij.type=IU`) with Java plugin dependency (`plugins.set(listOf("java"))`).
- `plugin.xml` currently depends on `com.intellij.modules.platform` only; add extra `<depends>` entries when using optional platform modules.
- Data flow is currently startup-only metadata loading: IDE reads `plugin.xml` -> wires extensions/actions -> loads plugin classes.

## Build and run workflows
- Use the Gradle wrapper from repo root:
  - `./gradlew.bat build`
  - `./gradlew.bat runIde`
- `runIde` has `autoReloadPlugins=true` in `build.gradle.kts`, so iterative plugin dev should prefer this task.
- `patchPluginXml` sets `sinceBuild="233"` and no `untilBuild`; keep compatibility changes aligned with IntelliJ 2023.3 APIs.
- `test` is configured for JUnit Platform, but no tests exist yet.

## Project-specific conventions to keep
- Java level is fixed to 17 (`build.gradle.kts`, `gradle.properties`); keep new code/API usage Java 17 compatible.
- Keep plugin identity fields synchronized when editing metadata:
  - Gradle: `group`, `version`
  - Properties: `pluginGroup`, `pluginName`, `pluginVersion`
  - Descriptor: `<id>`, `<name>`, `<vendor>` in `plugin.xml`
- Register new IntelliJ integrations explicitly in `plugin.xml` (`<extensions>` / `<actions>`), then implement backing classes under `com.example.codeinsight` (or a consistent subpackage).

## Integration points to use next
- New features should typically add:
  1. IntelliJ extension/action declaration in `plugin.xml`
  2. Java implementation class in `src/main/java/...`
  3. (Optional) additional IntelliJ module/plugin dependencies in Gradle + `<depends>`
- External libraries are currently only from Maven Central and IntelliJ platform artifacts; add dependencies in `build.gradle.kts` and verify compatibility with IntelliJ 2023.3.

## Notes discovered from existing docs
- `README.md` commands use a different local path (`C:\dev\Sprint\offer-basket\...`); run commands from this repo root instead.

