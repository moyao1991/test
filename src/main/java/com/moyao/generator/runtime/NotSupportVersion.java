package com.moyao.generator.runtime;

public interface NotSupportVersion {

    String fieldName = "version";

    static NotSupportVersionException exception() {
        return new NotSupportVersionException();
    }

    class NotSupportVersionException extends RuntimeException {

        public NotSupportVersionException() {
            super("not find field Column :" + fieldName);
        }
    }
}
