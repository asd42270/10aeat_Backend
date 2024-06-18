package com.final_10aeat.common.dto;

/*
  user = manager + owner
  member = owner
 */
public record UserIdAndRole(
    Long id,
    boolean isManager
) {

}
