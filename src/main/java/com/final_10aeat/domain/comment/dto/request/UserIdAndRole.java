package com.final_10aeat.domain.comment.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserIdAndRole {
    private final Long id;
    private final boolean isManager;

    @Builder
    public UserIdAndRole(Long id, boolean isManager) {
        this.id = id;
        this.isManager = isManager;
    }
}
