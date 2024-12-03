package com.example.numberguessinggameP.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Controller
@RequestMapping("/")
public class GameController {

    private int targetNumber; // 컴퓨터가 맞춰야 할 숫자
    private int attemptCount; // 시도 횟수
    private static final int MAX_ATTEMPTS = 10; // 최대 시도 횟수
    private int numberRange = 100; // 난이도에 따른 숫자 범위 기본값

    public GameController() {
        resetGame();
    }

    // 게임 초기화 메서드
    private void resetGame() {
        Random random = new Random();
        targetNumber = random.nextInt(numberRange) + 1; // 난이도에 따른 범위 내에서 숫자 생성
        attemptCount = 0;
    }

    // 홈 화면 표시
    @GetMapping
    public String home(Model model) {
        model.addAttribute("remainingAttempts", MAX_ATTEMPTS - attemptCount);
        return "index";
    }

    // 숫자 제출 처리
    @PostMapping("/guess")
    public String guess(
            @RequestParam("number") int number,
            @RequestParam(value = "difficulty", defaultValue = "medium") String difficulty,
            Model model) {

        // 첫 시도 시 난이도에 따른 범위 설정
        if (attemptCount == 0) {
            switch (difficulty) {
                case "easy":
                    numberRange = 50; // 쉬움: 1~50
                    break;
                case "hard":
                    numberRange = 200; // 어려움: 1~200
                    break;
                default:
                    numberRange = 100; // 중간: 1~100
            }
            resetGame();
        }

        attemptCount++;

        // 시도 횟수 초과 처리
        if (attemptCount > MAX_ATTEMPTS) {
            model.addAttribute("message", "게임 종료! 시도 횟수를 초과했습니다. 정답은 " + targetNumber + "였습니다.");
            resetGame();
            return "index";
        }

        // 숫자 비교
        if (number == targetNumber) {
            model.addAttribute("message", "정답입니다! 시도 횟수: " + attemptCount + "번");
            resetGame();
        } else if (number < targetNumber) {
            model.addAttribute("message", "더 높은 숫자입니다!");
        } else {
            model.addAttribute("message", "더 낮은 숫자입니다!");
        }

        model.addAttribute("remainingAttempts", MAX_ATTEMPTS - attemptCount);
        return "index";
    }

    // 리셋 버튼 처리
    @GetMapping("/reset")
    public String resetGameRequest() {
        resetGame();
        return "redirect:/";
    }
}
