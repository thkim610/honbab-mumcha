package yougboyclub.honbabstop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yougboyclub.honbabstop.domain.Participants;

public interface ParticipantsRepository extends JpaRepository<Participants,Long> {
}