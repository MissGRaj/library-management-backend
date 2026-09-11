package com.example.library_management.controller;

import com.example.library_management.dto.request.BorrowingRequest;
import com.example.library_management.dto.response.BorrowingResponse;
import com.example.library_management.entity.Borrowing;
import com.example.library_management.service.BorrowingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borrowings")
public class BorrowingController {

    private final BorrowingService borrowingService;

    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @PostMapping
    public ResponseEntity<BorrowingResponse> borrowBook(
            @Valid @RequestBody BorrowingRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        BorrowingResponse response = borrowingService.borrowBook(
                request.getBookId(),
                username);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{borrowingId}/return")
    public ResponseEntity<BorrowingResponse> returnBook(
            @PathVariable Long borrowingId,
            Authentication authentication) {

        String username = authentication.getName();

        BorrowingResponse response =
                borrowingService.returnBook(borrowingId, username);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<BorrowingResponse>> getMyBorrowings(
            Authentication authentication) {

        String username = authentication.getName();

        List<BorrowingResponse> response =
                borrowingService.getMyBorrowings(username);

        return ResponseEntity.ok(response);
    }


}