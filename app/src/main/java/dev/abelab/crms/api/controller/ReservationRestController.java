package dev.abelab.crms.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.*;
import lombok.*;
import dev.abelab.crms.api.request.ReservationCreateRequest;
import dev.abelab.crms.api.response.ReservationsResponse;

@Api(tags = "Reservation")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ReservationRestController {

    /**
     * 予約一覧取得API
     *
     * @param jwt JWT
     *
     * @return 予約一覧レスポンス
     */
    @ApiOperation(value = "予約一覧の取得", //
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
        @RequestHeader(name = "Authorization", required = true) final String jwt //
    ) {
        // FIXME
        return ReservationsResponse.builder().build();
    }

    /**
     * 予約作成API
     *
     * @param jwt         JWT
     *
     * @param requestBody 予約作成リクエスト
     */
    @ApiOperation(value = "予約の作成", //
        notes = "予約を作成する。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 201, message = "作成成功"), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
                @ApiResponse(code = 409, message = "指定時刻の予約が既に存在している"), //
        } //
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser( //
        @RequestHeader(name = "Authorization", required = true) final String jwt, //
        @Validated @ApiParam(name = "body", required = true, value = "新規予約情報") @RequestBody final ReservationCreateRequest requestBody //
    ) {
        // FIXME
    }

    /**
     * 予約削除API
     *
     * @param jwt           JWT
     *
     * @param reservationId 予約ID
     */
    @ApiOperation(value = "予約の削除", //
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
        @RequestHeader(name = "Authorization", required = true) final String jwt, //
        @ApiParam(name = "reservation_id", required = true, value = "予約ID") @PathVariable("reservation_id") final int reservationId //
    ) {
        // FIXME
    }

}
