package com.paf.exercise.exercise.service;

import com.paf.exercise.exercise.entity.Player;
import com.paf.exercise.exercise.entity.Tournament;
import com.paf.exercise.exercise.exception.exception.RecordNotFoundException;
import com.paf.exercise.exercise.repository.PlayerRepository;
import com.paf.exercise.exercise.repository.TournamentRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author Mohammad Fathizadeh 2020-01-04
 */
@Service
@Transactional
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final PlayerRepository playerRepository;

    public TournamentService(TournamentRepository tournamentRepository, PlayerRepository playerRepository) {
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
    }

    public List<Tournament> getTournaments() {
        return tournamentRepository.findAll();
    }

    public Optional<Tournament> getTournamentById(Integer id) {
        return Optional.ofNullable(tournamentRepository.findOne(id));
    }

    public Optional<Tournament> updateTournament(Integer id, com.paf.exercise.exercise.dto.Tournament tournament) {
        return Optional.ofNullable(tournamentRepository.findOne(id)).map(tournamentEnt -> {
            tournamentEnt.setRewardAmount(tournament.getRewardAmount());
            return tournamentRepository.save(tournamentEnt);
        });
    }

    public com.paf.exercise.exercise.entity.Tournament addTournament(com.paf.exercise.exercise.dto.Tournament tournament) {
        com.paf.exercise.exercise.entity.Tournament newTournament = new com.paf.exercise.exercise.entity.Tournament();
        newTournament.setRewardAmount(tournament.getRewardAmount());
        return tournamentRepository.save(newTournament);
    }

    public Optional<Set<Player>> getPlayersByTournamentId(Integer id) {
        return Optional.ofNullable(tournamentRepository.findOne(id)).map(com.paf.exercise.exercise.entity.Tournament::getPlayers);
    }

    public boolean deleteTournamentById(Integer id) {
        if (!tournamentRepository.exists(id)) {
            return false;
        }
        tournamentRepository.delete(id);
        return true;
    }

    public Optional<com.paf.exercise.exercise.entity.Tournament> addPlayerIntoTournament(com.paf.exercise.exercise.entity.Tournament tournament, Integer playerId) {
        return Optional.ofNullable(playerRepository.findOne(playerId)).map(player -> {
            tournament.getPlayers().add(player);
            return tournamentRepository.save(tournament);
        });
    }

    public void deletePlayerFromTournament(Integer tournamentId, Integer playerId) {
        com.paf.exercise.exercise.entity.Tournament tournament = tournamentRepository.findOne(tournamentId);
        if (tournament == null)
            throw new RecordNotFoundException("Tournament not found with id: " + tournamentId);

        Set<Player> players = tournament.getPlayers();
        players.stream().filter(player -> Objects.equals(player.getId(), playerId)).findFirst()
                .map(player -> {
                    tournament.getPlayers().remove(player);
                    return tournamentRepository.save(tournament);
                })
                .orElseThrow(() -> new RecordNotFoundException("Player not found with id: " + playerId + " for tournament with id: " + tournamentId));
    }
}
