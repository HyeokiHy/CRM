package com.example.b2bcrm.deal;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import com.example.b2bcrm.deal.dto.DealCreateRequest;
import com.example.b2bcrm.deal.dto.DealMoveRequest;
import com.example.b2bcrm.deal.dto.DealResponse;
import com.example.b2bcrm.deal.dto.DealUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deals")
public class DealController {

    private final DealService dealService;

    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    @GetMapping
    public List<DealResponse> listDeals(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String stage,
        @RequestParam(required = false) String priority
    ) {
        return dealService.findDeals(search, stage, priority);
    }

    @GetMapping("/{id}")
    public DealResponse getDeal(@PathVariable Long id) {
        return dealService.findDeal(id);
    }

    @PostMapping
    public ResponseEntity<DealResponse> createDeal(@Valid @RequestBody DealCreateRequest request) {
        DealResponse created = dealService.createDeal(request);
        return ResponseEntity
            .created(URI.create("/api/deals/" + created.getId()))
            .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DealResponse> updateDeal(@PathVariable Long id, @Valid @RequestBody DealUpdateRequest request) {
        return ResponseEntity.ok(dealService.updateDeal(id, request));
    }

    @PatchMapping("/{id}/move")
    public ResponseEntity<DealResponse> moveDeal(@PathVariable Long id, @Valid @RequestBody DealMoveRequest request) {
        return ResponseEntity.ok(dealService.moveDeal(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeal(@PathVariable Long id) {
        dealService.deleteDeal(id);
        return ResponseEntity.noContent().build();
    }
}
