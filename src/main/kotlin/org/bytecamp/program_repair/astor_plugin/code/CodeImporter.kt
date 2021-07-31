package org.bytecamp.program_repair.astor_plugin.code

import com.intellij.psi.*

class CodeImporter {
    private val logger = com.intellij.openapi.diagnostic.Logger.getInstance(CodeImporter::class.java)

    fun flattenFile(psi: PsiElement): ArrayList<PsiMethod> {
        val result = ArrayList<PsiMethod>()
        if (psi is PsiClass) {
            for (m in psi.allMethods) {
                result.addAll(flattenFile(m))
            }
        } else if (psi is PsiMethod) {
            result.add(psi)
        } else {
            for (p in psi.children) {
                result.addAll(flattenFile(p))
            }
        }
        return result
    }

    fun importCode(src: PsiFile, modified: PsiFile): PsiFile {
        val result = src.copy() as PsiFile
        val flatSrc = flattenFile(result)
        val flatModified = flattenFile(modified)
        var ps = 0
        var pm = 0
        while (ps < flatSrc.size || pm < flatModified.size) {
            if (ps < flatSrc.size && pm < flatModified.size) {
                if (flatSrc[ps].body != null && flatModified[pm].body != null)
                    tryOverwriteBody(flatSrc[ps].body!!, flatModified[pm].body!!)
                pm += 1
                ps += 1
            } else {
                break
            }
        }
        return result
    }

    private fun ifIncludeSingle(psi: PsiElement): Boolean {
        if (psi.textLength == 0)
            return false
        if (psi is PsiComment)
            return false
        if (psi is PsiWhiteSpace)
            return false
        return true
    }

    fun expand(psi: PsiElement, recursive: Int): ArrayList<PsiElement> {
        val result = ArrayList<PsiElement>()
        if (psi is PsiBlockStatement) {
            result.addAll(expand(psi.children[0], recursive))
        } else if (psi.children.isNotEmpty()) {
            for (p in psi.children) {
                if (recursive > 0)
                    result.addAll(expand(p, recursive - 1))
                else if (ifIncludeSingle(p))
                    result.add(p)
            }
        } else {
            if (ifIncludeSingle(psi))
                result.add(psi)
        }

        return result
    }

    fun compareList(a: ArrayList<PsiElement>, b: ArrayList<PsiElement>): Boolean {
        if (a.size != b.size)
            return false
        for ((i, j) in a.zip(b)) {
            if (i.text != j.text)
                return false
        }
        return true
    }

    fun tryOverwriteBody(src: PsiElement, modified: PsiElement) {
        val filteredSrc = expand(src, 0)
        val filteredModified = expand(modified, 0)
        if (filteredSrc.size != filteredModified.size) {
            src.replace(modified)
        } else if (filteredSrc.size == 1) {
            src.replace(modified)
        } else {
            var ps = 0
            var pm = 0
            while (ps < filteredSrc.size || pm < filteredModified.size) {
                if (ps < filteredSrc.size && pm < filteredModified.size) {
                    if (!compareList(
                            expand(filteredSrc[ps], 100),
                            expand(filteredModified[pm], 100)
                        )
                    ) {
                        tryOverwriteBody(filteredSrc[ps], filteredModified[pm])
                    }
                    pm += 1
                    ps += 1
                } else {
                    break
                }
            }

        }
    }
}