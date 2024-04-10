package com.example.memo.controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MemoController {
    // DB가 없기 때문에 메모를 저장할 map
    private final Map<Long, Memo> memoList = new HashMap<>();

    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        Memo memo = new Memo(requestDto);

        Long maxId = !memoList.isEmpty() ? Collections.max(memoList.keySet()) + 1 : 1;
        memo.setId(maxId);

        memoList.put(memo.getId(), memo);

        return new MemoResponseDto(memo);
    }

    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        List<MemoResponseDto> responseList = memoList.values().stream().map(MemoResponseDto::new).toList();

        return responseList;
    }

    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        if (memoList.containsKey(id)) {
            Memo memo = memoList.get(id);

            memo.update(requestDto);

            return memo.getId();
        } else {
            throw new IllegalArgumentException("선택된 메모는 존재하지 않습니다.");
        }
    }

    @DeleteMapping("memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        if (memoList.containsKey(id)) {
            memoList.remove(id);

            return id;
        } else {
            throw new IllegalArgumentException("선택된 메모는 존재하지 않습니다.");
        }
    }
}
