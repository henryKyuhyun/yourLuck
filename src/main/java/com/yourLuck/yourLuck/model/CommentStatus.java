package com.yourLuck.yourLuck.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Enumerated;

@RequiredArgsConstructor
@Getter
public enum CommentStatus {

    ACTIVE,
    DELETED
}
