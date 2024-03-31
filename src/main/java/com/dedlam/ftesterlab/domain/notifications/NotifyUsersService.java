package com.dedlam.ftesterlab.domain.notifications;

import java.util.List;
import java.util.UUID;

public interface NotifyUsersService {
  void notify(List<UUID> userIds, String message);
}
