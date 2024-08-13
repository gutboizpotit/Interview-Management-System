package btl.g4.mock.security;

import btl.g4.mock.dto.BaseMessageDto;

public interface AuthService {

    BaseMessageDto sendResetPasswordEmail(String email);

    Object resetUserPassword(String code, String email);
}
