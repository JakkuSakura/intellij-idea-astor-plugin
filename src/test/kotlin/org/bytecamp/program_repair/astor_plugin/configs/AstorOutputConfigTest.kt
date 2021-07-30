package org.bytecamp.program_repair.astor_plugin.configs

import com.google.gson.Gson
import kotlinx.serialization.json.Json
import org.junit.Test

internal class AstorOutputConfigTest {
    @Test
    fun testJsonParsing() {
        val classLoader = javaClass.classLoader
        val file = classLoader.getResource("astor_output.json")?.file!!
        val config = Gson().fromJson(file, AstorOutputConfig::class.java)
    }
}