package com.example.memo.dto;

import com.example.memo.entity.Memo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class MemoResponseDto {
    private Long id;
    private String username;
    private String contents;

    public MemoResponseDto(Memo memo) {
        this.id = memo.getId();
        this.username = memo.getUsername();
        this.contents = memo.getContents();
    }
//    public MemoResponseDto(Memo memo) {
//        this.id = memo.getId();
//        this.username = memo.getUsername();
//        this.contents = memo.getContents();
//    }
}
