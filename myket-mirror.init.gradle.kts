import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository

val myketUrl = "https://maven.myket.ir/"

val mirroredHosts = listOf(
    "plugins.gradle.org",
    "dl.google.com",
    "maven.google.com",
    "repo.maven.apache.org",
    "repo1.maven.org",
    "jcenter.bintray.com",
    "jitpack.io",
    "maven.fabric.io",
    "developer.huawei.com"
)

fun RepositoryHandler.redirectToMyket() {
    all {
        if (this is MavenArtifactRepository) {
            val host = url.host
            if (mirroredHosts.any { host == it || host.endsWith(".$it") }) {
                logger.lifecycle("[myket-mirror] $url -> $myketUrl")
                setUrl(myketUrl)
            }
        }
    }
}

gradle.beforeSettings {
    pluginManagement.repositories.redirectToMyket()
    dependencyResolutionManagement.repositories.redirectToMyket()
}

gradle.allprojects {
    buildscript {
        repositories.redirectToMyket()
    }
    repositories.redirectToMyket()
}

gradle.projectsEvaluated {
    allprojects {
        buildscript.repositories.redirectToMyket()
        repositories.redirectToMyket()
    }
}
