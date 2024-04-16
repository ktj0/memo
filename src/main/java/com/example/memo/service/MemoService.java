package com.example.memo.service;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import com.example.memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;

    public MemoResponseDto createMemo(MemoRequestDto requestDto) {
        String username = requestDto.getUsername();
        String contents = requestDto.getContents();

        Memo memo = new Memo(username, contents);

        Memo saveMemo = memoRepository.save(memo);

        MemoResponseDto memoResponseDto = new MemoResponseDto(saveMemo);

        return memoResponseDto;
    }

    public List<MemoResponseDto> getMemos() {
        return memoRepository.findAll();
    }

    public Long updateMemo(long id, MemoRequestDto requestDto) {
        Memo memo = memoRepository.findById(id);

        if (memo != null) {
            memoRepository.update(id, requestDto);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    public Long deleteMemo(Long id) {
        Memo memo = memoRepository.findById(id);

        if (memo != null) {
            memoRepository.delete(id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }
}
