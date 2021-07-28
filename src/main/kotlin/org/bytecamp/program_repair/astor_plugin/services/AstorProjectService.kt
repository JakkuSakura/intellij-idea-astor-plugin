package org.bytecamp.program_repair.astor_plugin.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import fr.inria.main.evolution.AstorMain
//import fr.inria.astor.core.setup.ConfigurationProperties
//import fr.inria.astor.core.solutionsearch.population.PopulationConformation
//import fr.inria.main.evolution.AstorMain
import org.apache.commons.cli.Options
import java.io.File
import java.util.*

private class AbsMain {
    var options = Options()

    init {
        options.addOption("id", true, "(Optional) Name/identified of the project to evaluate (Default: folder name)")
        options.addOption("mode", true, " (Optional) Mode (Default: jGenProg Mode)")
        options.addOption(
            "autoconfigure", true,
            " Auto-configure project. Must install https://github.com/tdurieux/project-info-maven-plugin"
        )
        options.addOption("location", true, "URL of the project to manipulate")
        options.addOption(
            "dependencies", true,
            "dependencies of the application, separated by char " + File.pathSeparator
        )
        options.addOption("package", true, "package to instrument e.g. org.commons.math")
        options.addOption(
            "failing", true,
            "failing test cases, separated by Path separator char (: in linux/mac  and ; in windows)"
        )
        options.addOption(
            "out", true,
            "(Optional) Out dir: place were solutions and intermediate program variants are stored. (Default: ./outputMutation/)"
        )
        options.addOption("help", false, "print help and usage")
        options.addOption("bug280", false, "Run the bug 280 from Apache Commons Math")
        options.addOption("bug288", false, "Run the bug 288 from Apache Commons Math")
        options.addOption("bug309", false, "Run the bug 309 from Apache Commons Math")
        options.addOption("bug428", false, "Run the bug 428 from Apache Commons Math")
        options.addOption("bug340", false, "Run the bug 340 from Apache Commons Math")

        // Optional parameters
        options.addOption(
            "jvm4testexecution", true,
            "(Optional) location of JVM that executes the mutated version of a program (Folder that contains java script, such as /bin/ )."
        )
        options.addOption(
            "jvm4evosuitetestexecution", true,
            "(Optional) location of JVM that executes the EvoSuite test cases. If it is not specified, Astor uses that one from property 'jvm4testexecution'"
        )
        options.addOption("maxgen", true, "(Optional) max number of generation a program variant is evolved")
        options.addOption(
            "population", true,
            "(Optional) number of population (program variants) that the approach evolves"
        )

        options.addOption("maxtime", true, "(Optional) maximum time (in minutes) to execute the whole experiment")

//        options.addOption(
//            "validation", true,
//            "(Optional) type of validation: process|evosuite. Default:"
//                    + ConfigurationProperties.properties.getProperty("validation")
//
//        )
//        options.addOption(
//            "flthreshold", true, "(Optional) threshold for Fault locatication. Default:"
//                    + ConfigurationProperties.properties.getProperty("flthreshold")
//        )
//
//        options.addOption(
//            "maxsuspcandidates", true,
//            "(Optional) Maximun number of suspicious statement considered. Default: "
//                    + ConfigurationProperties.properties.getProperty("maxsuspcandidates")
//        )
//
//        options.addOption(
//            "reintroduce", true,
//            "(Optional) indicates whether it reintroduces the original program in each generation (value: "
//                    + PopulationConformation.ORIGINAL.toString() + "), "
//                    + " reintroduces parent variant in next generation (value: "
//                    + PopulationConformation.PARENTS.toString() + "), "
//                    + " reintroduce the solution in the next generation (value: "
//                    + PopulationConformation.SOLUTIONS.toString() + ") "
//                    + " reintroduces origina and parents (value: original-parents) "
//                    + "or do not reintroduce nothing (value: none).  More than one option can be written, separated by: "
//                    + File.pathSeparator + "Default: "
//                    + ConfigurationProperties.properties.getProperty("reintroduce")
//        )

        options.addOption(
            "tmax1", true,
            "(Optional) maximum time (in miliseconds) for validating the failing test case "
        )
        options.addOption(
            "tmax2", true,
            "(Optional) maximum time (in miliseconds) for validating the regression test cases "
        )
        options.addOption(
            "stopfirst", true,
            "(Optional) Indicates whether it stops when it finds the first solution (default: true)"
        )
        options.addOption(
            "allpoints", true,
            "(Optional) True if analyze all points of a program validation during a generation. False for analyzing only one gen per generation."
        )

        options.addOption(
            "savesolution", false,
            "(Optional) Save on disk intermediate program variants (even those that do not compile)"
        )
        options.addOption("saveall", false, "(Optional) Save on disk all program variants generated")

        options.addOption("testbystep", false, "(Optional) Executes each test cases in a separate process.")

        options.addOption(
            "modificationpointnavigation", true,
            "(Optional) Method to navigate the modification point space of a variant: inorder, random, weight random (according to the gen's suspicous value)"
        )

        options.addOption(
            "mutationrate", true,
            "(Optional) Value between 0 and 1 that indicates the probability of modify one gen (default: 1)"
        )

        options.addOption(
            "probagenmutation", false,
            "(Optional) Mutates a gen according to its suspicious value. Default: always mutates gen."
        )

        options.addOption(
            "srcjavafolder", true,
            "(Optional) folder with the application source code files (default: /src/java/main/)"
        )

        options.addOption(
            "srctestfolder", true,
            "(Optional) folder with the test cases source code files (default: /src/test/main/)"
        )

        options.addOption(
            "binjavafolder", true,
            "(Optional) folder with the application binaries (default: /target/classes/)"
        )

        options.addOption(
            "bintestfolder", true,
            "(Optional) folder with the test cases binaries (default: /target/test-classes/)"
        )

        options.addOption(
            "multipointmodification", false,
            "(Optional) An element of a program variant (i.e., gen) can be modified several times in different generation"
        )

        options.addOption(
            "uniqueoptogen", true,
            "(Optional) An operation can be applied only once to a gen, even this operation is in other variant."
        )

        options.addOption(
            "resetoperations", false,
            "(Optional) The program variants do not pass the operators throughs the generations"
        )

        options.addOption(
            "regressionforfaultlocalization", true,
            "(Optional) Use the regression for fault localization.Otherwise, failing test cases"
        )

        options.addOption(
            "javacompliancelevel", true,
            "(Optional) Compliance level (e.g., 7 for java 1.7, 6 for java 1.6). Default Java 1.7"
        )

        options.addOption(
            "alternativecompliancelevel", true,
            "(Optional) Alternative compliance level. Default Java 1.4. Used after Astor tries to compile to the complicance level and fails."
        )

        options.addOption(
            "seed", true,
            "(Optional) Random seed, for reproducible runs.  Default is whatever java.util.Random uses when not explicitly initialized."
        )


        options.addOption(
            "maxdate", true,
            "(Optional) Indicates the hour Astor has to stop processing. it must have the format: HH:mm"
        )


    }

}
@Service
class AstorProjectService(project: Project) {
    val main: AstorMain = AstorMain()
    fun getHelp(): String {
        return "This is help"
    }
}
