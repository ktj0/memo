package com.example.memo.service;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import com.example.memo.repository.MemoRepository;
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

        MemoRepository memoRepository = new MemoRepository(jdbcTemplate);

        Memo saveMemo = memoRepository.save(memo);

        MemoResponseDto memoResponseDto = new MemoResponseDto(saveMemo);

        return memoResponseDto;
    }

    public List<MemoResponseDto> getMemos() {
        MemoRepository memoRepository = new MemoRepository(jdbcTemplate);

        return memoRepository.findAll();
    }

    public Long updateMemo(long id, MemoRequestDto requestDto) {
        MemoRepository memoRepository = new MemoRepository(jdbcTemplate);

        Memo memo = memoRepository.findById(id);

        if (memo != null) {
            memoRepository.update(id, requestDto);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    public Long deleteMemo(Long id) {
        MemoRepository memoRepository = new MemoRepository(jdbcTemplate);

        Memo memo = memoRepository.findById(id);

        if (memo != null) {
            memoRepository.delete(id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }
}
