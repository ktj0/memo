package com.example.memo.repository;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemoRepository {
    private final JdbcTemplate jdbcTemplate;

    public Memo save(Memo memo) {
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

        return memo;
    }

    public List<MemoResponseDto> findAll() {
        String sql = "SELECT * FROM memo";

        return jdbcTemplate.query(sql, new RowMapper<MemoResponseDto>() {
            @Override
            public MemoResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                Memo memo = new Memo(rs.getString("username"), rs.getString("contents"));

                memo.setId(rs.getLong("id"));

                return new MemoResponseDto(memo);
            }
        });
    }

    public void update(Long id, MemoRequestDto requestDto) {
        String sql = "UPDATE memo SET username = ?, contents = ? WHERE id = ?";

        jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getContents(), id);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM memo WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }

    public Memo findById(Long id) {
        String sql = "SELECT * FROM memo WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                Memo memo = new Memo(resultSet.getLong("id"), resultSet.getString("username"), resultSet.getString("contents"));

                return memo;
            } else {
                return null;
            }
        }, id);
    }
}
