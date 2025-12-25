# GraalVM Native Image Build

This project has been configured to support GraalVM native image compilation for significantly reduced startup time.

## Prerequisites

### Install GraalVM

Using SDKMAN (recommended):

```bash
sdk install java 24.0.2-graalce
sdk use java 24.0.2-graalce
```

Verify installation:

```bash
java -version
native-image --version
```

## Building Native Image

### Standard Build

To compile the application to a native executable:

```bash
# Switch to GraalVM JDK
sdk use java 24.0.2-graalce

# Build the native image
./gradlew nativeCompile
```

The native executable will be created at:
```
build/native/nativeCompile/spring-ai-claude
```

### Build Configuration

The native image is configured with the following optimizations in `build.gradle.kts`:

- **Image Name**: `spring-ai-claude`
- **Optimization Level**: `-Ob` (optimized for faster startup)
- **Monitoring**: JFR (Java Flight Recorder) enabled
- **Verbose Output**: Enabled for troubleshooting
- **Exception Stack Traces**: Enabled for better debugging

## Running the Native Image

After building, run the native executable directly:

```bash
# Make sure your environment variable is set
export PERSONAL_CLAUDE_API_KEY=your-api-key-here

# Run the native executable
./build/native/nativeCompile/spring-ai-claude
```

## Performance Comparison

### Actual Measured Results

**JVM Mode:**
```bash
./gradlew bootRun
```
Startup time: **2.497 seconds**

**Native Image Mode:**
```bash
./build/native/nativeCompile/spring-ai-claude
```
Startup time: **0.154 seconds**

**Performance Improvement: ~16x faster startup!**

The native executable is 125MB in size and uses less memory than the JVM version at runtime.

## Testing

To run tests with native image:

```bash
./gradlew nativeTest
```

## Troubleshooting

### Missing native-image tool

If you get an error about missing native-image, ensure you're using GraalVM:

```bash
sdk use java 24.0.2-graalce
which native-image
```

### Memory Issues During Build

Native image compilation is memory-intensive. If the build fails with OOM errors, increase memory:

```bash
./gradlew nativeCompile -Dorg.gradle.jvmargs="-Xmx8g"
```

### Reflection Issues

Spring Boot 3.x includes excellent AOT (Ahead-of-Time) processing that handles most reflection automatically. If you encounter reflection-related issues at runtime, check the generated hints at:

```
build/resources/aot/META-INF/native-image/
```

## Additional Resources

- [GraalVM Native Image Documentation](https://www.graalvm.org/latest/reference-manual/native-image/)
- [Spring Boot Native Image Support](https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html)
- [Spring AI Native Support](https://docs.spring.io/spring-ai/reference/index.html)