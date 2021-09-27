package app.game;

import app.game.domain.UserOnline;

public interface BaseUserOnlineService {

    UserOnline getUserOnlieByUserId(String userId);

    UserOnline getUserOnlineByLogToken(String logToken);


}
