package yougboyclub.honbabstop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yougboyclub.honbabstop.domain.Board;

public interface BoardRepository extends JpaRepository<Board,Long> {
}