package net.ringo.ringoSwap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ringo.ringoSwap.dao.GameDAO;
import net.ringo.ringoSwap.domain.SingleDifficulty;

@Service
public class GameServiceImple implements GameService{
	@Autowired
	GameDAO dao;
	
	@Override
	public SingleDifficulty difficultyCall(String user_id) {
		return dao.difficultyCall(user_id);
	}

}
