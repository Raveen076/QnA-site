package lk.ac.sjp.foe.co4353.g6.voteservice.controller;

import lk.ac.sjp.foe.co4353.g6.voteservice.models.AnswerVote;
import lk.ac.sjp.foe.co4353.g6.voteservice.models.QuestionVote;
import lk.ac.sjp.foe.co4353.g6.voteservice.models.VoteState;
import lk.ac.sjp.foe.co4353.g6.voteservice.repository.AnswerVotesRepository;
import lk.ac.sjp.foe.co4353.g6.voteservice.repository.QuestionVotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votes")
public class VoteServiceController {

    @Autowired
    AnswerVotesRepository answerVotesRepository;

    @Autowired
    QuestionVotesRepository questionVotesRepository;


    @GetMapping("/answer/{answerId}")
    public ResponseEntity<Integer> getAnswerVotes(@PathVariable long answerId){
        try {
            List<AnswerVote> answerVotes =  answerVotesRepository.findByAnswerId(answerId);

            if (answerVotes == null){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else{

                int sum = 0;
                for (AnswerVote temp : answerVotes) {
                    sum += temp.getVote();
                }
                return new ResponseEntity<>(sum, HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<Integer> getQuestionVotes(@PathVariable long questionId){
        try {
            List<QuestionVote> questionVotes =  questionVotesRepository.findByQuestionId(questionId);

            if (questionVotes == null){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else{
                int sum = 0;
                for (QuestionVote temp : questionVotes) {
                    sum += temp.getVote();
                }
                return new ResponseEntity<>(sum, HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/state/user/{userId}/answer/{answerId}")
    public ResponseEntity<VoteState> getAnswerVoteState(@PathVariable long userId, @PathVariable long answerId){
        try {
        AnswerVote answerVote = answerVotesRepository.findByAnswerIdAndUserId(answerId, userId);
        if (answerVote == null){
            return new ResponseEntity<>(new VoteState(0),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new VoteState(answerVote.getVote()), HttpStatus.OK);
        }

        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/state/user/{userId}/question/{questionId}")
    public ResponseEntity<VoteState> getQuestionVoteState(@PathVariable long userId, @PathVariable long questionId){
        try {
            QuestionVote questionVote = questionVotesRepository.findByQuestionIdAndUserId(questionId, userId);
            if (questionVote == null){
                return new ResponseEntity<>(new VoteState(0),HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new VoteState(questionVote.getVote()), HttpStatus.OK);
            }

        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/vote/user/{userId}/question/{questionId}/vote/{vote}")
    public ResponseEntity<QuestionVote> setQuestionVote(@PathVariable long userId, @PathVariable long questionId, @PathVariable int vote){

        try {
            QuestionVote questionVote = questionVotesRepository.findByQuestionIdAndUserId(questionId, userId);
            if (questionVote == null){
                QuestionVote questionVote2 = questionVotesRepository.save(new QuestionVote(questionId, userId, vote));
                return new ResponseEntity<>(questionVote2, HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/vote/user/{userId}/answer/{answerId}/vote/{vote}")
    public ResponseEntity<AnswerVote> setAnswerVote(@PathVariable long userId, @PathVariable long answerId, @PathVariable int vote){

        try {
            AnswerVote answerVote = answerVotesRepository.findByAnswerIdAndUserId(answerId, userId);
            if (answerVote == null){
                AnswerVote answerVote2 = answerVotesRepository.save(new AnswerVote(answerId, userId, vote));
                return new ResponseEntity<>(answerVote2, HttpStatus.CREATED);
            }else{
                //question id and user id already voted
                return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
