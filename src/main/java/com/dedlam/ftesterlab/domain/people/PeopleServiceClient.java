package com.dedlam.ftesterlab.domain.people;

import com.dedlam.ftesterlab.domain.people.models.Contact;
import com.dedlam.ftesterlab.domain.people.models.Person;
import com.dedlam.ftesterlab.domain.people.services.CreatePersonRequest;
import com.dedlam.ftesterlab.domain.people.services.PeopleResponse;
import com.dedlam.ftesterlab.domain.people.services.PersonDto;
import jakarta.annotation.Nullable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient("people-service")
public interface PeopleServiceClient {
  @RequestMapping(method = RequestMethod.GET, value = "/people/info/bulk")
  PeopleResponse getPeople(@RequestParam int pageNumber);

  @RequestMapping(method = RequestMethod.GET, value = "/people/{personId}/info")
  @Nullable
  Person getPersonInfo(@PathVariable UUID personId);

  @RequestMapping(method = RequestMethod.GET, value = "/people/info/by-user-id")
  @Nullable
  Person getPersonInfoByUserId(@RequestParam UUID userId);

  @RequestMapping(method = RequestMethod.POST, value = "/people/info")
  @Nullable
  UUID createPerson(@RequestBody CreatePersonRequest createPersonRequest);

  @RequestMapping(method = RequestMethod.POST, value = "/people/{personId}/info")
  void updatePerson(@PathVariable UUID personId, @RequestBody PersonDto personInfo);



  @RequestMapping(method = RequestMethod.GET, value = "/people/{personId}/contacts")
  @Nullable
  List<Contact> getContacts(@PathVariable UUID personId);

  @RequestMapping(method = RequestMethod.PUT, value = "/people/{personId}/contacts")
  void updateContacts(@PathVariable UUID personId, @RequestBody List<ContactDto> contacts);
}
