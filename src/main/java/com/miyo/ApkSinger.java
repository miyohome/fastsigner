package com.miyo;

import com.common.FileUtility;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;


public class ApkSinger {
    public static final String DEFAULT = "sign";

    public static String fastExecute(String jksFilePath, String inApkpath) throws Exception {
        String outFilePath = FileUtility.addSuffixToFileName(inApkpath, "sign");

        // apksigner sign --ks my-release-key.jks --out %RootPath%\hello_signed-align.apk %RootPath%\hello_unsigner.apk
        String[] params = {
                "sign",
                "--v2-signing-enabled", "true",
                "--v3-signing-enabled", "true",
                "-ks", jksFilePath,
                "--out", outFilePath, inApkpath,
        };

        StringBuilder stringBuilder = new StringBuilder("apksigner");
        for (String p: params) {
            stringBuilder.append(" ");
            stringBuilder.append(p);
        }
        System.out.println(stringBuilder);

        byte[] in = Keytool.DEFAULT_VALUE.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(in);

        System.setIn(byteArrayInputStream);

        try {
            com.android.apksigner.ApkSignerTool.main(params);
        } catch (Exception e) {
            System.out.println(e);
        }
        return outFilePath;
    }
}
