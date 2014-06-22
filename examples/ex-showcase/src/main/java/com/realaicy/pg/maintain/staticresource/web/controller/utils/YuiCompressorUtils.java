package com.realaicy.pg.maintain.staticresource.web.controller.utils;

import com.realaicy.pg.core.Constants;
import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import java.io.*;

/**
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class YuiCompressorUtils {

    /**
     * 压缩js/css  自动生成的压缩文件在当前目录下并以.min.js/css结尾
     *
     * @param fileName xxx
     * @return 返回压缩好的文件名 xxx
     */
    public static String compress(final String fileName) {
        String minFileName;
        final String extension = FilenameUtils.getExtension(fileName);
        if ("js".equalsIgnoreCase(extension)) {
            minFileName = fileName.substring(0, fileName.length() - 3) + ".min.js";
        } else if ("css".equals(extension)) {
            minFileName = fileName.substring(0, fileName.length() - 4) + ".min.css";
        } else {
            throw new RuntimeException("file type only is js/css, but was fileName:" + fileName + ", extension:" + extension);
        }
        Reader in = null;
        Writer out = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), Constants.ENCODING));
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(minFileName), Constants.ENCODING));

            if ("js".equals(extension)) {

                CustomErrorReporter errorReporter = new CustomErrorReporter();

                JavaScriptCompressor compressor = new JavaScriptCompressor(in, errorReporter);
                compressor.compress(out, 10, true, false, false, false);

                if (errorReporter.hasError()) {
                    throw new RuntimeException(errorReporter.getErrorMessage());
                }

            } else if ("css".equals(extension)) {

                CssCompressor compressor = new CssCompressor(in);
                compressor.compress(out, 10);
            }
        } catch (Exception e) {
            //如果失败了，直接做个副本，防止加载js/css出错
            try {
                FileUtils.copyFile(new File(fileName), new File(minFileName));
            } catch (IOException ioException) {
                throw new RuntimeException("compress error:" + ioException.getMessage(), ioException);
            }
            throw new RuntimeException("compress error:" + e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {
                }
            }
        }

        if (FileUtils.sizeOf(new File(minFileName)) == 0) {
            try {
                FileUtils.copyFile(new File(fileName), new File(minFileName));
            } catch (IOException ioException) {
                throw new RuntimeException("compress error:" + ioException.getMessage(), ioException);
            }
        }

        return minFileName;
    }

    public static boolean hasCompress(String fileName) {
        return fileName.toLowerCase().endsWith(".min.js") || fileName.endsWith(".min.css");
    }

    public static String getCompressFileName(String fileName) {
        if (hasCompress(fileName)) {
            return fileName;
        }
        String compressFileName = null;
        final String extension = FilenameUtils.getExtension(fileName);
        if ("js".equalsIgnoreCase(extension)) {
            compressFileName = fileName.substring(0, fileName.length() - 3) + ".min.js";
        } else if ("css".equals(extension)) {
            compressFileName = fileName.substring(0, fileName.length() - 4) + ".min.css";
        }
        return compressFileName;
    }

    public static String getNoneCompressFileName(String fileName) {
        if (!hasCompress(fileName)) {
            return fileName;
        }
        String noneCompressFileName = null;
        final String extension = FilenameUtils.getExtension(fileName);
        if ("js".equalsIgnoreCase(extension)) {
            noneCompressFileName = fileName.substring(0, fileName.length() - 7) + ".js";
        } else if ("css".equals(extension)) {
            noneCompressFileName = fileName.substring(0, fileName.length() - 8) + ".css";
        }
        return noneCompressFileName;
    }

    private static class CustomErrorReporter implements ErrorReporter {
        private StringBuilder error = new StringBuilder();

        public boolean hasError() {
            return error.length() > 0;
        }

        public String getErrorMessage() {
            return error.toString();
        }

        public void warning(String message, String sourceName,
                            int line, String lineSource, int lineOffset) {
            if (line < 0) {
                error.append("\n[WARNING] ").append(message);
            } else {
                error.append("\n[WARNING] ").append(line).append(':').append(lineOffset).append(':').append(message);
            }
        }

        public void error(String message, String sourceName,
                          int line, String lineSource, int lineOffset) {
            if (line < 0) {
                error.append("\n[ERROR] ").append(message);
            } else {
                error.append("\n[ERROR] ").append(line).append(':').append(lineOffset).append(':').append(message);
            }
        }

        public EvaluatorException runtimeError(String message, String sourceName,
                                               int line, String lineSource, int lineOffset) {
            error(message, sourceName, line, lineSource, lineOffset);
            return new EvaluatorException(message);
        }
    }

}
