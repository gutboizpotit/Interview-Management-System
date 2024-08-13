package btl.g4.mock.notification;

import org.springframework.scheduling.annotation.Async;

import btl.g4.mock.config.AsyncTaskConfig;

public interface MessengerService {

    @Async(AsyncTaskConfig.BEAN_ASYNC_EXECUTOR)
    void sendMessageInAsync(EmailMessage messageInfo);
}
