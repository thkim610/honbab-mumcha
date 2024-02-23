package yougboyclub.honbabstop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yougboyclub.honbabstop.domain.Board;
import yougboyclub.honbabstop.domain.Participants;
import yougboyclub.honbabstop.domain.User;
import yougboyclub.honbabstop.dto.ParticipantsDto;
import yougboyclub.honbabstop.dto.RequestBoardDto;
import yougboyclub.honbabstop.dto.ResponseBoardDto;
import yougboyclub.honbabstop.dto.UpdateBoardRequest;
import yougboyclub.honbabstop.service.BoardService;
import yougboyclub.honbabstop.service.LikesService;
import yougboyclub.honbabstop.service.ParticipantsService;
import yougboyclub.honbabstop.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/board")
@RestController
@CrossOrigin
public class BoardController {

    private final BoardService boardService;
    private final LikesService likesService;
    private final UserService userService;
    private final ParticipantsService participantsService;

    //모든 모집글 조회
    @GetMapping
    public List<ResponseBoardDto> showBoardList() {
        List<Board> boards = boardService.findAllBoard();
        List<ResponseBoardDto> boardDtos = boards.stream()
                .map(board -> new ResponseBoardDto(board))
                .collect(Collectors.toList());

        log.info("boards : {}", boards);
        log.info("boardDto : {}", boardDtos);
        return boardDtos;
    }

    //신규 모집글 작성
    @PostMapping("/new")
    public ResponseEntity<ResponseBoardDto> createBoard(@RequestBody RequestBoardDto dto) {
        System.out.println("이건 컨트롤러로 들어오는 dto: " + dto);
        Board board = boardService.createBoard(dto);

        User user = userService.findByEmail(board.getWriter().getEmail());
        // 유저 이메일 잘 들어오는지 확인
        log.info("user : {}", user);

        participantsService.createParticipant(Participants.builder()
                .board(board)
                .user(user)
                .status(1)
                .build()
        );

        ResponseBoardDto responseBoardDto = new ResponseBoardDto(board);
        return ResponseEntity.ok(responseBoardDto);
    }

    //모집글 조회(음식)
    @GetMapping("/food/{foodCategory}")
    public List<ResponseBoardDto> showBoardListByFood(@PathVariable String foodCategory) {
        log.info("foodcategry : {}", foodCategory);
        List<Board> boards = boardService.findByFoodCategory(foodCategory);
        List<ResponseBoardDto> boardDtos = boards.stream()
                .map(board -> new ResponseBoardDto(board))
                .collect(Collectors.toList());
        log.info("boards : {}", boards);
        log.info("boardDto : {}", boardDtos);
        return boardDtos;
    }

    //모집글 조회(장소)
    @GetMapping("/place/{placeCategory}")
    public List<ResponseBoardDto> showBoardListByPlace(@PathVariable String placeCategory) {
        log.info("placeCategory : {}", placeCategory);
        List<Board> boards = boardService.findByPlaceCategory(placeCategory);
        List<ResponseBoardDto> boardDtos = boards.stream()
                .map(board -> new ResponseBoardDto(board))
                .collect(Collectors.toList());
        log.info("boards : {}", boards);
        log.info("boardDto : {}", boardDtos);
        return boardDtos;
    }

    //모집글 상세 조회(개별 상세 조회)
    @GetMapping("/boardDetails/{id}")
    public ResponseBoardDto findBoardDetailById(@PathVariable Long id, @RequestHeader("User-Id") Long userId) {
        log.info("Header_userId : {}", userId);
        User currentUser = userService.findById(userId); // 현재 로그인한 사용자 정보를 가져옴
        log.info("currentUser : {}", currentUser);
        Board board = boardService.findByIdAndUser(id, currentUser); // 조회수 증가 로직이 포함된 서비스 호출
        log.info("board : {}", board);
        return new ResponseBoardDto(board);
    }

    //모집글 수정
    @PutMapping("/boardDetails/edit/{id}")
    public ResponseEntity<ResponseBoardDto> updateById(@PathVariable Long id, @RequestBody UpdateBoardRequest request) {
        log.info("update request : {}", request);
        Board updateBoard = boardService.updateById(id, request);
        ResponseBoardDto responseBoardDto = new ResponseBoardDto(updateBoard);
        return ResponseEntity.ok().body(responseBoardDto);
    }

    //모집글 삭제
    //외래키 제약 조건 때문에 삭제 안돼ㅠ
    @DeleteMapping("/boardDetails/delete/{id}")
    public ResponseEntity<ResponseBoardDto> deleteById(@PathVariable Long id, @RequestHeader("User-Id") Long userId) {
        // 게시글 존재 확인 및 권한 확인
        boardService.deleteById(id, userId);
        // 삭제 성공 확인
        if (boardService.findById(id) != null) {
            throw new IllegalArgumentException("게시글 삭제에 실패했습니다.");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/findby/{keyword}")
    public List<ResponseBoardDto> showBoardListByKeyword(@PathVariable String keyword) {
        log.info("keyword : {}", keyword);
        List<Board> boards = boardService.findByKeyword(keyword);
        List<ResponseBoardDto> boardDtos = boards.stream()
                .map(board -> new ResponseBoardDto(board))
                .collect(Collectors.toList());
        log.info("boards : {}", boards);
        log.info("boardDto : {}", boardDtos);
        return boardDtos;
    }
}