package yougboyclub.honbabstop.service;

import org.springframework.data.repository.query.Param;
import yougboyclub.honbabstop.domain.Board;
import yougboyclub.honbabstop.domain.User;
import yougboyclub.honbabstop.dto.RequestBoardDto;
import yougboyclub.honbabstop.dto.ResponseBoardDto;
import yougboyclub.honbabstop.dto.UpdateBoardRequest;

import java.util.List;

public interface BoardService {
  
    List<Board> findAllBoard();
  
    List<Board> findByFoodCategory(String foodCategory);
  
    List<Board> findByPlaceCategory(String placeCategory);
  
    Board postBoard(RequestBoardDto requestBoardDto);
  
    // 내가 작성한 글을 조회
    public List<Board> findByWriter(User user);

    Board update(Long id, UpdateBoardRequest request);

    List<Board> findByKeyword(String keyword);

    List<Board> findByUserNonWriter(User user);

    List<Board> findByUser(User user);

    void deleteById(Long boardId);

    Board findById(Long id);

    Board updateById(Long id, UpdateBoardRequest request);
}

  
//   Board createBoard(RequestBoardDto requestBoardDto, Long id);
//
//   // 내가 작성한 글을 조회
//   public List<Board> findByWriter(User user);
//
//   Board update(Long id, UpdateBoardRequest request);
//
//   ResponseBoardDto getBoardDetail(Long id, User user);
//
//   List<Board> findAllBoard();
//
//   List<Board> findByFoodCategory(String foodCategory);
//
//   List<Board> findByPlaceCategory(String placeCategory);
//
//   List<Board> findByWriter(User user);
//
//   Board updateById(Long id, UpdateBoardRequest request);
//
//   List<Board> findAllBoard();
//
//   List<Board> findByFoodCategory(String foodCategory);
//
//   List<Board> findByPlaceCategory(String placeCategory);
