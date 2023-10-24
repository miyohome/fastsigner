package com.miyo;

import com.common.FileUtility;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.Arrays;

public class AndroidSigningSuite {
    private static final String VERSION_CODE = "1.1.0";



    public static void main(String[] params) {
        try {
            if (params.length != 0 && !"--help".equals(params[0]) && !"-h".equals(params[0])) {
                String cmd = params[0];

                if ("--version".equals(cmd)) {
                    System.out.println(VERSION_CODE);
                } else {
                    if ("zipalign".equals(cmd)) {
                        zipalign(Arrays.copyOfRange(params, 1, params.length));
                    } else if ("keytool".equals(cmd)) {
                        keytool(Arrays.copyOfRange(params, 1, params.length));
                    } else if ("sign".equals(cmd)) {
                        sign(Arrays.copyOfRange(params, 1, params.length));
                    } else if ("fastsign".equals(cmd)) {
                        fastSigner(Arrays.copyOfRange(params, 1, params.length));
                    } else {
                        throw new InvalidParameterException("Unsupported command: " + cmd + ". See --help for supported commands");
                    }

                }
            } else {
                printUsage("help.txt");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static void printUsage(String page) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(AndroidSigningSuite.class.getClassLoader().getResourceAsStream(page), StandardCharsets.UTF_8));

            String line;
            try {
                while((line = in.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (Throwable throwable1) {
                try {
                    in.close();
                } catch (Throwable throwable2) {
                    throwable1.addSuppressed(throwable2);
                }

                throw throwable1;
            }

            in.close();
        } catch (IOException var6) {
            throw new RuntimeException("Failed to read " + page + " resource");
        }
    }


    private static void zipalign(String[] params) throws Exception {
        FileUtility.execute(ZipAlign.DEFAULT, params, null);
    }

    private static void keytool(String[] params) throws Exception {
        FileUtility.execute(Keytool.DEFAULT, params, null);
    }
    private static void sign(String[] params) throws Exception {
        FileUtility.execute(ApkSinger.DEFAULT, params, null);
    }
    private static void fastSigner(String[] params) throws Exception {
        if (params.length == 0 || "--help".equals(params[0]) || "-h".equals(params[0])) {
            printUsage("help_fastsign.txt");
        } else {
            if (!Paths.get(params[0]).isAbsolute()) {
                System.out.println(String.format("\"%s\" is not correct apkfile", params[0]));
                return;
            }

            String outFilePath = ZipAlign.fastExecute(params[0]);

            String jksPath = new File(outFilePath).getParentFile().getPath() + "\\keystore.jks";
            Keytool.fastExecute(jksPath);

            ApkSinger.fastExecute(jksPath, outFilePath);
            System.out.println("fastSigner done.");
        }
    }
}