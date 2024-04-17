package com.example.memo.service;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import com.example.memo.repository.MemoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;

    public MemoResponseDto createMemo(MemoRequestDto requestDto) {
        Memo memo = new Memo(requestDto);

        Memo saveMemo = memoRepository.save(memo);

        MemoResponseDto memoResponseDto = new MemoResponseDto(saveMemo);

        return memoResponseDto;
    }

    public List<MemoResponseDto> getMemos() {
        return memoRepository.findAll().stream().map(MemoResponseDto::new).toList();
    }

    private Memo findMemo(Long id) {
        return memoRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("선택된 메모는 존재하지 않습니다.")
        );
    }

    @Transactional
    public Long updateMemo(long id, MemoRequestDto requestDto) {
        Memo memo = findMemo(id);

        memo.update(requestDto);

        return id;
    }

    public Long deleteMemo(Long id) {
        Memo memo = findMemo(id);

        memoRepository.delete(memo);

        return id;
    }
}
