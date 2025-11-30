# 🤖 AI Agent Development Standards: Code, Nullness, and Version Control

This document outlines the mandatory quality, compliance, and procedural rules for all code generated or modified by the AI Agent in this project.

## 1. Code Quality and Modernization Standards

### Rule 1.1: Modern Java Usage (Java 25)

**ACTION:** The Agent **MUST** prioritize using modern Java 25 language features and APIs. For comprehensive and up-to-date guidance on implementing all new features, the Agent **MUST** consult the official Java 25 release documentation.

### Rule 1.2: Checkstyle and Structural Compliance

**ACTION:** All generated code **MUST** be fully compliant with the project's existing `checkstyle.xml`. The Agent **MUST NEVER** introduce new violations. Additionally, the Agent **MUST** continuously review and propose refactoring options for structure to ensure modularity and scalability.

### Rule 1.3: Unit Test and Coverage Mandate

**ACTION:** The Agent **MUST** create comprehensive Unit Tests for any new or modified code. Insufficient test coverage **MUST** be flagged, and necessary tests **MUST** be generated to prevent regressions.

### Rule 1.4: Explicit Error Handling and Logging Quality

**ACTION:** The Agent **MUST** use explicit error handling (preferring specific exception types and `Optional` over broad `catch (Exception e)`). All log statements **MUST** be structured (using SLF4J/Log4j 2) and contain contextual information (e.g., user ID, transaction ID) to provide demonstrable value for debugging.

### Rule 1.5: Native Image Compatibility (GraalVM)

**ACTION:** The Agent **MUST** prioritize writing code compatible with Native Image constraints (minimizing runtime reflection/proxies). Task execution is strictly conditional on the environment:

1. **Local Developer Environment (Resource Limited):** The Agent **MUST NOT** initiate any resource-intensive native image tasks. Local verification is limited to standard JVM compilation and unit tests.

2. **Cloud Agent/CI Environment (High Resource):** The Agent **MUST** execute all resource-intensive native image tasks for verification and build.

### Rule 1.6: Legacy API Modernization

**ACTION:** When encountering existing ("legacy") code, the Agent **MUST** proactively identify and replace all deprecated or outdated APIs, libraries, and code constructs with modern, equivalent Java 25 alternatives. This ensures the codebase remains modern and maintainable.

## 2. Mandatory JSpecify Nullness Compliance

The Agent **MUST** adhere to these rules for both new and existing code:

### Rule 2.1: Core Nullness Principle

**MANDATE:** This project operates under the **Non-Nullable by Default** principle. All type usages (parameters, returns, fields) are non-nullable unless explicitly annotated otherwise.

### Rule 2.2: Explicit Nullability Marking

**ACTION:** Use the `@Nullable` annotation exclusively to mark any type permitted to hold a `null` value. The `@NonNull` annotation **MUST NEVER** be used.

### Rule 2.3: Generics Nullability

**ACTION:** Nullability **MUST** specify both the container and the content (e.g., `List<@Nullable String>` for nullable content, `@Nullable List<String>` for nullable container).

### Rule 2.4: Package Scoping (`@NullMarked`)

**ACTION:** For every package, a `package-info.java` file applying the `@NullMarked` annotation **MUST** be created.

**Required Structure:**

```java
@NullMarked
package com.example.domain;

import org.jspecify.annotations.NullMarked;
```

### Rule 2.5: Legacy Nullness Audit

**ACTION:** The Agent **MUST** audit all existing ("legacy") code. Any legacy variables, method parameters, or return types that accept or return a `null` value **MUST** be explicitly marked with the `@Nullable` annotation to ensure full adherence to the JSpecify contract (Rule 2.1).

## 3. Version Control Guidelines (Git and PR)

### Rule 3.1: Git Commit Structure (7 Rules)

**ACTION:** Commit messages **MUST** adhere to the [standard seven rules](https://cbea.ms/git-commit/): a concise subject (max 50 chars, imperative mood, no period), a blank line separating it from the body, and a body wrapped at **72 characters**.

### Rule 3.2: Pull Request Proposal Generation

**ACTION:** For every significant change or refactoring task, the Agent **MUST** automatically suggest a structured Pull Request Proposal. This proposal **MUST** include a descriptive title, a detailed summary of changes, an impact assessment, and a verification checklist
