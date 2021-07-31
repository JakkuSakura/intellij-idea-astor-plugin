package org.bytecamp.program_repair.astor_plugin.utils

class MString(s: String = "") {
    private val sb = StringBuilder(s)
    fun append(s: String) {
        sb.append(s)
    }
    fun append(s: Char) {
        sb.append(s)
    }
    override fun equals(other: Any?): Boolean {
        if (other is MString) {
            return toString() == other.toString()
        }
        return false
    }

    override fun hashCode(): Int {
        return sb.toString().hashCode()
    }

    override fun toString(): String {
        return sb.toString()
    }
}