# safe-serializable

> DISCLAIMER
> The project is in the very beginning of the development phase and is **NOT** production-ready. 

The set of tools for making sure your Serializable classes are properly and safely work at runtime.

This project aims to make work with Serializable classes more safe and reliable. It's supposed that in the future there
should be libraries for runtime and maybe even compile-time checks and Lint rules or IDE plugins helping detect issues
on the fly while you're coding.

## What's the problem?

Long story short, the following class looks good at first glance:

```java
class Foo implements Serializable {
    Bar bar;
}
```

In fact, it depends on whether the `Bar` implements `Serializable` as well. If not then serialization/deserialization attempt 
will fail at runtime while the compiler might not even give you a warning about the case.

## Project content

* `sample` - sample project to demonstrate core-library capabilities;
* `core` - main library with Serializable verification logic;

### Core

The easiest way to use core is in unit tests. You just create a single test in a module of your project which has access
to every other module in project even if dependency is transient. The test may contains the following code:

```java
SerializableClassCollector classCollector = ClasspathSerializableClassCollector.builder()
        .addClasspathFilter(DirectoryClasspathFilter.createForCurrentProject())
        .build();
new SafeSerializable(classCollector).checkSafety();
```

This code will check if all classes in the project classpath implement `Serializable` properly. If not then 
`AssertionError` will be thrown.
