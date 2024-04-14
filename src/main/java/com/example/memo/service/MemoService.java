package com.example.memo.service;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MemoService {
    private final JdbcTemplate jdbcTemplate;

    public MemoService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MemoResponseDto createMemo(MemoRequestDto requestDto) {
        String username = requestDto.getUsername();
        String contents = requestDto.getContents();

        Memo memo = new Memo(username, contents);

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

        MemoResponseDto memoResponseDto = new MemoResponseDto(id, username, contents);

        return memoResponseDto;
    }

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

    public Long updateMemo(long id, MemoRequestDto requestDto) {
        Memo memo = findById(id);

        if (memo != null) {
            String sql = "UPDATE memo SET username = ?, contents = ? WHERE id = ?";

            jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getContents(), id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    public Long deleteMemo(Long id) {
        Memo memo = findById(id);

        if (memo != null) {
            String sql = "DELETE FROM memo WHERE id = ?";

            jdbcTemplate.update(sql, id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    private Memo findById(Long id) {

        String sql = "SELECT * FROM memo WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String contents = resultSet.getString("contents");

                Memo memo = new Memo(username, contents);

                return memo;
            } else {
                return null;
            }
        }, id);
    }
}
