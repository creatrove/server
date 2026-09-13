package com._6.creatrove.memo.exception;

public class MemoNotFoundException extends RuntimeException {
    public MemoNotFoundException(Long memoId) {
        super("존재하지 않는 메모입니다. id=" + memoId);
    }
}