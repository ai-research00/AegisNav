# Project Readiness Design

## Overview
This design outlines the technical approach to make the AegisNav project fully ready for development by fixing critical build issues, adding missing resources, and validating the development environment.

## Architecture

### Component Overview
```
Project Readiness
├── Build System
│   ├── Gradle Wrapper
│   ├── Build Configuration
│   └── Dependency Management
├── Android Resources
│   ├── Launcher Icons
│   ├── Themes & Styles
│   └── String Resources
├── Development Environment
│   ├── IDE Configuration
│   ├── Build Tools
│   └── Testing Infrastructure
└── Validation
    ├── Build Verification
    ├── Resource Validation
    └── Dependency Check
```

## Design Decisions

### 1. Gradle Wrapper Strategy

**Problem:** Gradle wrapper JAR is missing, network download fails
**Options:**
- A. Download wrapper manually
- B. Use system Gradle to regenerate
- C. Copy from another project

**Decision:** Use system Gradle to regenerate wrapper
**Rationale:** Most reliable when network is available, creates proper wrapper structure

**Implementation:**
```bash
gradle wrapper --gradle-version 8.1.4
```

### 2. Launcher Icon Generation

**Problem:** No launcher icons exist in mipmap directories
**Options:**
- A. Use Android Studio Image Asset tool
- B. Generate programmatically
- C. Use placeholder icons
- D. Copy from template

**Decision:** Generate simple placeholder icons programmatically
**Rationale:** Unblocks development immediately, can be replaced with proper design later

**Required Densities:**
- mipmap-mdpi (48x48)
- mipmap-hdpi (72x72)
- mipmap-xhdpi (96x96)
- mipmap-xxhdpi (144x144)
- mipmap-xxxhdpi (192x192)

### 3. Build Configuration Fixes

**Issues Identified:**
1. MaxPermSize deprecated in Java 8+
2. Gradle version compatibility
3. Repository configuration

**Solutions:**
- Replace MaxPermSize with MaxMetaspaceSize ✅
- Ensure Gradle 8.1.4 compatibility
- Verify Maven repository access

### 4. Dependency Verification

**Approach:**
- Run dependency resolution task
- Check for conflicts
- Verify all required libraries are accessible
- Validate version compatibility

## Implementation Plan

### Phase 1: Critical Fixes (Immediate)

#### 1.1 Fix Gradle Configuration
- ✅ Update gradle.properties (MaxPermSize → MaxMetaspaceSize)
- Generate proper Gradle wrapper
- Verify wrapper execution

#### 1.2 Create Launcher Icons
- Generate ic_launcher.png for all densities
- Generate ic_launcher_round.png for all densities
- Place in appropriate mipmap-* directories

#### 1.3 Verify Build System
- Run `./gradlew tasks` to verify wrapper
- Run `./gradlew dependencies` to check resolution
- Run `./gradlew build --dry-run` to validate configuration

### Phase 2: Resource Validation (Quick)

#### 2.1 Validate Android Resources
- Check all @string references resolve
- Check all @color references resolve
- Check all @style references resolve
- Verify AndroidManifest.xml validity

#### 2.2 Check Theme Configuration
- Verify Theme.AegisNav exists
- Validate color scheme
- Check typography configuration

### Phase 3: Build Verification (Essential)

#### 3.1 Clean Build
```bash
./gradlew clean
./gradlew build
```

#### 3.2 Debug APK Generation
```bash
./gradlew assembleDebug
```

#### 3.3 Lint Check
```bash
./gradlew lint
```

### Phase 4: Documentation Updates (Nice-to-have)

#### 4.1 Update README
- Correct tech stack (Kotlin/Android, not Flutter)
- Update build instructions
- Add troubleshooting section

#### 4.2 Create Build Guide
- Document build requirements
- List common issues and solutions
- Provide quick start commands

## Technical Specifications

### Gradle Wrapper Configuration
```properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https://services.gradle.org/distributions/gradle-8.1.4-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

### Gradle Properties (Fixed)
```properties
org.gradle.jvmargs=-Xmx2048m -XX:MaxMetaspaceSize=512m
org.gradle.parallel=true
org.gradle.caching=true
```

### Required Build Tools
- JDK 17 or 21
- Android SDK 34
- Gradle 8.1.4
- Kotlin 1.9.10

### Launcher Icon Specifications
```
Format: PNG with transparency
Color: Simple monochrome or brand colors
Content: "AN" text or simple navigation icon
Background: Transparent or solid color
```

## Validation Criteria

### Build System
- [ ] `./gradlew --version` shows Gradle 8.1.4
- [ ] `./gradlew tasks` completes without errors
- [ ] `./gradlew dependencies` resolves all dependencies
- [ ] Gradle daemon starts successfully

### Resources
- [ ] All mipmap directories contain launcher icons
- [ ] No missing resource errors in build output
- [ ] AndroidManifest.xml validates successfully
- [ ] Theme resources are complete

### Build Process
- [ ] `./gradlew clean` completes
- [ ] `./gradlew build` completes successfully
- [ ] `./gradlew assembleDebug` produces APK
- [ ] APK can be installed on device/emulator
- [ ] App launches without crashes

### Code Quality
- [ ] No compilation errors
- [ ] No critical lint warnings
- [ ] Kotlin code follows conventions
- [ ] Dependencies are up-to-date

## Risk Assessment

### High Risk
- **Gradle wrapper download failure**: Mitigated by using system Gradle
- **Dependency resolution issues**: Mitigated by verifying repository access
- **SDK version mismatch**: Mitigated by documenting requirements

### Medium Risk
- **Icon quality**: Acceptable for development, needs design later
- **Build performance**: May need optimization for large project

### Low Risk
- **Documentation accuracy**: Can be updated incrementally
- **Test infrastructure**: Not blocking initial development

## Success Metrics

### Immediate Success
- Project builds without errors
- APK can be generated and installed
- App launches successfully

### Short-term Success
- Build time < 2 minutes for clean build
- Build time < 30 seconds for incremental build
- No critical lint warnings

### Long-term Success
- Consistent build success rate > 99%
- Developer onboarding time < 30 minutes
- CI/CD pipeline ready

## Next Steps After Readiness

1. Implement sensor fusion algorithms
2. Integrate MapLibre for offline maps
3. Add ARCore integration
4. Implement navigation logic
5. Add comprehensive testing

## Correctness Properties

### Property 1: Build Reproducibility
**Validates: Requirements 1.1, 1.3**
```
∀ clean_state: BuildState,
  build(clean_state) = SUCCESS ⟹
  build(clean_state) = SUCCESS
```
A successful build from a clean state should always succeed when repeated.

### Property 2: Resource Completeness
**Validates: Requirements 2.1, 2.2, 2.5**
```
∀ resource_ref: ResourceReference,
  referenced(resource_ref) ⟹ exists(resource_ref)
```
All referenced resources must exist in the project.

### Property 3: Dependency Resolution
**Validates: Requirements 3.1, 3.2, 3.3, 3.4**
```
∀ dependency: Dependency,
  declared(dependency) ⟹ resolvable(dependency)
```
All declared dependencies must be resolvable from configured repositories.

### Property 4: APK Validity
**Validates: Requirements 1.3, 2.5**
```
∀ apk: APK,
  generated(apk) ⟹ installable(apk) ∧ launchable(apk)
```
Any generated APK must be installable and launchable.

### Property 5: Configuration Consistency
**Validates: Requirements 1.2, 1.5, 4.1**
```
∀ config: Configuration,
  valid(config) ⟹ ¬conflicts(config)
```
Valid configurations must not contain conflicts.
