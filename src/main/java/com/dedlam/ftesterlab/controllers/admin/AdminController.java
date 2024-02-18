package com.dedlam.ftesterlab.controllers.admin;

import com.dedlam.ftesterlab.auth.database.UsersRepository;
import com.dedlam.ftesterlab.domain.people.database.PeopleRepository;
import com.dedlam.ftesterlab.domain.university.services.GroupsService;
import com.dedlam.ftesterlab.domain.university.services.StudentsService;
import com.dedlam.ftesterlab.domain.university.services.TeachersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
