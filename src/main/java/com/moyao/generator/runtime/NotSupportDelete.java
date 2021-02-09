package com.moyao.generator.runtime;

public interface NotSupportDelete {

    String fieldName = "deleted";

    static NotSupportDeleteException exception() {
        return new NotSupportDeleteException();
    }

    class NotSupportDeleteException extends RuntimeException {

        public NotSupportDeleteException() {
            super("not find field Column :" + fieldName);
        }
    }

}
