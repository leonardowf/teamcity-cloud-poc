import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.finishBuildTrigger
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

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

version = "2021.1"

project {

    buildType(IOSContainer)
    buildType(Checks)
    buildType(AndroidContainer)
}

object AndroidContainer : BuildType({
    name = "Android Container"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        script {
            scriptContent = "./android.sh"
        }
    }

    triggers {
        finishBuildTrigger {
            buildType = "${Checks.id}"
            successfulOnly = true
        }
    }

    dependencies {
        snapshot(Checks) {
            onDependencyFailure = FailureAction.FAIL_TO_START
        }
    }

    requirements {
        matches("teamcity.agent.jvm.os.family", "Linux")
    }
})

object Checks : BuildType({
    name = "Checks"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        script {
            scriptContent = "./checks.sh"
        }
    }

    triggers {
        vcs {
        }
    }

    requirements {
        matches("teamcity.agent.jvm.os.family", "Linux")
    }
})

object IOSContainer : BuildType({
    name = "iOS container"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        script {
            name = "Apple"
            scriptContent = "./ios.sh"
        }
    }

    triggers {
        finishBuildTrigger {
            buildType = "${Checks.id}"
        }
    }

    dependencies {
        snapshot(Checks) {
            onDependencyFailure = FailureAction.FAIL_TO_START
        }
    }

    requirements {
        matches("teamcity.agent.jvm.os.name", "Mac OS X")
    }
})
