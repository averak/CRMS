package dev.abelab.crms.api.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.*;
import lombok.*;

@Api(tags = "Batch")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/batch", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class BatchRestController {

    /**
     * 予約抽選API
     */
    @ApiOperation( //
        value = "予約の抽選", //
        notes = "予約を抽選する。" //
    )
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "成功"), //
    } //
    )
    @GetMapping("/reservations/lottery")
    public void lotteryReservations() {
        // FIXME
    }

}
