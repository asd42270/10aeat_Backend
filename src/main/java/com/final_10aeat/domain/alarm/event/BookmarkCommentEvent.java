package com.final_10aeat.domain.alarm.event;

import com.final_10aeat.domain.member.entity.Member;
import lombok.Builder;

import java.util.List;

@Builder
public record BookmarkCommentEvent(
        List<Member> members,
        Long articleId
) {
}
