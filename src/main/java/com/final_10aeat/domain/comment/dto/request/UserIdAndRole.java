package com.final_10aeat.domain.comment.dto.request;

public record UserIdAndRole(
    Long id,
    boolean isManager
) {

}
