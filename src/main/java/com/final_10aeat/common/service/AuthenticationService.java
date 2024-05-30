package com.final_10aeat.common.service;

import com.final_10aeat.common.dto.UserIdAndRole;
import com.final_10aeat.domain.member.exception.UserNotExistException;
import com.final_10aeat.domain.office.exception.OfficeNotFoundException;
import com.final_10aeat.global.security.principal.ManagerPrincipal;
import com.final_10aeat.global.security.principal.MemberPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public UserIdAndRole getCurrentUserIdAndRole() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MemberPrincipal) {
            return new UserIdAndRole(((MemberPrincipal) principal).getMember().getId(), false);
        } else if (principal instanceof ManagerPrincipal) {
            return new UserIdAndRole(((ManagerPrincipal) principal).getManager().getId(), true);
        }
        throw new UserNotExistException();
    }

    public Long getUserOfficeId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MemberPrincipal) {
            return ((MemberPrincipal) principal).getMember().getDefaultOffice();
        } else if (principal instanceof ManagerPrincipal) {
            return ((ManagerPrincipal) principal).getManager().getOffice().getId();
        }
        throw new OfficeNotFoundException();
    }
}
