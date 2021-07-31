package org.bytecamp.program_repair.astor_plugin.code

import org.bytecamp.program_repair.astor_plugin.utils.MString
import java.util.*
import kotlin.collections.ArrayList
import kotlin.streams.toList


class CodeReferencesBuilder(var src: String, clean_up: Boolean = true) {
    var i = 0
    var braceLevel = 0
    var codeBlocks = Stack<ICodeBlock>()
    var codeBlock: PlainCodeBlock? = null

    init {
        val imports = buildImports()
        codeBlocks.push(CodeReferences(imports, ArrayList()))
        if (clean_up) {
            for (imp in imports) {
                src = src.replace("$imp(", imp.split(".").last() + "(")
            }
        }
    }

    private fun matchPair(prefix: String, postfix: String, type: BlockType): Boolean {
        if (src.substring(i).startsWith(prefix)) {
            tryAddCodeBlock()
            val end = src.indexOf(postfix, i + prefix.length) + postfix.length
            codeBlock = PlainCodeBlock(type, MString(src.substring(i, end)))
            i = end
            return true
        }
        return false
    }


    fun buildImports(): ArrayList<String> {
        val list = this.src.split(";").stream().filter { x -> x.startsWith("import") }
            .map { x -> x.substring("import ".length) }.toList()
        return ArrayList(list)
    }

    fun tryAddCodeBlock() {
        if (codeBlock != null) {
            codeBlocks.peek().addBlock(codeBlock!!)
            codeBlock = null
        }
    }

    fun pushBlock(block: ICodeBlock) {
        codeBlocks.peek().addBlock(block)
        codeBlocks.push(block)
    }

    fun popBlock(): ICodeBlock {
        return codeBlocks.pop()
    }

    fun matchClass(): Boolean {
        if (src.substring(i).startsWith("class")) {
            tryAddCodeBlock()
            val leftBrace = src.indexOf("{", i)
            pushBlock(
                ClassCodeBlock(
                    arrayListOf(
                        PlainCodeBlock(
                            BlockType.ClassHead,
                            MString(src.substring(i, leftBrace + 1))
                        )
                    )
                )
            )
            i = leftBrace + 1
            val enterBraceLevel = braceLevel
            braceLevel += 1
            while (i < src.length && braceLevel > enterBraceLevel) {
                if (matchComments())
                    continue
                if (matchFunction())
                    continue
                if (src[i] == '}') {
                    braceLevel -= 1
                    matchPlainCode()
                    continue
                }

                matchPlainCode()
            }
            popBlock()
            return true
        }
        return false
    }

    private fun matchFunction(): Boolean {
        if (src[i].isLetter() || src[i] == '@') {
            val enterBraceLevel = braceLevel
            var entered = false
            pushBlock(FunctionCodeBlock(ArrayList()))
            while (i < src.length && (!entered || braceLevel > enterBraceLevel)) {
                if (src[i] == '{') {
                    entered = true
                    braceLevel += 1
                    matchPlainCode()
                    continue
                }
                if (src[i] == '}') {
                    braceLevel -= 1
                    matchPlainCode()
                    continue
                }
                if (matchComments())
                    continue
                matchPlainCode()
            }
            tryAddCodeBlock()
            popBlock()
            return true
        }
        return false
    }

    private fun matchComments(): Boolean {
        return matchPair("//", "\n", BlockType.Comment) || matchPair("/*", "*/", BlockType.Comment)
    }

    private fun matchPlainCode() {
        // code
        if (codeBlock == null)
            codeBlock = PlainCodeBlock(BlockType.Code, MString(src.substring(i, i + 1)))
        else if (codeBlock!!.type != BlockType.Code) {
            tryAddCodeBlock()
            codeBlock = PlainCodeBlock(BlockType.Code, MString(src.substring(i, i + 1)))
        } else {
            codeBlock!!.content.append(src[i])
        }

        i += 1
    }

    fun buildReferences(): CodeReferences {
        while (i < src.length) {
            if (matchComments())
                continue
            if (matchClass())
                continue
            matchPlainCode()
        }
        tryAddCodeBlock()
        return codeBlocks.pop() as CodeReferences
    }
}

sealed interface ICodeBlock {
    fun getBlockType(): BlockType
    fun addBlock(block: ICodeBlock)
}

enum class BlockType {
    Comment,
    Code,
    ClassHead,
    ClassBlock,
    FunctionBlock,
    File
}

data class FunctionCodeBlock(val blocks: ArrayList<ICodeBlock>) : ICodeBlock {
    override fun getBlockType(): BlockType {
        return BlockType.FunctionBlock
    }

    override fun addBlock(block: ICodeBlock) {
        blocks.add(block)
    }

}

data class ClassCodeBlock(val blocks: ArrayList<ICodeBlock>) : ICodeBlock {
    override fun getBlockType(): BlockType {
        return BlockType.ClassBlock
    }

    override fun addBlock(block: ICodeBlock) {
        blocks.add(block)
    }

}

data class PlainCodeBlock(val type: BlockType, var content: MString = MString()) : ICodeBlock {
    override fun getBlockType(): BlockType {
        return type
    }

    override fun addBlock(block: ICodeBlock) {
        throw IllegalStateException("Could not add block")
    }

}

data class CodeReferences(val imports: ArrayList<String>, val blocks: ArrayList<ICodeBlock>) :
    ICodeBlock {
    override fun getBlockType(): BlockType {
        return BlockType.File
    }

    override fun addBlock(block: ICodeBlock) {
        blocks.add(block)
    }

}