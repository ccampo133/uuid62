# uuid62

[![Download](https://api.bintray.com/packages/ccampo133/public/uuid62/images/download.svg)](https://bintray.com/ccampo133/public/uuid62/_latestVersion)
[![](https://github.com/ccampo133/uuid62/workflows/Build%20master/badge.svg)](https://github.com/{owner}/{repo}/actions) 

A small Kotlin set of utilities to convert UUIDs to and from Base62 encoded strings. Perfect for URLs!

Jackson and Spring Boot auto-configuration is supported out of the box!

## Spring Boot Quick Start

You probably want to use this with Spring Boot. No problem, that's easy. Include the following in your `build.gradle` 
or `pom.xml`:

**Gradle**

```groovy
implementation 'me.ccampo:uuid62-core:0.1.1'
```

**Maven**

```xml
<dependency>
  <groupId>me.ccampo</groupId>
  <artifactId>uuid62-spring-boot-starter</artifactId>
  <version>0.1.1</version>
</dependency>
```

...and that's it! All `UUID`s in your controller request parameters, bodies, etc., will be serialized and deserialized 
to/from Base62 strings.

For more detailed usage options and additional information, keep reading.

## Background

According to [Wikipedia](https://en.wikipedia.org/wiki/Universally_unique_identifier#Format):

> In its canonical textual representation, the sixteen octets of a UUID are represented as 32 hexadecimal (base 16) 
digits, displayed in five groups separated by hyphens, in the form `8-4-4-4-12` for a total of 36 characters (32 
alphanumeric characters and four hyphens). For example: `123e4567-e89b-12d3-a456-426655440000`

Quite simply, UUIDs in that form are _ugly_ in URLs. For starters, they're long, and they're composed of a lot of wasted
space. Because they use the hexadecimal alphabet (`a-f`, `0-9`), you're limited to four bits per characters. 
Additionally, the hyphen separators don't serve much purpose besides readability. This limits us to the long, 36
character string.

By switching to [Base62](https://www.wikidata.org/wiki/Q809817), we can condense to roughly 6 bits per character. If we
remove the separators, we can then get a new string representation of the same UUID, shortened to 22-26 characters 
total.

**But why not just use Base64?**

Standard base64 is not URL and filename safe, due to the `+` and `/` characters in the encoding's alphabet. 
[RFC 4648](https://tools.ietf.org/html/rfc4648#section-5) defines a scheme for URL and filename safe base 64 encoding,
which may be fine for some users, however others still may not want any special characters at all. Therefore, base 62 
presents one with an encoding scheme composed of only alphanumeric characters and a similar bit density to base64.

## Usage

`uuid62` can be used in a few different ways. Each is explained below.

### As a Library

Perhaps the simplest usage of `uuid62` is as a library. You can use `uuid62-core` as a typical Kotlin or Java library 
(Groovy, Scala, and other JVM languages _should_ also be supported theoretically, but this has not been tested yet).

**Gradle**

```groovy
implementation 'me.ccampo:uuid62-core:0.1.1'
```

**Maven**

```xml
<dependency>
  <groupId>me.ccampo</groupId>
  <artifactId>uuid62-core</artifactId>
  <version>0.1.1</version>
</dependency>
```

#### Kotlin
When using as a Kotlin library, the API is simplified by providing some extension methods for `java.util.UUID`. 
Additionally, a couple standalone (static) functions are available as well.

```kotlin
import me.ccampo.uuid62.core.util.uuidFromBase62String
import me.ccampo.uuid62.core.util.uuidFromByteArray

val uuid1 = uuidFromBase62String("el1IFnw8eEOUCdMZCKcsdG") // Equivalent to beac91e2-8479-4f38-94d0-3199a0706c67

val bytes = uuid1.toByteArray() // This is an extension method defined in me.ccampo.uuid62.core.util

val uuid2 = uuidFromByteArray(bytes)

assert(uuid1 == uuid2) // true

val uuid62String = uuid1.toBase62String() // Another extension from the same package

assert(uuid62String == "el1IFnw8eEOUCdMZCKcsdG") // true
```

#### Java

Extension methods don't exist in Java, so instead they're available as static utility methods.

```java
import me.ccampo.uuid62.core.util.UUIDUtilsKt;

import java.util.UUID;

public class Example {
    
    public static void main(final String[] args) {
        
        final UUID uuid1 = UUIDUtilsKt.uuidFromBase62String("el1IFnw8eEOUCdMZCKcsdG");
        
        final byte[] bytes = UUIDUtilsKt.toByteArray(uuid1);
        
        final UUID uuid2 = UUIDUtilsKt.uuidFromByteArray(bytes);
        
        assert uuid1 == uuid2; // true
        
        final String uuid62String = UUIDUtilsKt.toBase62String(uuid1);
        
        assert uuid62String.equals("el1IFnw8eEOUCdMZCKcsdG"); // true
    }
}
```

See the Java unit tests in the `core` module for more examples of Java usage.

### Jackson

`uuid62` provides a Jackson JSON serializer and deserializer, as well as Jackson module, for parsing Base62 UUIDs.

**Gradle**

```groovy
implementation 'me.ccampo:uuid62-jackson:0.1.1'
```

**Maven**

```xml
<dependency>
  <groupId>me.ccampo</groupId>
  <artifactId>uuid62-jackson</artifactId>
  <version>0.1.1</version>
</dependency>
```

You can then register the `uuid62` module such as:

```java
final ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(new UUID62Module());
```

Kotlin makes this a bit simpler, and there are some convenience and extension methods provided, so check out the 
`jackson` module in this project for more details.


### Spring Boot

A Spring Boot auto-configuration class is available as well. See the [Spring Boot Quick Start](#spring-boot-quick-start)
section above for more details on including it as a dependency. When this module is included, a Spring Boot 
auto-configuration will be instantiated and automatically provide you with `uuid62` Jackson serializers and 
deserializers. If you are developing a web app, a custom Spring `Converter` will be registered as well, which will
handle translating base62 UUID strings into `java.util.UUID` instances when passed to your Spring MVC controllers.

See the `sample` project for a simple example of a Spring Boot project, using the auto-configuration.

If you wish to disable the auto-configuration and wire it yourself, you can follow the standard Spring approach of
selectively enabling auto-configuration classes (the `uuid62` auto-configuration class is called 
`me.ccampo.uuid62.springboot.autoconfigure.UUID62AutoConfiguration`), OR you can provide the following property in your
`application.properties`/`application.yml` file:

    uuid62.autoconfigure.enabled=false

When that property is set to false, auto-configuration is turned off and you can wire it up manually yourself.

## Development

Requires Java 8

### To Build

macOS or *nix:

    ./gradlew build

Windows:    

    gradlew.bat build
    
Additionally, each sub-module can be built in the same fashion.

#### Publishing to Bintray

    ./gradlew -PbintrayUser="..." -PbintrayKey="..." clean build dokkaJar bintrayUpload

