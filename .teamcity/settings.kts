import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2018_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2019.1"

project {

    vcsRoot(HttpsGithubComG0t4teamcityCourseCardsGit)

    buildType(DelpoyToStage)
    buildType(id02Chrome)
    buildType(Fasttests)
    buildType(id03Firefox)

    template(ZeldaTemplate)
}

object id02Chrome : BuildType({
    templates(ZeldaTemplate)
    id("02Chrome")
    name = "02. Chrome"

    params {
        param("Browser", "Chrome")
    }

    dependencies {
        snapshot(Fasttests) {
        }
    }
})

object id03Firefox : BuildType({
    templates(ZeldaTemplate)
    id("03Firefox")
    name = ".03 Firefox"

    params {
        param("Browser", "Firefox")
    }

    dependencies {
        snapshot(Fasttests) {
        }
    }
})

object DelpoyToStage : BuildType({
    name = "Delpoy to Stage"

    vcs {
        root(HttpsGithubComG0t4teamcityCourseCardsGit)
    }

    dependencies {
        snapshot(id02Chrome) {
        }
        snapshot(id03Firefox) {
        }
    }
})

object Fasttests : BuildType({
    templates(ZeldaTemplate)
    name = "fasttests"

    params {
        param("Browser", "PhantomJS")
    }

    steps {
        script {
            name = "BrowserTests"
            id = "RUNNER_7"
            scriptContent = "npm test -- --single-run --browsers %Browser%--colors false --reporters teamcity"
        }
    }
})

object ZeldaTemplate : Template({
    name = "Zelda template"

    params {
        param("Browser", "")
    }

    vcs {
        root(HttpsGithubComG0t4teamcityCourseCardsGit)
    }

    steps {
        script {
            name = "npm install"
            id = "RUNNER_6"
            scriptContent = "npm install"
        }
    }

    triggers {
        vcs {
            id = "vcsTrigger"
        }
    }
})

object HttpsGithubComG0t4teamcityCourseCardsGit : GitVcsRoot({
    name = "https://github.com/g0t4/teamcity-course-cards.git"
    url = "https://github.com/g0t4/teamcity-course-cards.git"
})
