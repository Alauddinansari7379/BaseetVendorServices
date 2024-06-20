pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

        maven { url = uri("https://www.jitpack.io") }
        maven {
            url =uri("https://github.com/jitsi/jitsi-maven-repository/raw/master/releases")
        }

    }

}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url =uri("https://github.com/jitsi/jitsi-maven-repository/raw/master/releases")
        }
        maven { url = uri("https://www.jitpack.io") }
    }
}

rootProject.name = "Vendor Services"
include(":app")
 