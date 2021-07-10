package dev.abelab.crms.api.controller.internal;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.*;
import lombok.*;
import dev.abelab.crms.api.request.ReservationCreateRequest;
import dev.abelab.crms.api.request.ReservationUpdateRequest;
import dev.abelab.crms.api.response.ReservationsResponse;
import dev.abelab.crms.service.ReservationService;

@Api(tags = "Reservation")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ReservationRestController {

    private final ReservationService reservationService;

    /**
     * 予約一覧取得API
     *
     * @param jwt JWT
     *
     * @return 予約一覧レスポンス
     */
    @ApiOperation( //
        value = "予約一覧の取得", //
        notes = "予約一覧を取得する。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 200, message = "取得成功", response = ReservationsResponse.class), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
        })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ReservationsResponse getReservations( //
        @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) final String jwt //
    ) {
        return this.reservationService.getReservations(jwt);
    }

    /**
     * 予約作成API
     *
     * @param jwt         JWT
     *
     * @param requestBody 予約作成リクエスト
     */
    @ApiOperation( //
        value = "予約の作成", //
        notes = "予約を作成する。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 201, message = "作成成功"), //
                @ApiResponse(code = 400, message = "指定時刻が無効"), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
                @ApiResponse(code = 409, message = "指定時刻の予約が既に存在している"), //
        } //
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createReservation( //
        @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) final String jwt, //
        @Validated @ApiParam(name = "body", required = true, value = "新規予約情報") @RequestBody final ReservationCreateRequest requestBody //
    ) {
        this.reservationService.createReservation(jwt, requestBody);
    }

    /**
     * 予約更新API
     *
     * @param jwt         JWT
     *
     * @param requestBody 予約更新リクエスト
     */
    @ApiOperation( //
        value = "予約の更新", //
        notes = "予約を更新する。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 200, message = "更新成功"), //
                @ApiResponse(code = 400, message = "指定時刻が無効"), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
                @ApiResponse(code = 403, message = "ユーザに権限がない"), //
                @ApiResponse(code = 409, message = "指定時刻の予約が既に存在している"), //
        } //
    )
    @PutMapping(value = "/{reservation_id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateReservation( //
        @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) final String jwt, //
        @ApiParam(name = "reservation_id", required = true, value = "予約ID") @PathVariable("reservation_id") final int reservationId, //
        @Validated @ApiParam(name = "body", required = true, value = "予約更新情報") @RequestBody final ReservationUpdateRequest requestBody //
    ) {
        this.reservationService.updateReservation(jwt, reservationId, requestBody);
    }

    /**
     * 予約削除API
     *
     * @param jwt           JWT
     *
     * @param reservationId 予約ID
     */
    @ApiOperation( //
        value = "予約の削除", //
        notes = "予約を削除する。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 200, message = "削除成功"), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
                @ApiResponse(code = 403, message = "ユーザに権限がない"), //
        } //
    )
    @DeleteMapping(value = "/{reservation_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReservation( //
        @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) final String jwt, //
        @ApiParam(name = "reservation_id", required = true, value = "予約ID") @PathVariable("reservation_id") final int reservationId //
    ) {
        this.reservationService.deleteReservation(jwt, reservationId);
    }

}
