package com.final_10aeat.domain.alarm.event;

import com.final_10aeat.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record CommentEvent(
        Member member,
        Long commentId
) {
}
