package org.bytecamp.program_repair.astor_plugin.configs

import java.io.File

object InputKeys {
    const val MODE = "-mode"
    const val LOCATION = "-location"
    const val PACKAGE = "-package"
    const val SRC = "-srcjavafolder"
    const val SRC_TEST = "-srctestfolder"
    const val BIN = "-binjavafolder"
    const val BIN_TEST = "-bintestfolder"
    const val OUT = "-out"
}

data class AstorInputConfig(
    var mode: String = "jGenProg",
    var projectName: String = "",
    var pkg: String = "",
    var location: String = "",
    var src: String = "",
    var srcTest: String = "",
    var bin: String = "",
    var binTest: String = "",
    var out: String = ""
) {
    private var normaized = false
    private fun normalize() {
        if (!normaized) {
            val location = File(this.location)
            src = File(src).relativeTo(location).path
            srcTest = File(srcTest).relativeTo(location).path
            bin = File(bin).relativeTo(location).path
            binTest = File(binTest).relativeTo(location).path
            normaized = true
        }
    }

    fun toArgs(): Array<String> {
        normalize()
        return arrayOf(
            InputKeys.MODE,
            mode,
            InputKeys.PACKAGE,
            pkg,
            InputKeys.LOCATION,
            location,
            InputKeys.SRC,
            src,
            InputKeys.SRC_TEST,
            srcTest,
            InputKeys.BIN,
            bin,
            InputKeys.BIN_TEST,
            binTest,
            InputKeys.OUT,
            out
        )
    }

    override fun toString(): String {
        return toArgs().joinToString(" ")
    }
}

/*
{
  "general": {
    "NR_RIGHT_COMPILATIONS": 109,
    "NR_ERRONEOUS_VARIANCES": null,
    "EXECUTION_IDENTIFIER": "",
    "OUTPUT_STATUS": "MAX_GENERATION",
    "NR_FAILLING_COMPILATIONS": 82,
    "NR_GENERATIONS": 200,
    "TOTAL_TIME": 24.391,
    "NR_FAILING_VALIDATION_PROCESS": null
  },
  "patches": [
    {
      "VARIANT_ID": "280",
      "VALIDATION": "|true|0|2181|[]|",
      "patchhunks": [
        {
          "LOCATION": "org.apache.commons.math.analysis.solvers.BisectionSolver",
          "PATH": "\\\/home\\\/jack\\\/Dev\\\/program-repair\\\/intellij-idea-astor-plugin\\\/astor\\\/examples\\\/math_70\\\/src\\\/main\\\/java\\\/org\\\/apache\\\/commons\\\/math\\\/analysis\\\/solvers\\\/BisectionSolver.java",
          "INGREDIENT_SCOPE": "PACKAGE",
          "SUSPICIOUNESS": "1",
          "INGREDIENT_PARENT": "return solve(f, initial, max)",
          "PATCH_HUNK_TYPE": "CtReturnImpl|CtBlockImpl",
          "ORIGINAL_CODE": "return solve(min, max)",
          "BUGGY_CODE_TYPE": "CtReturnImpl|CtBlockImpl",
          "OPERATOR": "ReplaceOp",
          "MODIFIED_FILE_PATH": "\\\/home\\\/jack\\\/Dev\\\/program-repair\\\/intellij-idea-astor-plugin\\\/astor\\\/examples\\\/math_70\\\/target\\\/astor\\\/AstorMain-math_70\\\/\\\/src\\\/\\\/variant-280\\\/org\\\/apache\\\/commons\\\/math\\\/analysis\\\/solvers\\\/BisectionSolver.java",
          "LINE": "72",
          "MP_RANKING": "0",
          "PATCH_HUNK_CODE": "return solve(f, initial, max)"
        }
      ],
      "TIME": "18",
      "GENERATION": "140",
      "FOLDER_SOLUTION_CODE": "\\\/home\\\/jack\\\/Dev\\\/program-repair\\\/intellij-idea-astor-plugin\\\/astor\\\/examples\\\/math_70\\\/target\\\/astor\\\/AstorMain-math_70\\\/\\\/src\\\/\\\/variant-280",
      "PATCH_DIFF_ORIG": "--- \\\/src\\\/main\\\/java\\\/org\\\/apache\\\/commons\\\/math\\\/analysis\\\/solvers\\\/BisectionSolver.java\\n+++ \\\/src\\\/main\\\/java\\\/org\\\/apache\\\/commons\\\/math\\\/analysis\\\/solvers\\\/BisectionSolver.java\\n@@ -72 +71,0 @@\\n-\\t\\treturn solve(min, max);\\n@@ -74 +72,0 @@\\n-\\t}\\n@@ -77,2 +74,0 @@\\n-\\tpublic double solve(final org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max) throws \\n-\\torg.apache.commons.math.MaxIterationsExceededException, org.apache.commons.math.FunctionEvaluationException {\\n@@ -80,5 +75,0 @@\\n-\\t\\tclearResult();\\n-\\t\\tverifyInterval(min, max);\\n-\\t\\tdouble m;\\n-\\t\\tdouble fm;\\n-\\t\\tdouble fmin;\\n@@ -86,5 +76,0 @@\\n-\\t\\tint i = 0;\\n-\\t\\twhile (i < maximalIterationCount) {\\n-\\t\\t\\tm = org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils.midpoint(min, max);\\n-\\t\\t\\tfmin = f.value(min);\\n-\\t\\t\\tfm = f.value(m);\\n@@ -92 +77,0 @@\\n-\\t\\t\\tif ((fm * fmin) > 0.0) {\\n@@ -94,2 +78,0 @@\\n-\\t\\t\\t\\tmin = m;\\n-\\t\\t\\t} else {\\n@@ -97,2 +79,0 @@\\n-\\t\\t\\t\\tmax = m;\\n-\\t\\t\\t}\\n@@ -100,7 +80,0 @@\\n-\\t\\t\\tif (java.lang.Math.abs(max - min) <= absoluteAccuracy) {\\n-\\t\\t\\t\\tm = org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils.midpoint(min, max);\\n-\\t\\t\\t\\tsetResult(m, i);\\n-\\t\\t\\t\\treturn m;\\n-\\t\\t\\t}\\n-\\t\\t\\t++i;\\n-\\t\\t} \\n@@ -108,2 +82,64 @@\\n-\\t\\tthrow new org.apache.commons.math.MaxIterationsExceededException(maximalIterationCount);\\n-\\t}}\\n\\\\ No newline at end of file\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\n+\\t\\treturn solve(f, initial, max);} \\tpublic double solve(final org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max) throws org.apache.commons.math.MaxIterationsExceededException, org.apache.commons.math.FunctionEvaluationException { \\t\\tclearResult(); \\t\\tverifyInterval(min, max); \\t\\tdouble m; \\t\\tdouble fm; \\t\\tdouble fmin; \\t\\tint i = 0; \\t\\twhile (i < maximalIterationCount) { \\t\\t\\tm = org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils.midpoint(min, max); \\t\\t\\tfmin = f.value(min); \\t\\t\\tfm = f.value(m); \\t\\t\\tif ((fm * fmin) > 0.0) { \\t\\t\\t\\tmin = m;} else { \\t\\t\\t\\tmax = m;} \\t\\t\\tif (java.lang.Math.abs(max - min) <= absoluteAccuracy) { \\t\\t\\t\\tm = org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils.midpoint(min, max); \\t\\t\\t\\tsetResult(m, i); \\t\\t\\t\\treturn m;} \\t\\t\\t++i;} \\t\\tthrow new org.apache.commons.math.MaxIterationsExceededException(maximalIterationCount);}}\\n\\\\ No newline at end of file\\n\\n"
    }
  ]
}
 */
