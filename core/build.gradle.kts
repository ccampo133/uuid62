buildscript {
    ext {
        kotlinVersion = '1.3.20'
        assertJVersion = '3.9.0'
    }
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    }
}

plugins {
    id "org.jetbrains.kotlin.jvm" version "1.3.31"
}

jar {
    baseName = 'uuid62-core'
    version = '0.1.0-SNAPSHOT'
}

compileKotlin {
    kotlinOptions {
        freeCompilerArgs = ['-Xjsr305=strict']
        jvmTarget = '1.8'
    }
}

compileTestKotlin {
    kotlinOptions {
        freeCompilerArgs = ['-Xjsr305=strict']
        jvmTarget = '1.8'
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.jetbrains.kotlin:kotlin-stdlib-jdk8'
    implementation 'org.jetbrains.kotlin:kotlin-reflect'
    testImplementation 'org.jetbrains.kotlin:kotlin-test'
    testImplementation 'org.jetbrains.kotlin:kotlin-test-junit'
    testImplementation "org.assertj:assertj-core:$assertJVersion"
}
