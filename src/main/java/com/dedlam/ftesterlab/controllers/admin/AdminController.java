package com.dedlam.ftesterlab.controllers.admin;

import com.dedlam.ftesterlab.domain.university.services.GroupsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("admin")
public class AdminController {
  private final GroupsService groupsService;

  public AdminController(GroupsService groupsService) {
    this.groupsService = groupsService;
  }

  @PostMapping("groups")
  public ResponseEntity<Boolean> createGroup(@RequestBody CreateGroupRequest request) {
    boolean isSuccess = groupsService.createGroup(request.name, request.grade, request.subjectNames);
    return ResponseEntity.ok(isSuccess);
  }

  public record CreateGroupRequest(
    String name,
    int grade,
    Set<String> subjectNames
  ) {
  }
}
