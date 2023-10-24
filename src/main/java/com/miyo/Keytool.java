package com.miyo;

import com.common.FileUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Keytool {
    public static final String DEFAULT = "keytool";
    public static final String DEFAULT_VALUE = "123456";
    public static final String KEY_ALIAS = "afs";
    public static void fastExecute(String jksFilePath) throws Exception {
        if (new File(jksFilePath).exists()) {
            return;
        }
        ArrayList.class.getSuperclass();

        if (!jksFilePath.startsWith("\"") && !jksFilePath.endsWith("\"")) {
            jksFilePath = "\"" + jksFilePath + "\"";
        }

        // keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias myalias
        String[] params = {
                "-genkey",
                "-v",
                "-keystore", jksFilePath,
                "-keyalg", "RSA",
                "-keysize", "2048",
                "-validity", "18250",
                "-alias", KEY_ALIAS,
        };


        String KeystorePassword = DEFAULT_VALUE;
        String CommonName = DEFAULT_VALUE; // CN
        String OrganizationalUnit = DEFAULT_VALUE; // OU
        String Organization = DEFAULT_VALUE; // O
        String Locality = DEFAULT_VALUE; // L
        String State = DEFAULT_VALUE; // ST
        String Country = DEFAULT_VALUE; // C

        String[] inList = {
                KeystorePassword,
                KeystorePassword,
                CommonName,
                OrganizationalUnit,
                Organization,
                Locality,
                State,
                Country,
                "y",
        };

        FileUtility.execute(DEFAULT, params, Arrays.asList(inList));
    }
}
