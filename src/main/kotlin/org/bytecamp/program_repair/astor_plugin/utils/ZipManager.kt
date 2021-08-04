package org.bytecamp.program_repair.astor_plugin.utils

import java.io.File
import java.io.FileInputStream
import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object ZipManager {

    fun zip(base: File, files: List<File>, out: OutputStream) {
        val buf = ByteArray(1024);

        val zos = ZipOutputStream(out);
        for (file in files) {
            val fis = FileInputStream(file);
            zos.putNextEntry(ZipEntry(file.relativeTo(base).path));

            while (true) {
                val len = fis.read(buf)
                if (len < 0)
                    break
                zos.write(buf, 0, len)
            }
            zos.closeEntry()
            fis.close()
        }
        zos.close();

    }

}