package com.dedlam.ftesterlab.controllers.admin;

import com.dedlam.ftesterlab.domain.university.services.GroupsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
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

  @GetMapping("/test")
  public String test() {
    return "Hello world!\ncurrent date and time: " + ZonedDateTime.now(ZoneId.systemDefault()).toString();
  }

  public record CreateGroupRequest(
    String name,
    int grade,
    Set<String> subjectNames
  ) {
  }
}
