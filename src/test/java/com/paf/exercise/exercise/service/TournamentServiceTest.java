package com.paf.exercise.exercise.service;

import com.paf.exercise.exercise.entity.Tournament;
import com.paf.exercise.exercise.repository.TournamentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @author Mohammad Fathizadeh 2020-01-04
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TournamentServiceTest {

    @MockBean
    private TournamentRepository tournamentRepository;

    @Autowired
    private TournamentService tournamentService;

    @Test
    public void getTournamentsTest() {
        when(tournamentRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(tournamentService.getTournaments().isEmpty());

        Tournament tournament = new Tournament(1, 5, null);
        when(tournamentRepository.findAll()).thenReturn(Collections.singletonList(tournament));
        assertArrayEquals(Collections.singletonList(tournament).toArray(), tournamentService.getTournaments().toArray());

    }

    @Test
    public void getTournamentByIdTest() {
        Tournament tournament = new Tournament(1, 5, null);

        when(tournamentRepository.findOne(1)).thenReturn(tournament);
        assertEquals(Optional.of(tournament), tournamentService.getTournamentById(1));

        when(tournamentRepository.findOne(2)).thenReturn(null);
        assertEquals(Optional.empty(), tournamentService.getTournamentById(2));
    }
}