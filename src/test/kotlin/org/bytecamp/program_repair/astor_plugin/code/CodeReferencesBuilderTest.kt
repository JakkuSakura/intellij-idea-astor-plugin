package org.bytecamp.program_repair.astor_plugin.code

import org.bytecamp.program_repair.astor_plugin.utils.MString
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals

internal class CodeReferencesBuilderTest {

    @Test
    fun testBuildReferencesSrc() {
        val builder = CodeReferencesBuilder("//hello\nvoid foo();")
        val references = builder.buildReferences()
        assertEquals(2, references.blocks.size)
        assertEquals(PlainCodeBlock(BlockType.Comment, MString("//hello\n")), references.blocks[0])
        assertEquals(PlainCodeBlock(BlockType.Code, MString("void foo();")), references.blocks[1])
    }

    @Test
    fun testBuildReferencesSrcBlockComment() {
        val builder = CodeReferencesBuilder("/*hello*/void foo();")
        val references = builder.buildReferences()
        assertEquals(2, references.blocks.size)
        assertEquals(PlainCodeBlock(BlockType.Comment, MString("/*hello*/")), references.blocks[0])
        assertEquals(PlainCodeBlock(BlockType.Code, MString("void foo();")), references.blocks[1])
    }

    @Test
    fun testBuildReferencesModified() {
        val builder = CodeReferencesBuilder("int foo();")
        val references = builder.buildReferences()
        assertEquals(1, references.blocks.size)
        assertEquals(PlainCodeBlock(BlockType.Code, MString("int foo();")), references.blocks[0])
    }

    @Test
    fun testClassAndFunction() {
        val builder = CodeReferencesBuilder("class Foo { void bar() {} }")
        val references = builder.buildReferences()
        assertEquals(2, references.blocks.size)
        assertEquals(
            ClassCodeBlock(
                arrayListOf(
                    PlainCodeBlock(BlockType.ClassHead, MString("class Foo {")),
                    FunctionCodeBlock(
                        arrayListOf(PlainCodeBlock(BlockType.Code, MString(" void bar() {}")))
                    ),

                )
            ),
            references.blocks[0]
        )
    }

    @Test
    fun testImports() {
        val builder = CodeReferencesBuilder("import com.company.foo;/*hello*/void foo();")
        val imports = builder.buildImports()
        assertArrayEquals(arrayOf("com.company.foo"), imports.toArray())
    }

    @Test
    fun testCleanUp() {
        val builder = CodeReferencesBuilder("import com.company.foo; new com.company.foo()")
        assertEquals(PlainCodeBlock(BlockType.Code, MString("import com.company.foo; new foo()")), builder.buildReferences().blocks[0])
    }
}