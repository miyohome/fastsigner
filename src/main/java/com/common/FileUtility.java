package com.common;

import com.common.process.ProcessCallback;
import com.common.process.ProcessFactory;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileUtility {
    public static void copyStreamAndNotify(InputStream inputStream, OutputStream outputStream) {
        int nRead;
        byte[] result = new byte[0x1000];
        try {
            while ((nRead = inputStream.read(result, 0, result.length)) != -1) {
                outputStream.write(result, 0, nRead);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static StringBuilder appendStrings(StringBuilder builder, String... args) {
        for (String s : args) {
            builder.append(s);
        }
        return builder;
    }

    public static String addSuffixToFileName(String filePath, String suffix) {
        int dotIndex = filePath.lastIndexOf('.');
        if (dotIndex != -1) {
            String fileName = filePath.substring(0, dotIndex);
            String extension = filePath.substring(dotIndex);
            return fileName + "_" + suffix + extension;
        }
        return filePath + "_" + suffix; // 没有扩展名时直接在文件名末尾添加后缀
    }

    public static void execute(String resFileName, String[] params, List<String> inList) throws Exception {
        File file = FileUtility.releaseTempFile(FileUtility.class.getClassLoader(), resFileName);
        if (file == null) {
            System.out.println("release file failed.");
            return;
        }

        String[] execParams = new String[params.length + 1];
        execParams[0] = file.getAbsolutePath();
        System.arraycopy(params, 0, execParams, 1, params.length);

        ProcessFactory processFactory = ProcessFactory.getInstance();
        processFactory.start(Arrays.asList(execParams), inList, new ProcessCallback() {
            @Override
            public void onNext(Process process, String out) {
                System.out.println(out);
            }

            @Override
            public void onFinish() {

            }
        });
    }
    private static File releaseTempFile(ClassLoader classLoader, String fileName) {
        File file = null;

        try {
            InputStream inputStream = classLoader.getResourceAsStream(fileName + ".exe");
            if (inputStream == null) {
                System.out.println("Unbale get the " + fileName);
                return null;
            }


            file = File.createTempFile(fileName, "");

            File newFile = new File(file.getParentFile().getAbsolutePath() + "/" + fileName + ".exe");
            if (newFile.exists()) {
                file.delete();
                return newFile;
            }

            OutputStream os = new FileOutputStream(file);
            FileUtility.copyStreamAndNotify(inputStream, os);
            os.close();
            inputStream.close();

            if (file.renameTo(newFile)) {
                file = newFile;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file;
    }
}