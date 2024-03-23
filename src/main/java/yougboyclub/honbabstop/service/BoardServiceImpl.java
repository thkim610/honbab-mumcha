package yougboyclub.honbabstop.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yougboyclub.honbabstop.domain.Board;
import yougboyclub.honbabstop.domain.User;
import yougboyclub.honbabstop.dto.RequestBoardDto;
import yougboyclub.honbabstop.dto.UpdateBoardRequest;
import yougboyclub.honbabstop.repository.BoardRepository;
import yougboyclub.honbabstop.repository.ParticipantsRepository;
import yougboyclub.honbabstop.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final ParticipantsRepository participantsRepository;

    private final UserRepository userRepository;

    //모집글 작성
    @Override
    public Board createBoard(RequestBoardDto requestBoardDto) {
        System.out.println("user : " + requestBoardDto.getWriter().getEmail());
        Optional<User> optionalUser = userRepository.findByEmail(requestBoardDto.getWriter().getEmail());
        if (optionalUser.isPresent()) {
            User getUser = optionalUser.get();
            log.info("모집글 작성자 = {}", getUser);
            Board board = requestBoardDto.toEntity();
            board.setHit(0L);
            board.setWriter(getUser);
            log.info("엔터티 변경 완료 모집글 = {} ", board);
            log.info("생성한 board_id = {}", board.getId());
            return boardRepository.save(board);
        }
        return null;
    }

    //모든 모집글 조회
    @Override
    public List<Board> findAllBoard() {
        return boardRepository.findAll();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public List<Board> findByFoodCategory(String foodCategory) {
        return boardRepository.findByFoodCategory(foodCategory);
    }

    @Override
    public List<Board> findByPlaceCategory(String placeCategory) {
        return boardRepository.findByPlaceCategory(placeCategory);
    }

    @Override
    public List<Board> findByWriter(User user) {
        return boardRepository.findByWriter(user);
    }

    @Override
    @Transactional
    public Board update(Long id, UpdateBoardRequest request) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        board.update(request.getTitle(),
                    request.getContent(),
                    request.getTime(),
                    request.getMeetDate(),
                    request.getFoodCategory(),
                    request.getPlaceCategory(),
                    request.getPeople(),
                    request.getRestaurantName(),
                    request.getRestaurantAddress(),
                    request.getLocationX(),
                    request.getLocationY());
        return board;
    }

    @Override
    public List<Board> findByKeyword(String keyword) {
        return boardRepository.findByKeyword(keyword);
    }


    //모집글 상세조회(모집글 번호)
    @Override
    public Board findByIdAndUser(Long id, User currentUser) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("찾지 못했습니다: " + id));
        // 본인 게시글이 아닐 경우에만 조회수 증가
        if (!board.getWriter().getId().equals(currentUser.getId())) {
            board.increaseHit();
        }
        boardRepository.save(board);
        return board;
    }

    @Override
    public Board findById(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("찾지 못했습니다: " + id));
        return board;
    }

    @Override
    public List<Board> findByUserNonWriter(User user) {
        return participantsRepository.findByUserNonWriter(user);
    }


    @Override
    public List<Board> findByUser(User user) {
        return participantsRepository.findBoardByUser(user);
    }


    //특정 모집글 수정
    @Override
    @Transactional
    public Board updateById(Long id, UpdateBoardRequest request) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("찾지 못했습니다. : " + id));
        board.update(request.getTitle(),
                request.getContent(),
                request.getTime(),
                request.getMeetDate(),
                request.getFoodCategory(),
                request.getPlaceCategory(),
                request.getPeople(),
                request.getRestaurantName(),
                request.getRestaurantAddress(),
                request.getLocationX(),
                request.getLocationY());
        boardRepository.save(board);
        return board;
    }

    //특정 모집글 삭제
    @Override
    @Transactional
    public void deleteById(Long id, Long userId) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        // 권한 확인
        if (!board.getWriter().getId().equals(userId)) {
            throw new IllegalArgumentException("게시글 삭제 권한이 없습니다.");
        }
        boardRepository.deleteById(id);
    }
}