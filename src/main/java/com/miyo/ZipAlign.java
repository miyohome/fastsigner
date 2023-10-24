package com.miyo;

import com.common.FileUtility;

public class ZipAlign {
    public static final String DEFAULT = "zipalign";
    public static String fastExecute(String inFilePath) throws Exception {
        String outFilePath = FileUtility.addSuffixToFileName(inFilePath, "align");

//        if (!inFilePath.startsWith("\"") && !inFilePath.endsWith("\"")) {
//            inFilePath = "\"" + inFilePath + "\"";
//        }
//        if (!outFilePath.startsWith("\"") && !outFilePath.endsWith("\"")) {
//            outFilePath = "\"" + outFilePath + "\"";
//        }

        String[] params = {
                "-p",
                "-f",
                "-v",
                "4",
                inFilePath,
                outFilePath
        };

        FileUtility.execute(DEFAULT, params, null);

        return outFilePath;
    }
}
