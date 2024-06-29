package com.sparta.catube.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    FAILED_CREATE_VIDEO(HttpStatus.INTERNAL_SERVER_ERROR, "동영상을 업로드하지 못했습니다."),
    FAILED_READ_VIDEO(HttpStatus.NOT_FOUND, "동영상을 찾을 수 없습니다."),
    FAILED_UPDATE_VIDEO(HttpStatus.INTERNAL_SERVER_ERROR, "동영상 편집에 실패했습니다."),
    FAILED_DELETE_VIDEO(HttpStatus.INTERNAL_SERVER_ERROR, "동영상 삭제에 실패했습니다."),

    FAILED_WATCH_VIDEO(HttpStatus.NOT_FOUND, "동영상 재생에 실패했습니다."),
    FAILED_STOP_VIDEO(HttpStatus.INTERNAL_SERVER_ERROR, "동영상 재생 중단에 실패했습니다."),
    FAILED_REGISTER_AD(HttpStatus.INTERNAL_SERVER_ERROR, "광고 등록에 실패했습니다.");

    private final HttpStatus status;
    private final String message;
}
