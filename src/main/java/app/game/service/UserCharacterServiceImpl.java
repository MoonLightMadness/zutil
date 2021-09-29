package app.game.service;

import app.game.UserCharacterService;
import app.game.cons.ServiceCenter;
import app.game.dto.UserCharaterDTO;
import app.game.vo.UserCreateCharacterReqVO;
import app.mapper.annotation.TableName;
import app.utils.SimpleUtils;
import com.alibaba.fastjson.JSON;

import java.time.LocalDateTime;

@TableName("user_characters")
public class UserCharacterServiceImpl implements UserCharacterService {

    @Override
    public void createCharacter(UserCharaterDTO userCharaterDTO) {
        ServiceCenter.mapper.setTableName(this.getClass());
        UserCreateCharacterReqVO userCreateCharacterReqVO = new UserCreateCharacterReqVO();
        userCreateCharacterReqVO.setUserId(userCharaterDTO.getUserId());
        userCreateCharacterReqVO.setCharacterId(userCharaterDTO.getCharacterId());
        userCreateCharacterReqVO.setCharacterData(JSON.toJSONString(userCharaterDTO.getCharacterData()));
        userCreateCharacterReqVO.setCreateTime(LocalDateTime.now().toString());
        userCreateCharacterReqVO.setUpdateTime(LocalDateTime.now().toString());
        userCreateCharacterReqVO.setDeleteFlag("0");
        ServiceCenter.mapper.save(userCreateCharacterReqVO);
    }
}
