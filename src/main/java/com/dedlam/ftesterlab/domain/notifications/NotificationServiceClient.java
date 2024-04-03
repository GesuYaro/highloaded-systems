package com.dedlam.ftesterlab.domain.notifications;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("notification-service")
public interface NotificationServiceClient {
  @PostMapping("/notify/web-socket/bulk")
  void bulkNotifyWithWebSockets(@RequestBody BulkNotifyUsersWebSocketRequest request);
}
