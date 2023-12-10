pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                // Do not change the username below.
                // This should always be `mapbox` (not your username).
                username = "mapbox"
                // Use the secret token you stored in gradle.properties as the password
                password = "sk.eyJ1IjoicmFmYS00NzgwIiwiYSI6ImNsb2Q1bTd3bDA0NGcyanFjdnRxZm14cGsifQ.Gw-LViSXAgwQt4QaX6TATw"
            }
        }

    }
}

rootProject.name = "Beast_Tower"
include(":app")
