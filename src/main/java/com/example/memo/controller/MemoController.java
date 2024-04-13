package com.example.memo.controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        String requestUsername = requestDto.getUsername();
        String requestContents = requestDto.getContents();

        Memo memo = new Memo(requestUsername, requestContents);

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

        String username = memo.getUsername();
        String contents = memo.getContents();


        MemoResponseDto memoResponseDto = new MemoResponseDto(id, username, contents);

        return memoResponseDto;
    }

    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        String sql = "SELECT * FROM memo";

        return jdbcTemplate.query(sql, new RowMapper<MemoResponseDto>() {
            @Override
            public MemoResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String contents = rs.getString("contents");

                return new MemoResponseDto(id, username, contents);
            }
        });
    }

    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        Memo memo = findById(id);

        if (memo != null) {
            String sql = "UPDATE memo SET username = ?, contents = ? where id = ?";

            jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getContents(), id);

            return id;
        } else {
            throw new IllegalArgumentException("선택된 메모는 존재하지 않습니다.");
        }
    }
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

    private Memo findById(Long id) {
        String sql = "SELECT * FROM memo WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String contnets = resultSet.getString("contents");

                Memo memo = new Memo(username, contnets);

                return memo;
            } else {
                return null;
            }
        }, id);
    }
}
