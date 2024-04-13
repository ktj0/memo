package com.example.memo.controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MemoController {
    private final JdbcTemplate jdbcTemplate;

    public MemoController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        Memo memo = new Memo(requestDto);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO memo (username, contents) VALUES (?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, memo.getUsername());
            preparedStatement.setString(2, memo.getContents());

            return preparedStatement;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        memo.setId(id);

        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto;
    }

//    @GetMapping("/memos")
//    public List<MemoResponseDto> getMemos() {
//        List<MemoResponseDto> responseList = memoList.values().stream().map(MemoResponseDto::new).toList();
//
//        return responseList;
//    }
//
//    @PutMapping("/memos/{id}")
//    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
//        if (memoList.containsKey(id)) {
//            Memo memo = memoList.get(id);
//
//            memo.update(requestDto);
//
//            return memo.getId();
//        } else {
//            throw new IllegalArgumentException("선택된 메모는 존재하지 않습니다.");
//        }
//    }
//
//    @DeleteMapping("memos/{id}")
//    public Long deleteMemo(@PathVariable Long id) {
//        if (memoList.containsKey(id)) {
//            memoList.remove(id);
//
//            return id;
//        } else {
//            throw new IllegalArgumentException("선택된 메모는 존재하지 않습니다.");
//        }
//    }
}
