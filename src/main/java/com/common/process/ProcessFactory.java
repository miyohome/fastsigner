package com.common.process;

import com.common.FileUtility;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.List;

public class ProcessFactory {
    private static ProcessFactory processFactory;
    public static ProcessFactory getInstance() {
        synchronized (ProcessFactory.class) {
            if (processFactory != null) return processFactory;
            processFactory = new ProcessFactory();
            return processFactory;
        }
    }

    public void start(String command, List<String> in, ProcessCallback callback) throws Exception {
        start(Collections.singletonList(command), in, callback);
    }
    public void start(List<String> commands, List<String> in, ProcessCallback callback) throws Exception {
        // Check param is valid
        if (commands == null || commands.isEmpty()) {
            throw new InvalidParameterException("commands is null or commands is invalid");
        }

        // Create process instance
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        Process process = processBuilder.start();

        if (in != null) {
            OutputStream outputStream = process.getOutputStream();
            // input command
            if (!in.isEmpty()) {
                for (String s : in) {
                    if (s == null) {
                        continue;
                    }
                    outputStream.write(s.concat("\n").getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                }
                outputStream.close();
            }
        }


        if (callback != null) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            FileUtility.copyStreamAndNotify(process.getInputStream(), buffer);
            callback.onNext(process, new String(buffer.toByteArray(), StandardCharsets.UTF_8));
        }

        // check errorStream
        if (process.getErrorStream().available() > 0) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            FileUtility.copyStreamAndNotify(process.getErrorStream(), byteArrayOutputStream);
            System.out.println(byteArrayOutputStream);
        }

        process.waitFor();
        if (callback != null) callback.onFinish();
    }
}
