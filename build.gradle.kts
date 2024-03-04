plugins {
    `java-library`
    `java-test-fixtures`
    signing
    alias(libs.plugins.net.thebugmc.gradle.sonatype.central.portal.publisher)
}

group = providers.gradleProperty("GROUP_NAME").get()
version = providers.gradleProperty("VERSION_NAME").get()

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(rootProject.libs.io.netty.all)
    implementation(rootProject.libs.org.slf4j.api)
    implementation(rootProject.libs.io.github.ppzxc.fixh)

    testImplementation(rootProject.libs.org.junit.jupiter)
    testImplementation(rootProject.libs.org.assertj.core)

    testFixturesImplementation(rootProject.libs.io.github.ppzxc.fixh)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

java {
    withSourcesJar()
    withJavadocJar()
}

artifacts {
    archives(tasks.named("javadocJar"))
    archives(tasks.named("sourcesJar"))
}

signing {
    val signingKey = providers.environmentVariable("GPG_SIGNING_KEY")
    val signingPassphrase = providers.environmentVariable("GPG_SIGNING_PASSPHRASE")

    if (signingKey.isPresent && signingPassphrase.isPresent) {
        useInMemoryPgpKeys(signingKey.get(), signingPassphrase.get())
        val extension = extensions.getByName("publishing") as PublishingExtension
        sign(extension.publications)
    }
}

centralPortal {
    username = providers.environmentVariable("OSSRH_USERNAME").getOrElse("null")
    password = providers.environmentVariable("OSSRH_PASSWORD").getOrElse("null")
    pom {
        group = providers.gradleProperty("GROUP_NAME").get()
        name = providers.gradleProperty("ARTIFACT_NAME").get()
        description = providers.gradleProperty("POM_DESCRIPTION").get()
        packaging = "jar"
        // `name = project.name`, `description = project.description` and `packaging = "jar"`
        // are applied automatically, but you can override them here
        url = providers.gradleProperty("POM_URL").get()
        licenses {
            license {
                name = providers.gradleProperty("POM_LICENSE_NAME").get()
                url = providers.gradleProperty("POM_LICENSE_URL").get()
            }
        }
        developers {
            developer {
                id = providers.gradleProperty("POM_DEVELOPER_ID").get()
                name = providers.gradleProperty("POM_DEVELOPER_NAME").get()
                email = providers.gradleProperty("POM_DEVELOPER_EMAIL").get()
                url = providers.gradleProperty("POM_DEVELOPER_URL").get()
            }
        }
        scm {
            url = providers.gradleProperty("POM_SCM_URL").get()
            connection = providers.gradleProperty("POM_SCM_CONNECTION").get()
            developerConnection = providers.gradleProperty("POM_SCM_DEV_CONNECTION").get()
        }
    }
}