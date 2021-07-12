package dev.abelab.crms.api.controller.internal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.*;
import lombok.*;
import dev.abelab.crms.service.ReservationService;

@Api(tags = "Batch")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/batch", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class BatchRestController {

    private final ReservationService reservationService;

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
    @ResponseStatus(HttpStatus.OK)
    public void lotteryReservations() {
        this.reservationService.lotteryReservations();
    }

}
