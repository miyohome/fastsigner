package com.common.process;

public interface ProcessCallback {
    void onNext(Process process, String out);

    void onFinish();
}
