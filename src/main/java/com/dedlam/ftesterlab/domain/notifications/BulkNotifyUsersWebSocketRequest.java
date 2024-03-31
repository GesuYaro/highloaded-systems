package com.dedlam.ftesterlab.domain.notifications;

import java.util.List;
import java.util.UUID;

public record BulkNotifyUsersWebSocketRequest(
  List<UUID> userIds,
  String message
) {
}