@Suppress("PropertyName")
data class PatchHunk(
    val OPERATOR: String = "",
    val LOCATION: String = "",
    val PATH: String = "",
    val MODIFIED_FILE_PATH: String = "",
    val LINE: String = "",
    val SUSPICIOUNESS: String = "",
    val MP_RANKING: String = "",
    val ORIGINAL_CODE: String = "",
    val BUGGY_CODE_TYPE: String = "",
    val PATCH_HUNK_CODE: String = "",
    val PATCH_HUNK_TYPE: String = "",
    val INGREDIENT_SCOPE: String = "",
    val INGREDIENT_PARENT: String = ""
)

@Suppress("PropertyName")
data class AstorGeneral(
    val NR_RIGHT_COMPILATIONS: Int = -1,
    val NR_ERRONEOUS_VARIANCES: String? = "",
    val EXECUTION_IDENTIFIER: String = "",
    val OUTPUT_STATUS: String = "",
    val NR_FAILLING_COMPILATIONS: Int = -1,
    val NR_GENERATIONS: Int = -1,
    val TOTAL_TIME: Double = 0.0,
    val NR_FAILING_VALIDATION_PROCESS: String? = ""

)

@Suppress("PropertyName")
data class AstorPatch(
    val VARIANT_ID: String = "",
    val TIME: String = "",
    val VALIDATION: String = "",
    val GENERATION: String = "",
    val FOLDER_SOLUTION_CODE: String = "",
    val HUNKS: String = "",
    val PATCH_DIFF_ORIG: String = "",
    val patchhunks: ArrayList<PatchHunk> = ArrayList()
)

@Suppress("PropertyName")
data class AstorOutputConfig(
    val general: AstorGeneral = AstorGeneral(),
    val patches: ArrayList<AstorPatch> = ArrayList()
)