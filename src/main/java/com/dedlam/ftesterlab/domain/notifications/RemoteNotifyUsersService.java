package com.dedlam.ftesterlab.domain.notifications;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RemoteNotifyUsersService implements NotifyUsersService {
  private final NotificationServiceClient notificationServiceClient;

  public RemoteNotifyUsersService(NotificationServiceClient notificationServiceClient) {
    this.notificationServiceClient = notificationServiceClient;
  }

  @Override
  public void notify(List<UUID> userIds, String message) {
    var request = new BulkNotifyUsersWebSocketRequest(userIds, message);
    notificationServiceClient.bulkNotifyWithWebSockets(request);
  }
}
